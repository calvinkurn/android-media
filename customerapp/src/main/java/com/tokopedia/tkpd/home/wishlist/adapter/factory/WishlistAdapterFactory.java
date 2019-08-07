package com.tokopedia.tkpd.home.wishlist.adapter.factory;

import android.view.View;

import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory;
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.tkpd.home.adapter.OnWishlistActionButtonClicked;
import com.tokopedia.tkpd.home.presenter.WishListView;
import com.tokopedia.tkpd.home.wishlist.adapter.viewholder.WishlistEmptySearchViewHolder;
import com.tokopedia.tkpd.home.wishlist.adapter.viewholder.WishlistEmptyViewHolder;
import com.tokopedia.tkpd.home.wishlist.adapter.viewholder.WishlistProductListViewHolder;
import com.tokopedia.tkpd.home.wishlist.adapter.viewholder.WishlistRecomTitleViewHolder;
import com.tokopedia.tkpd.home.wishlist.adapter.viewholder.WishlistRecomendationViewHolder;
import com.tokopedia.tkpd.home.wishlist.adapter.viewholder.WishlistTopAdsListViewHolder;
import com.tokopedia.tkpd.home.wishlist.adapter.viewmodel.WishlistEmptySearchViewModel;
import com.tokopedia.tkpd.home.wishlist.adapter.viewmodel.WishlistEmptyViewModel;
import com.tokopedia.tkpd.home.wishlist.adapter.viewmodel.WishlistProductViewModel;
import com.tokopedia.tkpd.home.wishlist.adapter.viewmodel.WishlistRecomTitleViewModel;
import com.tokopedia.tkpd.home.wishlist.adapter.viewmodel.WishlistRecomendationViewModel;
import com.tokopedia.tkpd.home.wishlist.adapter.viewmodel.WishlistTopAdsViewModel;
import com.tokopedia.tkpd.home.wishlist.analytics.WishlistAnalytics;

import org.jetbrains.annotations.NotNull;

/**
 * Author errysuprayogi on 25,July,2019
 */
public class WishlistAdapterFactory extends BaseAdapterTypeFactory implements WishlistTypeFactory {

    private OnWishlistActionButtonClicked actionButtonClicked;
    private WishListView wishListView;
    private WishlistAnalytics wishlistAnalytics;

    public WishlistAdapterFactory(OnWishlistActionButtonClicked actionButtonClicked,
                                  WishListView wishListView,
                                  WishlistAnalytics wishlistAnalytics) {
        this.actionButtonClicked = actionButtonClicked;
        this.wishListView = wishListView;
        this.wishlistAnalytics = wishlistAnalytics;
    }

    @Override
    public int type(@NotNull WishlistProductViewModel viewModel) {
        return WishlistProductListViewHolder.LAYOUT;
    }

    @Override
    public int type(@NotNull WishlistEmptyViewModel viewModel) {
        return WishlistEmptyViewHolder.LAYOUT;
    }

    @Override
    public int type(@NotNull WishlistEmptySearchViewModel viewModel) {
        return WishlistEmptySearchViewHolder.LAYOUT;
    }

    @Override
    public int type(@NotNull WishlistTopAdsViewModel viewModel) {
        return WishlistTopAdsListViewHolder.LAYOUT;
    }

    @Override
    public int type(@NotNull WishlistRecomendationViewModel viewModel) {
        return WishlistRecomendationViewHolder.LAYOUT;
    }

    @Override
    public int type(@NotNull WishlistRecomTitleViewModel viewModel) {
        return WishlistRecomTitleViewHolder.LAYOUT;
    }

    @Override
    public AbstractViewHolder createViewHolder(View parent, int type) {
        if(type == WishlistEmptySearchViewHolder.LAYOUT) {
            return new WishlistEmptySearchViewHolder(parent, actionButtonClicked);
        } else if (type == WishlistProductListViewHolder.LAYOUT){
            return new WishlistProductListViewHolder(parent, wishlistAnalytics, wishListView);
        } else if (type == WishlistEmptyViewHolder.LAYOUT){
            return new WishlistEmptyViewHolder(parent, actionButtonClicked);
        } else if (type == WishlistTopAdsListViewHolder.LAYOUT){
            return new WishlistTopAdsListViewHolder(parent);
        } else if (type == WishlistRecomendationViewHolder.LAYOUT){
            return new WishlistRecomendationViewHolder(parent, wishlistAnalytics);
        } else if (type == WishlistRecomTitleViewHolder.LAYOUT){
            return new WishlistRecomTitleViewHolder(parent);
        }
        return super.createViewHolder(parent, type);
    }
}
