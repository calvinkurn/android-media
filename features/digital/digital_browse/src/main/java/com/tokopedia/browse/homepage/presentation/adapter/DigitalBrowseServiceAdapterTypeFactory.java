package com.tokopedia.browse.homepage.presentation.adapter;

import android.view.View;

import com.tokopedia.abstraction.base.view.adapter.factory.AdapterTypeFactory;
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory;
import com.tokopedia.abstraction.base.view.adapter.model.LoadingModel;
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.browse.homepage.presentation.adapter.viewholder.DigitalBrowseServiceShimmeringViewHolder;
import com.tokopedia.browse.homepage.presentation.adapter.viewholder.DigitalBrowseServiceViewHolder;
import com.tokopedia.browse.homepage.presentation.model.DigitalBrowseServiceCategoryViewModel;

/**
 * @author by furqan on 04/09/18.
 */

public class DigitalBrowseServiceAdapterTypeFactory extends BaseAdapterTypeFactory implements AdapterTypeFactory {

    private DigitalBrowseServiceViewHolder.CategoryListener categoryListener;

    public DigitalBrowseServiceAdapterTypeFactory(DigitalBrowseServiceViewHolder.CategoryListener categoryListener) {
        this.categoryListener = categoryListener;
    }

    @Override
    public AbstractViewHolder createViewHolder(View parent, int type) {
        if (type == DigitalBrowseServiceShimmeringViewHolder.LAYOUT) {
            return new DigitalBrowseServiceShimmeringViewHolder(parent);
        } else if (type == DigitalBrowseServiceViewHolder.LAYOUT) {
            return new DigitalBrowseServiceViewHolder(parent, categoryListener);
        } else {
            return super.createViewHolder(parent, type);
        }
    }

    @Override
    public int type(LoadingModel viewModel) {
        return DigitalBrowseServiceShimmeringViewHolder.LAYOUT;
    }

    public int type(DigitalBrowseServiceCategoryViewModel viewModel) {
        return DigitalBrowseServiceViewHolder.LAYOUT;
    }
}
