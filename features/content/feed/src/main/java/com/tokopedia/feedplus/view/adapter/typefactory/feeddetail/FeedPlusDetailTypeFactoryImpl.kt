package com.tokopedia.feedplus.view.adapter.typefactory.feeddetail

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.model.EmptyModel
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.feedplus.view.adapter.viewholder.feeddetail.EmptyFeedDetailViewHolder
import com.tokopedia.feedplus.view.adapter.viewholder.feeddetail.FeedDetailViewHolder
import com.tokopedia.feedplus.view.listener.FeedPlusDetailListener
import com.tokopedia.feedplus.view.viewmodel.feeddetail.FeedDetailItemModel
import com.tokopedia.feedplus.view.viewmodel.feeddetail.FeedDetailProductModel

/**
 * @author by nisie on 5/18/17.
 */
class FeedPlusDetailTypeFactoryImpl(private val viewListener: FeedPlusDetailListener) : BaseAdapterTypeFactory(), FeedPlusDetailTypeFactory {
    override fun type(viewModel: FeedDetailItemModel): Int {
        return FeedDetailViewHolder.LAYOUT
    }

    override fun type(productModel: FeedDetailProductModel): Int {
        return FeedDetailViewHolder.LAYOUT
    }

    override fun type(emptyModel: EmptyModel): Int {
        return EmptyFeedDetailViewHolder.LAYOUT
    }

    override fun createViewHolder(view: View, viewType: Int): AbstractViewHolder<*> {
        return when (viewType) {
            FeedDetailViewHolder.LAYOUT -> FeedDetailViewHolder(view, viewListener)
            EmptyFeedDetailViewHolder.LAYOUT -> EmptyFeedDetailViewHolder(view, viewListener)
            else -> super.createViewHolder(view, viewType)
        }
    }

}
