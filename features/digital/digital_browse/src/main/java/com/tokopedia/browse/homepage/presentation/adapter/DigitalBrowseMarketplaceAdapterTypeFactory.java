package com.tokopedia.browse.homepage.presentation.adapter;

import android.view.View;

import com.tokopedia.abstraction.base.view.adapter.factory.AdapterTypeFactory;
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory;
import com.tokopedia.abstraction.base.view.adapter.model.LoadingModel;
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.browse.homepage.presentation.adapter.viewholder.DigitalBrowseMarketplaceShimmeringViewHolder;
import com.tokopedia.browse.homepage.presentation.model.DigitalBrowseMarketplaceViewModel;

/**
 * @author by furqan on 03/09/18.
 */

public class DigitalBrowseMarketplaceAdapterTypeFactory extends BaseAdapterTypeFactory implements AdapterTypeFactory {

    public DigitalBrowseMarketplaceAdapterTypeFactory() {
    }

    @Override
    public AbstractViewHolder createViewHolder(View parent, int type) {
        if (type == DigitalBrowseMarketplaceShimmeringViewHolder.LAYOUT) {
            return new DigitalBrowseMarketplaceShimmeringViewHolder(parent);
        } else {
            return super.createViewHolder(parent, type);
        }
    }

    @Override
    public int type(LoadingModel viewModel) {
        return DigitalBrowseMarketplaceShimmeringViewHolder.LAYOUT;
    }

    public int type(DigitalBrowseMarketplaceViewModel viewModel) {
        return 0;
    }
}
