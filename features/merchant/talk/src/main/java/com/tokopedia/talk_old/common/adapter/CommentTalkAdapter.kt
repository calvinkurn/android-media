package com.tokopedia.talk_old.common.adapter

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseAdapter
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.talk_old.common.adapter.viewholder.CommentTalkViewHolder

/**
 * @author by nisie on 9/5/18.
 */

class CommentTalkAdapter(adapterTypeFactory: CommentTalkTypeFactoryImpl,
                         listComment: ArrayList<Visitable<*>>)
    : BaseAdapter<CommentTalkTypeFactoryImpl>(adapterTypeFactory, listComment) {

    fun addList(list: ArrayList<Visitable<*>>) {
        this.visitables.addAll(list)
        this.notifyItemRangeInserted(visitables.size, list.size)
    }

    fun add(list: Visitable<*>) {
        this.visitables.add(0,list)
        this.notifyItemRangeInserted(0, 1)
    }

    override fun onViewRecycled(holder: AbstractViewHolder<out Visitable<*>>) {
        super.onViewRecycled(holder)
        when(holder){
            is CommentTalkViewHolder -> holder.onViewRecycled()
        }
    }

}