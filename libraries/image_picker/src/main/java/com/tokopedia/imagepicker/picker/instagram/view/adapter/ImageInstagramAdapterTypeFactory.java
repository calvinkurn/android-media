package com.tokopedia.imagepicker.picker.instagram.view.adapter;

import android.view.View;

import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory;
import com.tokopedia.abstraction.base.view.adapter.model.LoadingModel;
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.imagepicker.picker.instagram.view.holder.LoadingShimmeringGrid3ViewHolder;
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
        return LoadingShimmeringGrid3ViewHolder.LAYOUT;
    }

    @Override
    public AbstractViewHolder createViewHolder(View parent, int type) {
        if (type == ImagePickerInstagramViewHolder.LAYOUT) {
            return new ImagePickerInstagramViewHolder(parent, imageResize);
        } else if (type == LoadingShimmeringGrid3ViewHolder.LAYOUT) {
            return new LoadingShimmeringGrid3ViewHolder(parent);
        } else if (type == InstagramErrorLoginViewHolder.LAYOUT){
            return new InstagramErrorLoginViewHolder(parent);
        }
        return super.createViewHolder(parent, type);
    }
}
