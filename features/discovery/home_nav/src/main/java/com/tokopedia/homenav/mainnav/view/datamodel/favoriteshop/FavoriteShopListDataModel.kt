package com.tokopedia.homenav.mainnav.view.datamodel.favoriteshop

import com.tokopedia.homenav.mainnav.domain.model.NavFavoriteShopModel
import com.tokopedia.homenav.mainnav.view.adapter.typefactory.MainNavTypeFactory
import com.tokopedia.homenav.mainnav.view.datamodel.MainNavVisitable
import com.tokopedia.topads.sdk.domain.model.ImpressHolder

/**
 * Created by Frenzel on 18/04/22
 */
data class FavoriteShopListDataModel(
    val showViewAll: Boolean = false,
    val favoriteShops: List<NavFavoriteShopModel>
): MainNavVisitable, ImpressHolder() {
    override fun id(): Any = "favoriteShopList"

    override fun isContentTheSame(visitable: MainNavVisitable): Boolean =
            visitable is FavoriteShopListDataModel &&
                    visitable.favoriteShops == favoriteShops

    override fun type(factory: MainNavTypeFactory): Int {
        return factory.type(this)
    }
}