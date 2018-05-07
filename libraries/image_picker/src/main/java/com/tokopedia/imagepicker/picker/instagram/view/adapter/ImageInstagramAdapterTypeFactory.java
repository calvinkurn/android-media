package com.tokopedia.imagepicker.picker.instagram.view.adapter;

import android.view.View;

import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory;
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.imagepicker.picker.instagram.view.model.InstagramMediaModel;

/**
 * Created by zulfikarrahman on 5/3/18.
 */

public class ImageInstagramAdapterTypeFactory extends BaseAdapterTypeFactory {

    public int type(InstagramMediaModel instagramMediaModel) {
        return ImagePickerInstagramViewHolder.LAYOUT;
    }

    @Override
    public AbstractViewHolder createViewHolder(View parent, int type) {
        if(type == ImagePickerInstagramViewHolder.LAYOUT){
            return new ImagePickerInstagramViewHolder(parent);
        }
        return super.createViewHolder(parent, type);
    }
}
