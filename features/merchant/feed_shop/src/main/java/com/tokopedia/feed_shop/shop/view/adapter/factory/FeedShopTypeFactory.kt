package com.tokopedia.feed_shop.shop.view.adapter.factory

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.feed_shop.shop.view.model.EmptyFeedShopSellerMigrationUiModel
import com.tokopedia.feed_shop.shop.view.model.EmptyFeedShopUiModel
import com.tokopedia.feed_shop.shop.view.model.WhitelistUiModel

/**
 * @author by yfsx on 16/05/19.
 */
interface FeedShopTypeFactory {

    fun type(whitelistUiModel: WhitelistUiModel): Int

    fun type(emptyFeedShopUiModel: EmptyFeedShopUiModel): Int

    fun type(emptyFeedShopSellerMigrationUiModel: EmptyFeedShopSellerMigrationUiModel): Int

    fun createViewHolder(view: View, viewType: Int): AbstractViewHolder<*>
}