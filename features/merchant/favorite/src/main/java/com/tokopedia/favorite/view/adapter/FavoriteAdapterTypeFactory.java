package com.tokopedia.favorite.view.adapter;

import android.view.View;

import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory;
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.favorite.view.adapter.viewholders.FavoriteShopViewHolder;
import com.tokopedia.favorite.view.adapter.viewholders.TopAdsShopViewHolder;
import com.tokopedia.favorite.view.viewlistener.FavoriteClickListener;
import com.tokopedia.favorite.view.viewmodel.FavoriteShopUiModel;
import com.tokopedia.favorite.view.viewmodel.TopAdsShopViewModel;

/**
 * @author kulomady on 1/24/17.
 */

public class FavoriteAdapterTypeFactory
        extends BaseAdapterTypeFactory implements FavoriteTypeFactory {

    private FavoriteClickListener favoriteClickListener;
    private TopAdsShopAdapter.ImpressionImageLoadedListener impressionImageLoadedListener;

    public FavoriteAdapterTypeFactory(FavoriteClickListener favoriteClickListener,
                                      TopAdsShopAdapter.ImpressionImageLoadedListener impressionImageLoadedListener) {
        this.favoriteClickListener = favoriteClickListener;
        this.impressionImageLoadedListener = impressionImageLoadedListener;
    }

    @Override
    public int type(TopAdsShopViewModel viewModel) {
        return TopAdsShopViewHolder.LAYOUT;
    }


    @Override
    public int type(FavoriteShopUiModel viewModel) {
        return FavoriteShopViewHolder.LAYOUT;
    }


    @Override
    public AbstractViewHolder createViewHolder(View parent, int type) {
        AbstractViewHolder createViewHolder;
        if (type == TopAdsShopViewHolder.LAYOUT) {
            createViewHolder = new TopAdsShopViewHolder(
                    parent, favoriteClickListener, impressionImageLoadedListener);
        } else if (type == FavoriteShopViewHolder.LAYOUT) {
            createViewHolder = new FavoriteShopViewHolder(parent);
        } else {
            createViewHolder = super.createViewHolder(parent, type);
        }

        return createViewHolder;
    }
}
