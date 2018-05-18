package com.tokopedia.imagepicker.picker.instagram.view.adapter;

import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter;
import com.tokopedia.imagepicker.picker.instagram.view.model.InstagramMediaModel;

/**
 * Created by hendry on 18/05/18.
 */

public class ImageInstagramAdapter extends BaseListAdapter<InstagramMediaModel, ImageInstagramAdapterTypeFactory> {
    public ImageInstagramAdapter(ImageInstagramAdapterTypeFactory baseListAdapterTypeFactory) {
        super(baseListAdapterTypeFactory);
    }

    public ImageInstagramAdapter(ImageInstagramAdapterTypeFactory baseListAdapterTypeFactory, OnAdapterInteractionListener<InstagramMediaModel> onAdapterInteractionListener) {
        super(baseListAdapterTypeFactory, onAdapterInteractionListener);
    }


}
