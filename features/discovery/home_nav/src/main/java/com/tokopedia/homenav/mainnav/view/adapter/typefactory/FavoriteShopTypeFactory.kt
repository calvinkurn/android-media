package com.tokopedia.homenav.mainnav.view.adapter.typefactory

import com.tokopedia.homenav.mainnav.view.datamodel.favoriteshop.EmptyStateFavoriteShopDataModel
import com.tokopedia.homenav.mainnav.view.datamodel.favoriteshop.OtherFavoriteShopModel
import com.tokopedia.homenav.mainnav.view.datamodel.favoriteshop.FavoriteShopModel

interface FavoriteShopTypeFactory {
    fun type(favoriteShopModel: FavoriteShopModel): Int
    fun type(otherFavoriteShopModel: OtherFavoriteShopModel): Int
    fun type(emptyStateFavoriteShopDataModel: EmptyStateFavoriteShopDataModel): Int
}