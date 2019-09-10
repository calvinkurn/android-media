package com.tokopedia.promocheckout.widget;

import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.Target;

public class ImageUtil {
    public static void dimImage(ImageView imageView) {
        ColorMatrix matrix = new ColorMatrix();
        matrix.setSaturation(0);
        ColorMatrixColorFilter filter = new ColorMatrixColorFilter(matrix);
        imageView.setColorFilter(filter);
    }

    public static void unDimImage(ImageView imageView) {
        ColorMatrix matrix = new ColorMatrix();
        matrix.setSaturation(1);
        ColorMatrixColorFilter filter = new ColorMatrixColorFilter(matrix);
        imageView.setColorFilter(filter);
    }

    public static void loadImage(ImageView imageView, String url) {
        try {
            Glide.with(imageView.getContext())
                    .load(url)
                    .dontAnimate()
                    .placeholder(com.tokopedia.design.R.drawable.ic_loading_image)
                    .error(com.tokopedia.design.R.drawable.ic_loading_image)
                    .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                    .into(imageView);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
