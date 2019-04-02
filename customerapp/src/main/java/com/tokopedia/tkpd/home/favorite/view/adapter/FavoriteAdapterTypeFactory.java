package com.tokopedia.tkpd.home.favorite.view.adapter;

import android.view.View;

import com.tokopedia.core.base.adapter.BaseAdapterTypeFactory;
import com.tokopedia.core.base.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.tkpd.home.favorite.view.adapter.viewholders.FavoriteShopViewHolder;
import com.tokopedia.tkpd.home.favorite.view.adapter.viewholders.TopAdsShopViewHolder;
import com.tokopedia.tkpd.home.favorite.view.viewlistener.FavoriteClickListener;
import com.tokopedia.tkpd.home.favorite.view.viewmodel.FavoriteShopViewModel;
import com.tokopedia.tkpd.home.favorite.view.viewmodel.TopAdsShopViewModel;

/**
 * @author kulomady on 1/24/17.
 */

public class FavoriteAdapterTypeFactory
        extends BaseAdapterTypeFactory implements FavoriteTypeFactory {

    private FavoriteClickListener favoriteClickListener;

    public FavoriteAdapterTypeFactory(FavoriteClickListener favoriteClickListener) {
        this.favoriteClickListener = favoriteClickListener;
    }

    @Override
    public int type(TopAdsShopViewModel viewModel) {
        return TopAdsShopViewHolder.LAYOUT;
    }


    @Override
    public int type(FavoriteShopViewModel viewModel) {
        return FavoriteShopViewHolder.LAYOUT;
    }


    @Override
    public AbstractViewHolder createViewHolder(View parent, int type) {
        AbstractViewHolder creatViewHolder;
        if (type == TopAdsShopViewHolder.LAYOUT) {
            creatViewHolder = new TopAdsShopViewHolder(parent, favoriteClickListener);
        } else if (type == FavoriteShopViewHolder.LAYOUT) {
            creatViewHolder = new FavoriteShopViewHolder(parent);
        } else {
            creatViewHolder = super.createViewHolder(parent, type);
        }

        return creatViewHolder;
    }
}
