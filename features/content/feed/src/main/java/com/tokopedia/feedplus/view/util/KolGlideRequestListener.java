package com.tokopedia.feedplus.view.util;

import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;
import com.bumptech.glide.request.target.Target;

/**
 * @author by milhamj on 04/04/18.
 */

public class KolGlideRequestListener implements RequestListener<String, GlideDrawable> {
    @Override
    public boolean onException(Exception e, String model, Target<GlideDrawable> target,
                               boolean isFirstResource) {
        return false;
    }

    @Override
    public boolean onResourceReady(GlideDrawable resource, String model,
                                   Target<GlideDrawable> target,
                                   boolean isFromMemoryCache,
                                   boolean isFirstResource) {
        if (target instanceof GlideDrawableImageViewTarget) {
            ImageView targetImageView = ((GlideDrawableImageViewTarget) target).getView();
            targetImageView.layout(0,0,0,0);
            int width = resource.getIntrinsicWidth();
            int height = resource.getIntrinsicHeight();
            if (width >= height) {
                targetImageView.getLayoutParams().width = ViewGroup.LayoutParams.MATCH_PARENT;
                targetImageView.getLayoutParams().height = ViewGroup.LayoutParams.WRAP_CONTENT;
            } else {
                targetImageView.getLayoutParams().width = ViewGroup.LayoutParams.MATCH_PARENT;
                targetImageView.getLayoutParams().height = height;
            }
            targetImageView.requestLayout();
        }
        return false;
    }
}
