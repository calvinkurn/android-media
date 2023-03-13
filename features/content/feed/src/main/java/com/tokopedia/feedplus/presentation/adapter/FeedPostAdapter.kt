package com.tokopedia.feedplus.presentation.adapter

import androidx.recyclerview.widget.DiffUtil
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseAdapter
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.feedplus.presentation.adapter.viewholder.FeedPostImageViewHolder
import com.tokopedia.feedplus.presentation.util.FeedDiffUtilCallback

/**
 * Created By : Muhammad Furqan on 02/02/23
 */
class FeedPostAdapter(typeFactory: FeedAdapterTypeFactory) :
    BaseAdapter<FeedAdapterTypeFactory>(typeFactory) {

    fun onToggleClearView() {
        notifyDataSetChanged()
    }

    fun updateList(newList: List<Visitable<*>>) {
        val diffResult = DiffUtil.calculateDiff(FeedDiffUtilCallback(visitables, newList))

        visitables.clear()
        visitables.addAll(newList)
        diffResult.dispatchUpdatesTo(this)
    }

    override fun onBindViewHolder(
        holder: AbstractViewHolder<out Visitable<*>>,
        position: Int,
        payloads: MutableList<Any>
    ) {
        super.onBindViewHolder(holder, position, payloads)
//        if (payloads.isEmpty()) super.onBindViewHolder(holder, position, payloads)
//        else {
//            if (holder is FeedPostImageViewHolder)
//                holder.bindWithPayloads(position, payloads)
//        }
    }


    // TODO : Later to use DiffUtil
}
