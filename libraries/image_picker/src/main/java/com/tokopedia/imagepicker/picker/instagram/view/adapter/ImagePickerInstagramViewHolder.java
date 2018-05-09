package com.tokopedia.imagepicker.picker.instagram.view.adapter;

import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.abstraction.common.utils.image.ImageHandler;
import com.tokopedia.imagepicker.R;
import com.tokopedia.imagepicker.picker.instagram.view.model.InstagramMediaModel;

/**
 * Created by zulfikarrahman on 5/3/18.
 */

public class ImagePickerInstagramViewHolder extends AbstractViewHolder<InstagramMediaModel> {
    public static final int LAYOUT = R.layout.item_media_instagram;

    private ImageView imageInstagram;
    private int imageResize;

    public ImagePickerInstagramViewHolder(View itemView, int imageResize) {
        super(itemView);
        imageInstagram = itemView.findViewById(R.id.image_instagram);
        this.imageResize = imageResize;
    }

    @Override
    public void bind(InstagramMediaModel element) {
        ImageHandler.LoadImageResize(imageInstagram.getContext(), imageInstagram, element.getThumbnail(),
                imageResize, imageResize);
    }
}
