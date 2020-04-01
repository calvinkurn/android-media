package com.tokopedia.favorite.view.viewlistener;

import android.view.View;

import com.tokopedia.favorite.view.viewmodel.TopAdsShopItem;


/**
 * Created by erry on 01/02/17.
 */
public interface FavoriteClickListener {

    void onFavoriteShopClicked(View view, TopAdsShopItem shopItem);

}