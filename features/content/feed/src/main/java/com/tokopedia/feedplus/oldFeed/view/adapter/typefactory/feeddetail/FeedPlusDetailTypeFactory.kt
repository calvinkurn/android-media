package com.tokopedia.feedplus.oldFeed.view.adapter.typefactory.feeddetail

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.feedplus.oldFeed.view.viewmodel.feeddetail.FeedDetailItemModel
import com.tokopedia.feedplus.oldFeed.view.viewmodel.feeddetail.FeedDetailProductModel

/**
 * @author by nisie on 5/18/17.
 */
interface FeedPlusDetailTypeFactory {
    fun type(viewModel: FeedDetailItemModel): Int
    fun type(productModel: FeedDetailProductModel): Int
    fun createViewHolder(view: View, viewType: Int): AbstractViewHolder<*>
}
