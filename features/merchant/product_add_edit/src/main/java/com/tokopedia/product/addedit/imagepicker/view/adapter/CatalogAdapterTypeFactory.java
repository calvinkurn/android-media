package com.tokopedia.product.addedit.imagepicker.view.adapter;

import android.view.View;

import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory;
import com.tokopedia.abstraction.base.view.adapter.model.LoadingModel;
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.imagepicker.picker.instagram.view.holder.LoadingShimmeringGrid3ViewHolder;
import com.tokopedia.product.addedit.imagepicker.view.model.CatalogModelView;

/**
 * Created by zulfikarrahman on 6/5/18.
 */

public class CatalogAdapterTypeFactory extends BaseAdapterTypeFactory {

    private int imageResize;

    public CatalogAdapterTypeFactory(int imageResize) {
        this.imageResize = imageResize;
    }

    public int type(CatalogModelView catalogModelView) {
        return CatalogImageViewHolder.LAYOUT;
    }

    @Override
    public int type(LoadingModel viewModel) {
        return LoadingShimmeringGrid3ViewHolder.LAYOUT;
    }

    @Override
    public AbstractViewHolder createViewHolder(View parent, int type) {
        if (type == CatalogImageViewHolder.LAYOUT) {
            return new CatalogImageViewHolder(parent, imageResize);
        } else if (type == LoadingShimmeringGrid3ViewHolder.LAYOUT) {
            return new LoadingShimmeringGrid3ViewHolder(parent);
        } else {
            return super.createViewHolder(parent, type);
        }
    }
}
