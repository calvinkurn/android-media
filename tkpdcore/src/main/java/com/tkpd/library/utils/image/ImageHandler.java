package com.tkpd.library.utils.image;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.tokopedia.core.myproduct.utils.FileUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static android.R.attr.bitmap;
import static com.tokopedia.core.newgallery.GalleryActivity.getOutputMediaFile;

/**
 * Created by normansyahputa on 9/1/16.
 */

public class ImageHandler {

    private static final int DEFAULT_WIDTH = 2048;
    private Context context;

    public ImageHandler(Context context) {
        this.context = context;
    }

    public void loadImage(ImageView imageview, @DrawableRes int resId) {
        Glide.with(context)
                .load(resId)
                .placeholder(resId)
                .dontAnimate()
                .error(resId)
                .into(imageview);
    }

    public void loadImage(ImageView imageView, @NonNull  String url){
        Glide.with(context)
                .load(url)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .dontAnimate()
                .into(imageView);
    }

    public void loadImage(ImageView imageView, @NonNull String url, SimpleTarget<Bitmap> simpleTarget){
        Log.d(ImageHandler.class.getSimpleName(), "url : "+url);
        if(simpleTarget == null)
            return;

        Glide.with(context)
                .load(url)
                .asBitmap()
                .skipMemoryCache( true )
                .diskCacheStrategy( DiskCacheStrategy.NONE )
                .into(simpleTarget);
    }

    public void loadImage(ImageView imageView, @NonNull String url, boolean withCache){
        if(withCache){
            loadImage(imageView, url);
        }else{
            Glide.with(context)
                    .load(url)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true)
                    .dontAnimate()
                    .into(imageView);
        }
    }

    public interface OnResourceReady{
        void onResourceReady(Bitmap bitmap);
    }
    public static void downloadImage(Context context,
                                             final String url,
                                             final OnResourceReady listener ) {
        Glide.with(context)
                .load(url)
                .asBitmap()
                .into(new SimpleTarget<Bitmap>(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL) {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation glideAnimation) {
                        listener.onResourceReady(resource);
                    }
                });

//        try {
//
//            Bitmap bitmap = Glide.with(context)
//                    .load(url)
//                    .asBitmap()
//                    .into(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
//                    .get();
//            Bitmap resizedBitmap;
//            if (bitmap.getWidth() > DEFAULT_WIDTH || bitmap.getHeight() > DEFAULT_WIDTH) {
//                resizedBitmap = com.tkpd.library.utils.ImageHandler.ResizeBitmap(bitmap, DEFAULT_WIDTH);
//            }
//            else {
//                resizedBitmap = bitmap;
//            }
//            File directory = new File(FileUtils.getFolderPathForUpload(Environment.getExternalStorageDirectory().getAbsolutePath()));
//            if (!directory.exists()) {
//                directory.mkdirs();
//            }
//            try {
//                File outputFile = getOutputMediaFile();
//                if (outputFile.exists()) {
//                    outputFile.delete();
//                }
//                FileOutputStream bao = new FileOutputStream(outputFile);
//                resizedBitmap.compress(Bitmap.CompressFormat.JPEG,
//                        compressionQuality > 100? 100 : compressionQuality, bao);
//                return outputFile.getAbsolutePath();
//            } catch (FileNotFoundException e) {
//                return null;
//            }
//        } catch (InterruptedException e) {
//            return null;
//        } catch (ExecutionException e) {
//            return null;
//        }
//    }
    }

}
