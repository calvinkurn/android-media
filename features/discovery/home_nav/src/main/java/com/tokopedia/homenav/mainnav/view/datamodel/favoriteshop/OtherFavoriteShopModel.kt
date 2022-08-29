package com.tokopedia.homenav.mainnav.view.datamodel.favoriteshop

import com.tokopedia.homenav.mainnav.view.adapter.typefactory.FavoriteShopTypeFactory
import com.tokopedia.topads.sdk.domain.model.ImpressHolder

/**
 * Created by Frenzel on 18/04/22.
 */

data class OtherFavoriteShopModel(
        val otherShopsCount: Int = 0
): FavoriteShopNavVisitable, ImpressHolder() {
    override fun type(factory: FavoriteShopTypeFactory): Int {
        return factory.type(this)
    }
}