package com.tokopedia.feedplus.view.adapter.typefactory.feeddetail

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.feedplus.view.viewmodel.feeddetail.FeedDetailHeaderModel
import com.tokopedia.feedplus.view.viewmodel.feeddetail.FeedDetailItemModel

/**
 * @author by nisie on 5/18/17.
 */
interface FeedPlusDetailTypeFactory {
    fun type(viewModel: FeedDetailItemModel): Int
    fun type(viewModel: FeedDetailHeaderModel): Int
    fun createViewHolder(view: View, viewType: Int): AbstractViewHolder<*>
}