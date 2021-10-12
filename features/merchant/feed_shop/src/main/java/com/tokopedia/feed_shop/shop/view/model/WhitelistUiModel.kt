package com.tokopedia.feed_shop.shop.view.model

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.feed_shop.shop.domain.WhitelistDomain
import com.tokopedia.feed_shop.shop.view.adapter.factory.FeedShopTypeFactory

/**
 * @author by yfsx on 16/05/19.
 */
data  class WhitelistUiModel(
        var whitelist: WhitelistDomain = WhitelistDomain()
) : Visitable<FeedShopTypeFactory> {
    override fun type(typeFactory: FeedShopTypeFactory?): Int {
        return typeFactory!!.type(this)
    }
}