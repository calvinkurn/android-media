package com.tokopedia.homenav.mainnav.view.datamodel.wishlist

import com.tokopedia.homenav.mainnav.domain.model.NavWishlistModel
import com.tokopedia.homenav.mainnav.view.adapter.typefactory.WishlistTypeFactory
import com.tokopedia.kotlin.model.ImpressHolder

/**
 * Created by Frenzel on 21/04/22.
 */

data class WishlistModel(
        val navWishlistModel: NavWishlistModel
): WishlistNavVisitable, ImpressHolder() {

    override fun type(factory: WishlistTypeFactory): Int {
        return factory.type(this)
    }
}