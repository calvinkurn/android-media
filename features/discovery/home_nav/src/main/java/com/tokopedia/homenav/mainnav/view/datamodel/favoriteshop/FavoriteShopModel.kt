package com.tokopedia.homenav.mainnav.view.datamodel.favoriteshop

import com.tokopedia.homenav.mainnav.domain.model.NavFavoriteShopModel
import com.tokopedia.homenav.mainnav.view.adapter.typefactory.FavoriteShopTypeFactory
import com.tokopedia.kotlin.model.ImpressHolder

/**
 * Created by Frenzel on 18/04/22.
 */

data class FavoriteShopModel(
        val navFavoriteShopModel: NavFavoriteShopModel
): FavoriteShopNavVisitable, ImpressHolder() {

    override fun type(factory: FavoriteShopTypeFactory): Int {
        return factory.type(this)
    }
}