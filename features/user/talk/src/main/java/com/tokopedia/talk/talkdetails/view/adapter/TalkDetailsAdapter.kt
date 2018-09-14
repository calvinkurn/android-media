package com.tokopedia.talk.talkdetails.view.adapter

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseAdapter
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.talk.inboxtalk.view.viewmodel.EmptyInboxTalkViewModel
import com.tokopedia.talk.talkdetails.view.adapter.factory.TalkDetailsTypeFactory
import com.tokopedia.talk.talkdetails.view.adapter.factory.TalkDetailsTypeFactoryImpl
import com.tokopedia.talk.talkdetails.view.viewmodel.TalkDetailsCommentViewModel

/**
 * Created by Hendri on 28/08/18.
 */
class TalkDetailsAdapter(adapterTypeFactory: TalkDetailsTypeFactoryImpl)
    : BaseListAdapter<Visitable<*>,TalkDetailsTypeFactoryImpl>(adapterTypeFactory)  {

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

    fun addItem(item:Visitable<*>) {
        val position = this.visitables.size
        this.visitables.add(item)
        this.notifyItemInserted(position)
    }

    fun setList(list: ArrayList<Visitable<*>>) {
        this.visitables.clear()
        this.visitables.addAll(list)
        notifyDataSetChanged()
    }

    fun removeCommentWithId(id:String) {
        for ((index, item) in this.visitables.withIndex()) {
            if(item is TalkDetailsCommentViewModel) {
                if(item.id.equals(id)) {
                    visitables.remove(item)
                    this.notifyItemRemoved(index)
                    return
                }
            }
        }
    }
}