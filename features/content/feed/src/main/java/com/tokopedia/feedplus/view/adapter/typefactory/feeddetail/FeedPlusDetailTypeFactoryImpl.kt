package com.tokopedia.feedplus.view.adapter.typefactory.feeddetail

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.model.EmptyModel
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.feedplus.view.adapter.viewholder.feeddetail.EmptyFeedDetailViewHolder
import com.tokopedia.feedplus.view.adapter.viewholder.feeddetail.FeedDetailHeaderViewHolder
import com.tokopedia.feedplus.view.adapter.viewholder.feeddetail.FeedDetailViewHolder
import com.tokopedia.feedplus.view.listener.FeedPlusDetail
import com.tokopedia.feedplus.view.viewmodel.feeddetail.FeedDetailHeaderViewModel
import com.tokopedia.feedplus.view.viewmodel.feeddetail.FeedDetailViewModel

/**
 * @author by nisie on 5/18/17.
 */
class FeedPlusDetailTypeFactoryImpl(private val viewListener: FeedPlusDetail.View) : BaseAdapterTypeFactory(), FeedPlusDetailTypeFactory {
    override fun type(viewModel: FeedDetailViewModel): Int {
        return FeedDetailViewHolder.LAYOUT
    }

    override fun type(viewModel: FeedDetailHeaderViewModel): Int {
        return FeedDetailHeaderViewHolder.LAYOUT
    }

    override fun type(emptyModel: EmptyModel): Int {
        return EmptyFeedDetailViewHolder.LAYOUT
    }

    override fun createViewHolder(view: View, viewType: Int): AbstractViewHolder<*> {
        return when (viewType) {
            FeedDetailViewHolder.LAYOUT -> FeedDetailViewHolder(view, viewListener)
            FeedDetailHeaderViewHolder.LAYOUT -> FeedDetailHeaderViewHolder(view, viewListener)
            EmptyFeedDetailViewHolder.LAYOUT -> EmptyFeedDetailViewHolder(view, viewListener)
            else -> super.createViewHolder(view, viewType)
        }
    }

}