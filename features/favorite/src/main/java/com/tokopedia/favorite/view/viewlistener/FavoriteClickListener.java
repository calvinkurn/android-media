package com.tokopedia.home.account.favorite.view.viewlistener;

import android.view.View;

import com.tokopedia.home.account.favorite.view.viewmodel.TopAdsShopItem;


/**
 * Created by erry on 01/02/17.
 */
public interface FavoriteClickListener {

    void onFavoriteShopClicked(View view, TopAdsShopItem shopItem);

}