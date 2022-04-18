package com.tokopedia.homenav.mainnav.view.datamodel.favoriteshoplist

import com.tokopedia.homenav.mainnav.domain.model.NavFavoriteShopModel
import com.tokopedia.homenav.mainnav.view.adapter.typefactory.FavoriteShopListTypeFactory
import com.tokopedia.kotlin.model.ImpressHolder

data class FavoriteShopModel(
        val navFavoriteShopModel: NavFavoriteShopModel
): FavoriteShopNavVisitable, ImpressHolder() {

    override fun type(factory: FavoriteShopListTypeFactory): Int {
        return factory.type(this)
    }
}