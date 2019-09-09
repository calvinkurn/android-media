package com.tkpd.library.utils.image;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.widget.ImageView;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.SimpleTarget;

/**
 * Created by normansyahputa on 9/1/16.
 */

public class ImageHandler {
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
                .asBitmap()
                .load(url)
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
}
