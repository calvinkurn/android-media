package com.tokopedia.feedplus.view.adapter.typefactory.feeddetail

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.feedplus.view.viewmodel.feeddetail.FeedDetailHeaderViewModel
import com.tokopedia.feedplus.view.viewmodel.feeddetail.FeedDetailViewModel

/**
 * @author by nisie on 5/18/17.
 */
interface FeedPlusDetailTypeFactory {
    fun type(viewModel: FeedDetailViewModel): Int
    fun type(viewModel: FeedDetailHeaderViewModel): Int
    fun createViewHolder(view: View, viewType: Int): AbstractViewHolder<*>
}