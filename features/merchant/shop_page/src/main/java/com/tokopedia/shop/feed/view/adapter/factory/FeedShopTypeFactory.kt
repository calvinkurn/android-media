package com.tokopedia.shop.feed.view.adapter.factory

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.shop.feed.view.model.EmptyFeedShopSellerMigrationUiModel
import com.tokopedia.shop.feed.view.model.EmptyFeedShopViewModel
import com.tokopedia.shop.feed.view.model.WhitelistUiModel

/**
 * @author by yfsx on 16/05/19.
 */
interface FeedShopTypeFactory {

    fun type(whitelistUiModel: WhitelistUiModel): Int

    fun type(emptyFeedShopViewModel: EmptyFeedShopViewModel): Int

    fun type(emptyFeedShopSellerMigrationUiModel: EmptyFeedShopSellerMigrationUiModel): Int

    fun createViewHolder(view: View, viewType: Int): AbstractViewHolder<*>
}