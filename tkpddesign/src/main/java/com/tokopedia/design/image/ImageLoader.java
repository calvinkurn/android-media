package com.tokopedia.design.image;

import android.widget.ImageView;

import com.bumptech.glide.Glide;

/**
 * Created by pranaymohapatra on 03/09/18.
 */

public class ImageLoader {
    public static void LoadImage(ImageView imageview, String url) {
        if (imageview.getContext() != null) {
            Glide.with(imageview.getContext())
                    .load(url)
                    .fitCenter()
                    .dontAnimate()
                    .placeholder(R.drawable.loading_page)
                    .error(R.drawable.error_drawable)
                    .into(imageview);
        }
    }
}
