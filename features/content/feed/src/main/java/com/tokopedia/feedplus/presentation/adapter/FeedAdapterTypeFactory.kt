package com.tokopedia.feedplus.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.feedplus.databinding.ItemFeedPostBinding
import com.tokopedia.feedplus.presentation.adapter.listener.FeedListener
import com.tokopedia.feedplus.presentation.adapter.viewholder.FeedPostViewHolder
import com.tokopedia.feedplus.presentation.fragment.FeedFragment
import com.tokopedia.feedplus.presentation.model.FeedModel

/**
 * Created By : Muhammad Furqan on 02/02/23
 */
class FeedAdapterTypeFactory(private val context: FeedFragment) : BaseAdapterTypeFactory() {

    private val feedListener: FeedListener

    init {
        this.feedListener =  context
    }


   fun type(model: FeedModel) = FeedPostViewHolder.LAYOUT

    override fun createViewHolder(parent: View, type: Int): AbstractViewHolder<out Visitable<*>>? =
        when (type) {
            FeedPostViewHolder.LAYOUT -> FeedPostViewHolder(
                ItemFeedPostBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent as ViewGroup,
                    false
                ),
                feedListener
            )
            else -> super.createViewHolder(parent, type)
        }

}
