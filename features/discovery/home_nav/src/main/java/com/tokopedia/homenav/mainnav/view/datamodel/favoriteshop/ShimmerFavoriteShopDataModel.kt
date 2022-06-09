package com.tokopedia.homenav.mainnav.view.datamodel.favoriteshop

import com.tokopedia.homenav.mainnav.view.adapter.typefactory.MainNavTypeFactory
import com.tokopedia.homenav.mainnav.view.datamodel.MainNavVisitable
import com.tokopedia.kotlin.model.ImpressHolder

data class ShimmerFavoriteShopDataModel(
        val id: Int = 126
): MainNavVisitable, ImpressHolder() {
    override fun id(): Any = id

    override fun isContentTheSame(visitable: MainNavVisitable): Boolean {
        return visitable is ShimmerFavoriteShopDataModel && id == visitable.id
    }

    override fun type(factory: MainNavTypeFactory): Int {
        return factory.type(this)
    }
}