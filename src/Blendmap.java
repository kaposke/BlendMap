import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Created by guilherme.bassa on 11/09/2017.
 */
public class Blendmap {

    public BufferedImage BlendImage (BufferedImage image)
    {
        BufferedImage out = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB);

        float maxHeight = getMaxHeight(image);
        for (int i = 0; i < image.getHeight(); i++) {
            for (int j = 0; j < image.getWidth(); j++) {
                Color color = new Color(image.getRGB(j,i));
                int tone = color.getRed();
                float h = tone / maxHeight;
                Color outColor = new Color(
                        calcLinear(0.75f, 1.00f, h, false),
                        calcPiramid(0.20f, 0.80f, h),
                        calcLinear(0f, 0.30f, h, true)
                );
                out.setRGB(j,i,outColor.getRGB());
            }
        }
        return out;
    }

    private float getMaxHeight (BufferedImage image)
    {
        float max = 0;
        for (int i = 0; i < image.getHeight(); i++) {
            for (int j = 0; j < image.getWidth(); j++) {
                Color color = new Color(image.getRGB(j,i));
                max = color.getRed() > max ? color.getRed() : max;
            }
        }
        return max;
    }

    private static float calcLinear(float min, float max, float value, boolean inverse) {
        float range = max - min;
        float result = (value - min) / range;
        result = result < 0 ? 0 :
                (result > 1 ? 1 : result);
        return inverse ? 1 - result : result;
    }

    private static float calcPiramid(float min, float max, float value) {
        float mid = (min + max) / 2.0f;
        return value <= mid ?
                calcLinear(min, mid, value, false) :
                calcLinear(mid, max, value, true);
    }

    public void run() throws IOException{
        String PATH = "C:/Temp/img/opengl/heights";
        BufferedImage heightMap = ImageIO.read(new File(PATH, "river.png"));

        BufferedImage blendmap = BlendImage(heightMap);

        ImageIO.write(blendmap, "png", new File("blendImg.png"));
        System.out.println("Laroske");
    }

    public static void main(String args[]) throws IOException
    {
        new Blendmap().run();
    }

}
