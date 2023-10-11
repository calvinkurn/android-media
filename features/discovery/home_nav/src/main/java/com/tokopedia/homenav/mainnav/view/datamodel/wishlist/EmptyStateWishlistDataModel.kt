package com.tokopedia.homenav.mainnav.view.datamodel.wishlist

import com.tokopedia.homenav.MePage
import com.tokopedia.homenav.mainnav.view.adapter.typefactory.WishlistTypeFactory
import com.tokopedia.topads.sdk.domain.model.ImpressHolder

@MePage(MePage.Widget.WISHLIST)
class EmptyStateWishlistDataModel: WishlistNavVisitable, ImpressHolder() {
    override fun type(factory: WishlistTypeFactory): Int {
        return factory.type(this)
    }
}
