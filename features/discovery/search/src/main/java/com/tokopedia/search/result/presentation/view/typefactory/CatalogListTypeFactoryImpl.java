package com.tokopedia.search.result.presentation.view.typefactory;

import android.view.View;

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.discovery.common.constants.SearchConstant;
import com.tokopedia.search.result.presentation.model.CatalogHeaderViewModel;
import com.tokopedia.search.result.presentation.model.CatalogViewModel;
import com.tokopedia.search.result.presentation.model.EmptySearchViewModel;
import com.tokopedia.search.result.presentation.view.adapter.viewholder.catalog.BigGridCatalogViewHolder;
import com.tokopedia.search.result.presentation.view.adapter.viewholder.catalog.CatalogHeaderViewHolder;
import com.tokopedia.search.result.presentation.view.adapter.viewholder.catalog.GridCatalogViewHolder;
import com.tokopedia.search.result.presentation.view.adapter.viewholder.catalog.ListCatalogViewHolder;
import com.tokopedia.search.result.presentation.view.adapter.viewholder.product.EmptySearchViewHolder;
import com.tokopedia.search.result.presentation.view.listener.BannerAdsListener;
import com.tokopedia.search.result.presentation.view.listener.CatalogListener;
import com.tokopedia.search.result.presentation.view.listener.EmptyStateListener;
import com.tokopedia.topads.sdk.base.Config;

public class CatalogListTypeFactoryImpl extends SearchSectionTypeFactoryImpl implements CatalogListTypeFactory {

    private final CatalogListener catalogListener;
    private final EmptyStateListener emptyStateListener;
    private final BannerAdsListener bannerAdsListener;
    private final Config topAdsConfig;

    public CatalogListTypeFactoryImpl(CatalogListener listener, EmptyStateListener emptyStateListener, BannerAdsListener bannerAdsListener, Config topAdsConfig) {
        this.catalogListener = listener;
        this.emptyStateListener = emptyStateListener;
        this.bannerAdsListener = bannerAdsListener;
        this.topAdsConfig = topAdsConfig;
    }

    @Override
    public int type(CatalogHeaderViewModel headerViewModel) {
        return CatalogHeaderViewHolder.LAYOUT;
    }

    @Override
    public int type(CatalogViewModel viewModel) {
        switch (getRecyclerViewItem()) {
            case SearchConstant.RecyclerView.VIEW_PRODUCT:
                return ListCatalogViewHolder.LAYOUT;
            case SearchConstant.RecyclerView.VIEW_PRODUCT_GRID_1:
                return BigGridCatalogViewHolder.LAYOUT;
            case SearchConstant.RecyclerView.VIEW_PRODUCT_GRID_2:
            default:
                return GridCatalogViewHolder.LAYOUT;
        }
    }

    @Override
    public int type(EmptySearchViewModel emptySearchModel) {
        return EmptySearchViewHolder.LAYOUT;
    }

    @Override
    public AbstractViewHolder createViewHolder(View parent, int type) {
        AbstractViewHolder viewHolder;
        if (type == ListCatalogViewHolder.LAYOUT) {
            viewHolder = new ListCatalogViewHolder(parent, catalogListener);
        } else if (type == GridCatalogViewHolder.LAYOUT) {
            viewHolder = new GridCatalogViewHolder(parent, catalogListener);
        } else if (type == BigGridCatalogViewHolder.LAYOUT) {
            viewHolder = new BigGridCatalogViewHolder(parent, catalogListener);
        } else if (type == EmptySearchViewHolder.LAYOUT) {
            viewHolder = new EmptySearchViewHolder(parent, emptyStateListener, bannerAdsListener, null);
        } else if (type == CatalogHeaderViewHolder.LAYOUT) {
            viewHolder = new CatalogHeaderViewHolder(parent, catalogListener, bannerAdsListener, topAdsConfig);
        } else {
            viewHolder = super.createViewHolder(parent, type);
        }
        return viewHolder;
    }
}
