package com.tokopedia.favorite.view.viewlistener

import android.view.View
import com.tokopedia.favorite.view.viewmodel.TopAdsShopItem

/**
 * Created by erry on 01/02/17.
 */
interface FavoriteClickListener {

    fun onFavoriteShopClicked(view: View?, shopItem: TopAdsShopItem?)

}
