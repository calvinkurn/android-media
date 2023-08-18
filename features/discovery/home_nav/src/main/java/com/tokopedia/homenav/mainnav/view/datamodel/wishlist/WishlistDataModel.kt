package com.tokopedia.homenav.mainnav.view.datamodel.wishlist

import com.tokopedia.homenav.MePage
import com.tokopedia.homenav.mainnav.domain.model.NavWishlistModel
import com.tokopedia.homenav.mainnav.view.adapter.typefactory.MainNavTypeFactory
import com.tokopedia.homenav.mainnav.view.datamodel.MainNavVisitable
import com.tokopedia.topads.sdk.domain.model.ImpressHolder

/**
 * Created by Frenzel on 18/04/22
 */
@MePage(MePage.Widget.WISHLIST)
data class WishlistDataModel(
    val showViewAll: Boolean = false,
    val collections: List<NavWishlistModel>,
    val isEmptyState: Boolean = false
): MainNavVisitable, ImpressHolder() {
    override fun id(): Any = "wishlist"

    override fun isContentTheSame(visitable: MainNavVisitable): Boolean =
            visitable is WishlistDataModel &&
                    visitable.collections == collections

    override fun type(factory: MainNavTypeFactory): Int {
        return factory.type(this)
    }
}
