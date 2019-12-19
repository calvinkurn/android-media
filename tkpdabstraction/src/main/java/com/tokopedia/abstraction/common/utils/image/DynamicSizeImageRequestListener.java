package com.tokopedia.abstraction.common.utils.image;

import android.graphics.drawable.Drawable;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.Nullable;

import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.DrawableImageViewTarget;
import com.bumptech.glide.request.target.Target;

/**
 * Created by Hendri on 15/03/18.
 */

public class DynamicSizeImageRequestListener implements RequestListener<Drawable> {

    @Override
    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
        return false;
    }

    @Override
    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
        if (target instanceof DrawableImageViewTarget) {
            ImageView targetImageView = ((DrawableImageViewTarget) target).getView();
            targetImageView.layout(0,0,0,0);
            int width = resource.getIntrinsicWidth();
            int height = resource.getIntrinsicHeight();
            if (width >= height) {
                if (targetImageView.getLayoutParams().width != ViewGroup.LayoutParams.MATCH_PARENT) {
                    targetImageView.getLayoutParams().width = targetImageView.getMaxWidth();
                    if(targetImageView.getMaxWidth() == 0 || targetImageView.getMaxWidth() ==
                            Integer.MAX_VALUE){
                        targetImageView.getLayoutParams().width = ViewGroup.LayoutParams.WRAP_CONTENT;
                    }
                }
                targetImageView.getLayoutParams().height = ViewGroup.LayoutParams.WRAP_CONTENT;
            } else {
                targetImageView.getLayoutParams().width = ViewGroup.LayoutParams.WRAP_CONTENT;
                if(targetImageView.getLayoutParams().height != ViewGroup.LayoutParams.MATCH_PARENT) {
                    targetImageView.getLayoutParams().height = targetImageView.getMaxHeight();
                    if(targetImageView.getMaxHeight() == 0 || targetImageView.getMaxHeight() ==
                            Integer.MAX_VALUE){
                        targetImageView.getLayoutParams().height = ViewGroup.LayoutParams.WRAP_CONTENT;
                    }
                }
            }
            targetImageView.requestLayout();
        }
        return false;
    }

}
