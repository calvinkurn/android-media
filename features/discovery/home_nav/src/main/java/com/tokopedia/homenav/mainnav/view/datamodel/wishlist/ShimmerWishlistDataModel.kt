package com.tokopedia.homenav.mainnav.view.datamodel.wishlist

import com.tokopedia.homenav.MePage
import com.tokopedia.homenav.mainnav.view.adapter.typefactory.MainNavTypeFactory
import com.tokopedia.homenav.mainnav.view.datamodel.MainNavVisitable
import com.tokopedia.kotlin.model.ImpressHolder

@MePage(MePage.Widget.WISHLIST)
data class ShimmerWishlistDataModel(
        val id: Int = 127
): MainNavVisitable, ImpressHolder() {
    override fun id(): Any = id

    override fun isContentTheSame(visitable: MainNavVisitable): Boolean {
        return visitable is ShimmerWishlistDataModel && id == visitable.id
    }

    override fun type(factory: MainNavTypeFactory): Int {
        return factory.type(this)
    }
}
