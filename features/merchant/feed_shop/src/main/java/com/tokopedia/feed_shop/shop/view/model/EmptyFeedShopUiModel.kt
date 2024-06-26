package com.tokopedia.feed_shop.shop.view.model

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.feed_shop.shop.view.adapter.factory.FeedShopTypeFactory

/**
 * @author by yfsx on 17/05/19.
 */
data class EmptyFeedShopUiModel(
        var emptyTitle: String = "",
        var emptySubTitle: String = "",
        var buttonString: String = ""
) : Visitable<FeedShopTypeFactory> {
    override fun type(typeFactory: FeedShopTypeFactory?): Int {
        return typeFactory!!.type(this)
    }
}