package com.tokopedia.imagepicker.picker.instagram.view.adapter;

import android.view.View;

import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory;
import com.tokopedia.abstraction.base.view.adapter.model.LoadingModel;
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.abstraction.base.view.adapter.viewholders.LoadingShimmeringGridViewHolder;
import com.tokopedia.abstraction.base.view.adapter.viewholders.LoadingViewholder;
import com.tokopedia.imagepicker.picker.instagram.view.model.InstagramErrorLoginModel;
import com.tokopedia.imagepicker.picker.instagram.view.model.InstagramMediaModel;

/**
 * Created by zulfikarrahman on 5/3/18.
 */

public class ImageInstagramAdapterTypeFactory extends BaseAdapterTypeFactory {

    private int imageResize;

    public ImageInstagramAdapterTypeFactory(int imageResize) {
        this.imageResize = imageResize;
    }

    public int type(InstagramMediaModel instagramMediaModel) {
        return ImagePickerInstagramViewHolder.LAYOUT;
    }

    public int type(InstagramErrorLoginModel viewModel) {
        return InstagramErrorLoginViewHolder.LAYOUT;
    }

    @Override
    public int type(LoadingModel viewModel) {
        return LoadingViewholder.LAYOUT;
    }

    @Override
    public AbstractViewHolder createViewHolder(View parent, int type) {
        if (type == ImagePickerInstagramViewHolder.LAYOUT) {
            return new ImagePickerInstagramViewHolder(parent, imageResize);
        } else if (type == LoadingViewholder.LAYOUT) {
            return new LoadingViewholder(parent);
        } else if (type == InstagramErrorLoginViewHolder.LAYOUT){
            return new InstagramErrorLoginViewHolder(parent);
        }
        return super.createViewHolder(parent, type);
    }
}
