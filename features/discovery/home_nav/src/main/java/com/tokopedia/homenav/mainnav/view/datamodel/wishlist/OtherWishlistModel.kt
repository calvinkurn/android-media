package com.tokopedia.homenav.mainnav.view.datamodel.wishlist

import com.tokopedia.homenav.mainnav.view.adapter.typefactory.WishlistTypeFactory
import com.tokopedia.topads.sdk.domain.model.ImpressHolder

/**
 * Created by Frenzel on 21/04/22.
 */

data class OtherWishlistModel(
        val otherWishlistItemsCount: Int
): WishlistNavVisitable, ImpressHolder() {
    override fun type(factory: WishlistTypeFactory): Int {
        return factory.type(this)
    }
}