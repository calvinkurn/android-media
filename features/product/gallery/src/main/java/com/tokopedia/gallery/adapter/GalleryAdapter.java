package com.tokopedia.gallery.adapter;

import android.support.v4.app.FragmentManager;
import android.view.View;

import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory;
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.gallery.GalleryView;
import com.tokopedia.gallery.adapter.viewholder.GalleryItemViewHolder;
import com.tokopedia.gallery.viewmodel.ImageReviewItem;

/**
 * Created by errysuprayogi on 1/26/18.
 */

public class GalleryAdapter extends BaseAdapterTypeFactory implements TypeFactory {

    private final GalleryView listener;

    public GalleryAdapter(GalleryView listener) {
        this.listener = listener;
    }

    @Override
    public int type(ImageReviewItem viewModel) {
        return GalleryItemViewHolder.LAYOUT;
    }

    @Override
    public AbstractViewHolder createViewHolder(View parent, int type) {
        if (type == GalleryItemViewHolder.LAYOUT) {
            return new GalleryItemViewHolder(parent, listener);
        } else {
            return super.createViewHolder(parent, type);
        }
    }
}
