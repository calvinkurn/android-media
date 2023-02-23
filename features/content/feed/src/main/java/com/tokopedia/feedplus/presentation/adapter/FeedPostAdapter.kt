package com.tokopedia.feedplus.presentation.adapter

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseAdapter
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.feedplus.presentation.adapter.viewholder.FeedPostViewHolder
import com.tokopedia.feedplus.presentation.model.FeedModel

/**
 * Created By : Muhammad Furqan on 02/02/23
 */
class FeedPostAdapter(typeFactory: FeedAdapterTypeFactory, var isInClearView: Boolean = false) :
    BaseAdapter<FeedAdapterTypeFactory>(typeFactory) {

    override fun onBindViewHolder(holder: AbstractViewHolder<out Visitable<*>>, position: Int) {
        if (holder is FeedPostViewHolder) {
            holder.bind(visitables[position] as FeedModel, isInClearView)
        }
        super.onBindViewHolder(holder, position)
    }

    fun toggleClearView(isInClearView: Boolean) {
        this.isInClearView = isInClearView
        notifyDataSetChanged()
    }

    // TODO : Later to use DiffUtil
}
