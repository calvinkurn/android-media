package com.tokopedia.feedplus.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewParent
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.base.view.adapter.viewholders.ErrorNetworkViewHolder
import com.tokopedia.feedplus.databinding.ItemFeedNoContentBinding
import com.tokopedia.feedplus.databinding.ItemFeedPostBinding
import com.tokopedia.feedplus.databinding.ItemFeedPostLiveBinding
import com.tokopedia.feedplus.databinding.ItemFeedPostVideoBinding
import com.tokopedia.feedplus.presentation.adapter.listener.FeedListener
import com.tokopedia.feedplus.presentation.adapter.viewholder.FeedErrorViewHolder
import com.tokopedia.feedplus.presentation.adapter.viewholder.FeedNoContentViewHolder
import com.tokopedia.feedplus.presentation.adapter.viewholder.FeedPostImageViewHolder
import com.tokopedia.feedplus.presentation.adapter.viewholder.FeedPostLiveViewHolder
import com.tokopedia.feedplus.presentation.adapter.viewholder.FeedPostVideoViewHolder
import com.tokopedia.feedplus.presentation.fragment.FeedFragment
import com.tokopedia.feedplus.presentation.model.FeedCardImageContentModel
import com.tokopedia.feedplus.presentation.model.FeedCardLivePreviewContentModel
import com.tokopedia.feedplus.presentation.model.FeedCardVideoContentModel
import com.tokopedia.feedplus.presentation.model.FeedNoContentModel

/**
 * Created By : Muhammad Furqan on 02/02/23
 */
class FeedAdapterTypeFactory(
    private val context: FeedFragment,
    private val parentToBeDisabled: ViewParent?,
) : BaseAdapterTypeFactory() {

    private val feedListener: FeedListener

    init {
        this.feedListener = context
    }

    fun type(model: FeedCardImageContentModel) = FeedPostImageViewHolder.LAYOUT

    fun type(model: FeedNoContentModel) = FeedNoContentViewHolder.LAYOUT

    fun type(model: FeedCardVideoContentModel) = FeedPostVideoViewHolder.LAYOUT

    fun type(model: FeedCardLivePreviewContentModel) = FeedPostLiveViewHolder.LAYOUT

    override fun createViewHolder(parent: View, type: Int): AbstractViewHolder<out Visitable<*>>? =
        when (type) {
            FeedPostImageViewHolder.LAYOUT -> FeedPostImageViewHolder(
                ItemFeedPostBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent as ViewGroup,
                    false
                ),
                parentToBeDisabled,
                feedListener
            )
            FeedPostVideoViewHolder.LAYOUT -> FeedPostVideoViewHolder(
                ItemFeedPostVideoBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent as ViewGroup,
                    false
                ),
                feedListener
            )
            FeedPostLiveViewHolder.LAYOUT -> FeedPostLiveViewHolder(
                ItemFeedPostLiveBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent as ViewGroup,
                    false
                ),
                feedListener
            )
            FeedNoContentViewHolder.LAYOUT -> FeedNoContentViewHolder(
                ItemFeedNoContentBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent as ViewGroup,
                    false
                ),
                feedListener
            )
            ErrorNetworkViewHolder.LAYOUT -> FeedErrorViewHolder(
                ItemFeedNoContentBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent as ViewGroup,
                    false
                ),
                feedListener
            )
            else -> super.createViewHolder(parent, type)
        }
}
