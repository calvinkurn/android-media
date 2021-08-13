package com.tokopedia.shop.feed.view.model

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.shop.feed.domain.WhitelistDomain
import com.tokopedia.shop.feed.view.adapter.factory.FeedShopTypeFactory

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