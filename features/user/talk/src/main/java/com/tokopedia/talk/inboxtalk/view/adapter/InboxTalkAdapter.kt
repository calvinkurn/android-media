package com.tokopedia.talk.inboxtalk.view.adapter

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseAdapter
import com.tokopedia.talk.inboxtalk.view.viewmodel.EmptyInboxTalkViewModel

/**
 * @author by nisie on 8/29/18.
 */

class InboxTalkAdapter(adapterTypeFactory: InboxTalkTypeFactoryImpl,
                       listTalk: ArrayList<Visitable<*>>)
    : BaseAdapter<InboxTalkTypeFactoryImpl>(adapterTypeFactory, listTalk) {

    var emptyModel = EmptyInboxTalkViewModel()

    fun showEmpty() {
        this.visitables.clear()
        this.visitables.add(emptyModel)
        this.notifyDataSetChanged()
    }

    fun hideEmpty() {
        this.visitables.clear()
        this.notifyDataSetChanged()
    }

    fun addList(list: ArrayList<Visitable<*>>) {
        this.visitables.addAll(list)
        this.notifyItemRangeInserted(visitables.size, list.size)
    }

}