package com.tokopedia.tkpd.home.favorite.view.adapter.viewholders;

import android.support.annotation.LayoutRes;
import android.view.View;

import com.tokopedia.core.base.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.tkpd.R;
import com.tokopedia.tkpd.home.favorite.view.viewmodel.FavoriteShopViewModel;

/**
 * @author kulomady on 1/24/17.
 */

public class FavoriteShopViewHolder extends AbstractViewHolder<FavoriteShopViewModel> {

    @LayoutRes
    public static final int LAYOUT = R.layout.listview_manage_favorited_shop;

    public FavoriteShopViewHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void bind(FavoriteShopViewModel element) {

    }
}
