package com.tokopedia.homenav.mainnav.view.datamodel.wishlist

import com.tokopedia.homenav.mainnav.view.adapter.typefactory.MainNavTypeFactory
import com.tokopedia.homenav.mainnav.view.adapter.typefactory.WishlistTypeFactory
import com.tokopedia.homenav.mainnav.view.datamodel.MainNavVisitable
import com.tokopedia.topads.sdk.domain.model.ImpressHolder

class EmptyStateWishlistDataModel: WishlistNavVisitable, ImpressHolder() {
    override fun type(factory: WishlistTypeFactory): Int {
        return factory.type(this)
    }
}