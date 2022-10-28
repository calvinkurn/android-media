package com.tokopedia.feed_shop.shop.view.model

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.feed_shop.shop.view.adapter.factory.FeedShopTypeFactory

class EmptyFeedShopSellerMigrationUiModel : Visitable<FeedShopTypeFactory> {
    override fun type(typeFactory: FeedShopTypeFactory): Int {
        return typeFactory.type(this)
    }
}