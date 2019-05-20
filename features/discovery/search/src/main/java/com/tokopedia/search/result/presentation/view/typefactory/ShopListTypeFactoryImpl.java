package com.tokopedia.search.result.presentation.view.typefactory;

import android.view.View;

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.discovery.common.constants.SearchConstant;
import com.tokopedia.search.result.presentation.model.EmptySearchViewModel;
import com.tokopedia.search.result.presentation.model.ShopViewModel;
import com.tokopedia.search.result.presentation.view.adapter.viewholder.product.EmptySearchViewHolder;
import com.tokopedia.search.result.presentation.view.adapter.viewholder.shop.GridShopItemViewHolder;
import com.tokopedia.search.result.presentation.view.adapter.viewholder.shop.ListShopItemViewHolder;
import com.tokopedia.search.result.presentation.view.listener.BannerAdsListener;
import com.tokopedia.search.result.presentation.view.listener.EmptyStateListener;
import com.tokopedia.search.result.presentation.view.listener.ShopListener;

public class ShopListTypeFactoryImpl extends SearchSectionTypeFactoryImpl implements ShopListTypeFactory {

    private ShopListener shopListener;
    private EmptyStateListener emptyStateListener;
    private BannerAdsListener bannerAdsListener;

    public ShopListTypeFactoryImpl(ShopListener shopListener, EmptyStateListener emptyStateListener, BannerAdsListener bannerAdsListener) {
        this.shopListener = shopListener;
        this.emptyStateListener = emptyStateListener;
        this.bannerAdsListener = bannerAdsListener;
    }

    @Override
    public int type(ShopViewModel.ShopViewItem shopItem) {
        switch (getRecyclerViewItem()) {
            case SearchConstant.RecyclerView.VIEW_PRODUCT:
                return ListShopItemViewHolder.LAYOUT;
            case SearchConstant.RecyclerView.VIEW_PRODUCT_GRID_1:
            case SearchConstant.RecyclerView.VIEW_PRODUCT_GRID_2:
            default:
                return GridShopItemViewHolder.LAYOUT;
        }
    }

    @Override
    public int type(EmptySearchViewModel emptySearchModel) {
        return EmptySearchViewHolder.LAYOUT;
    }

    @Override
    public AbstractViewHolder createViewHolder(View view, int type) {

        AbstractViewHolder viewHolder;

        if (type == ListShopItemViewHolder.LAYOUT) {
            viewHolder = new ListShopItemViewHolder(view, shopListener);
        } else if (type == GridShopItemViewHolder.LAYOUT) {
            viewHolder = new GridShopItemViewHolder(view, shopListener);
        } else if (type == EmptySearchViewHolder.LAYOUT) {
            viewHolder = new EmptySearchViewHolder(view, emptyStateListener, bannerAdsListener, null);
        } else {
            viewHolder = super.createViewHolder(view, type);
        }
        return viewHolder;
    }
}