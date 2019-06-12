package com.tokopedia.shop.feed.view.adapter.factory

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.shop.feed.view.model.EmptyFeedShopViewModel
import com.tokopedia.shop.feed.view.model.WhitelistViewModel

/**
 * @author by yfsx on 16/05/19.
 */
interface FeedShopTypeFactory {

    fun type(whitelistViewModel: WhitelistViewModel): Int

    fun type(emptyFeedShopViewModel: EmptyFeedShopViewModel): Int

    fun createViewHolder(view: View, viewType: Int): AbstractViewHolder<*>
}