package com.tokopedia.homenav.mainnav.view.datamodel.favoriteshoplist

import com.tokopedia.homenav.mainnav.view.adapter.typefactory.FavoriteShopListTypeFactory
import com.tokopedia.topads.sdk.domain.model.ImpressHolder

data class OtherFavoriteShopModel(
        val otherShopsCount: Int
): FavoriteShopNavVisitable, ImpressHolder() {
    override fun type(factory: FavoriteShopListTypeFactory): Int {
        return factory.type(this)
    }
}