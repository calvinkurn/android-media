package com.tokopedia.homenav.mainnav.view.adapter.typefactory

import com.tokopedia.homenav.mainnav.view.datamodel.favoriteshoplist.OtherFavoriteShopModel
import com.tokopedia.homenav.mainnav.view.datamodel.favoriteshoplist.FavoriteShopModel

interface FavoriteShopListTypeFactory {
    fun type(favoriteShopModel: FavoriteShopModel): Int
    fun type(otherFavoriteShopModel: OtherFavoriteShopModel): Int
}