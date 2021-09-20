package com.tokopedia.favorite.view.viewmodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.favorite.view.adapter.FavoriteTypeFactory

/**
 * @author kulomady on 1/24/17.
 */
class TopAdsShopUiModel : Visitable<FavoriteTypeFactory> {

    var adsShopItems: List<TopAdsShopItem>? = null
        get() = if (field == null) {
            emptyList()
        } else field

    override fun type(typeFactory: FavoriteTypeFactory): Int {
        return typeFactory.type(this)
    }

}
