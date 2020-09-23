package com.tokopedia.shop.feed.view.model

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.shop.feed.view.adapter.factory.FeedShopTypeFactory

class EmptyFeedShopSellerMigrationUiModel : Visitable<FeedShopTypeFactory> {
    override fun type(typeFactory: FeedShopTypeFactory): Int {
        return typeFactory.type(this)
    }
}