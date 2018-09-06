package com.tokopedia.talk.common.adapter

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseAdapter

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

}