package com.tokopedia.browse.homepage.presentation.adapter;

import android.view.View;

import com.tokopedia.abstraction.base.view.adapter.factory.AdapterTypeFactory;
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory;
import com.tokopedia.abstraction.base.view.adapter.model.LoadingModel;
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.browse.homepage.presentation.adapter.viewholder.DigitalBrowseCategoryViewHolder;
import com.tokopedia.browse.homepage.presentation.adapter.viewholder.DigitalBrowseMarketplaceShimmeringViewHolder;
import com.tokopedia.browse.homepage.presentation.adapter.viewholder.DigitalBrowsePopularViewHolder;
import com.tokopedia.browse.homepage.presentation.model.DigitalBrowsePopularBrandsViewModel;
import com.tokopedia.browse.homepage.presentation.model.DigitalBrowseRowViewModel;

/**
 * @author by furqan on 03/09/18.
 */

public class DigitalBrowseMarketplaceAdapterTypeFactory extends BaseAdapterTypeFactory implements AdapterTypeFactory {

    private DigitalBrowsePopularViewHolder.PopularBrandListener popularBrandListener;
    private DigitalBrowseCategoryViewHolder.CategoryListener categoryListener;

    public DigitalBrowseMarketplaceAdapterTypeFactory(DigitalBrowsePopularViewHolder.PopularBrandListener popularBrandListener, DigitalBrowseCategoryViewHolder.CategoryListener categoryListener) {
        this.popularBrandListener = popularBrandListener;
        this.categoryListener = categoryListener;
    }

    @Override
    public AbstractViewHolder createViewHolder(View parent, int type) {
        if (type == DigitalBrowseMarketplaceShimmeringViewHolder.LAYOUT) {
            return new DigitalBrowseMarketplaceShimmeringViewHolder(parent);
        } else if (type == DigitalBrowseCategoryViewHolder.LAYOUT) {
            return new DigitalBrowseCategoryViewHolder(parent, categoryListener);
        } else if (type == DigitalBrowsePopularViewHolder.LAYOUT) {
            return new DigitalBrowsePopularViewHolder(parent, popularBrandListener);
        } else {
            return super.createViewHolder(parent, type);
        }
    }

    @Override
    public int type(LoadingModel viewModel) {
        return DigitalBrowseMarketplaceShimmeringViewHolder.LAYOUT;
    }

    public int type(DigitalBrowseRowViewModel viewModel) {
        return DigitalBrowseCategoryViewHolder.LAYOUT;
    }

    public int type(DigitalBrowsePopularBrandsViewModel viewModel) {
        return DigitalBrowsePopularViewHolder.LAYOUT;
    }
}
