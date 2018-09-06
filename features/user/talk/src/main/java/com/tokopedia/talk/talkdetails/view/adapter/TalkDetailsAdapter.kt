package com.tokopedia.talk.talkdetails.view.adapter

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseAdapter
import com.tokopedia.talk.inboxtalk.view.viewmodel.EmptyInboxTalkViewModel
import com.tokopedia.talk.talkdetails.view.adapter.factory.TalkDetailsTypeFactory
import com.tokopedia.talk.talkdetails.view.adapter.factory.TalkDetailsTypeFactoryImpl

/**
 * Created by Hendri on 28/08/18.
 */
class TalkDetailsAdapter(adapterTypeFactory: TalkDetailsTypeFactoryImpl,
                         listComment: ArrayList<Visitable<*>>)
    : BaseAdapter<TalkDetailsTypeFactoryImpl>(adapterTypeFactory, listComment)  {

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

    fun setList(list: ArrayList<Visitable<*>>) {
        this.visitables.clear()
        this.visitables.addAll(list)
        notifyDataSetChanged()
    }
}