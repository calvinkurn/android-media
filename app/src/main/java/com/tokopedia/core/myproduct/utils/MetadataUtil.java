package com.tokopedia.core.myproduct.utils;

import android.util.Log;

import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Metadata;
import com.drew.metadata.jpeg.JpegDirectory;
import com.drew.metadata.png.PngDirectory;

import java.io.File;
import java.io.IOException;

import static com.tokopedia.core.myproduct.presenter.ImageGalleryImpl.Pair;

/**
 * Created by m.normansyah on 18/12/2015.
 */
public class MetadataUtil {
    private static final String TAG = MetadataUtil.class.getSimpleName();
    private static final String messageTAG = TAG+" : ";

    /**
     *
     * @param path valid path
     * @return {@link Pair} with width and height, null if path is not file
     */
    public static Pair<Integer, Integer> getWidthFromImage(String path) throws UnSupportedimageFormatException{
        Pair<Integer, Integer> result = null;
        int width = -1;
        int height = -1;
        if(path==null)
            throw new RuntimeException("need to supply valid path");

        File imageFile = new File(path);
        try {
            if(!imageFile.exists()||imageFile.isDirectory())
                return null;

            Metadata metadata = ImageMetadataReader.readMetadata(imageFile);
            if (isJPEGImage(path)){
                JpegDirectory jpeg
                        = metadata.getFirstDirectoryOfType(JpegDirectory.class);
                if(jpeg!=null){
                    width = Integer.parseInt(jpeg.getDescription(JpegDirectory.TAG_IMAGE_HEIGHT).replace("pixels", "").trim());
                    height = Integer.parseInt(jpeg.getDescription(JpegDirectory.TAG_IMAGE_WIDTH).replace("pixels", "").trim());
                    result = new Pair<>(width, height);
                    Log.d(TAG, messageTAG+" : width["+width+","+height+"]");
                }
            }else if(isPNGImage(path)){

                PngDirectory png = metadata.getFirstDirectoryOfType(PngDirectory.class);
                if(png!=null){
                    width = Integer.parseInt(png.getDescription(PngDirectory.TAG_IMAGE_WIDTH).replace("pixels","").trim());
                    height = Integer.parseInt(png.getDescription(PngDirectory.TAG_IMAGE_HEIGHT).replace("pixels", "").trim());
                    result = new Pair<>(width, height);
                    Log.d(TAG, messageTAG+" : width["+width+","+height+"]");
                }
            }else{
                throw new UnSupportedimageFormatException();
            }
        }catch(IOException | ImageProcessingException e){
            Log.e(TAG, messageTAG+e.getLocalizedMessage());
        }
        return result;
    }

    public static boolean isJPEGImage(String path) throws IOException, ImageProcessingException{
        File imageFile = new File(path);
        Metadata metadata = ImageMetadataReader.readMetadata(imageFile);
        JpegDirectory jpeg
                = metadata.getFirstDirectoryOfType(JpegDirectory.class);
        return jpeg!= null;
    }

    public static boolean isPNGImage(String imagePath) throws IOException, ImageProcessingException{
        File imageFile = new File(imagePath);
        Metadata metadata = ImageMetadataReader.readMetadata(imageFile);
        PngDirectory png = metadata.getFirstDirectoryOfType(PngDirectory.class);
        return (png!=null);
    }

    public static final class UnSupportedimageFormatException extends Exception{
        public UnSupportedimageFormatException() {
            super("Tipe Gambar tidak didukung");
        }
    }
}
