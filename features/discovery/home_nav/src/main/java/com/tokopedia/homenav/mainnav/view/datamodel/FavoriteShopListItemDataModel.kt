package com.tokopedia.homenav.mainnav.view.datamodel

import com.tokopedia.homenav.mainnav.domain.model.NavFavoriteShopModel
import com.tokopedia.homenav.mainnav.domain.model.NavOrderListModel
import com.tokopedia.homenav.mainnav.view.adapter.typefactory.MainNavTypeFactory
import com.tokopedia.topads.sdk.domain.model.ImpressHolder

/**
 * Created by Frenzel on 18/04/22
 */
data class FavoriteShopListItemDataModel(
        val favoriteShopListModel: List<NavFavoriteShopModel>,
        val otherFavoriteShopsCount: Int = 0
): MainNavVisitable, ImpressHolder() {
    override fun id(): Any = "favoriteShopList"

    override fun isContentTheSame(visitable: MainNavVisitable): Boolean =
            visitable is FavoriteShopListItemDataModel &&
                    visitable.favoriteShopListModel == favoriteShopListModel

    override fun type(factory: MainNavTypeFactory): Int {
        return factory.type(this)
    }
}