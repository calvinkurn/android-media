package com.tokopedia.design.image;

import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.tokopedia.design.R;

/**
 * Created by pranaymohapatra on 03/09/18.
 */
@Deprecated
public class ImageLoader {
    public static void LoadImage(ImageView imageview, String url) {
        if (imageview.getContext() != null) {
            Glide.with(imageview.getContext())
                    .load(url)
                    .fitCenter()
                    .dontAnimate()
                    .placeholder(com.tokopedia.design.R.drawable.ic_loading_image)
                    .error(R.drawable.error_drawable)
                    .into(imageview);
        }
    }
}
