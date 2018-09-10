package com.tokopedia.talk.inboxtalk.view.adapter

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseAdapter
import com.tokopedia.abstraction.base.view.adapter.model.LoadingMoreModel
import com.tokopedia.talk.inboxtalk.view.viewmodel.EmptyInboxTalkViewModel
import com.tokopedia.talk.producttalk.view.viewmodel.ProductTalkItemViewModel
import com.tokopedia.talk.producttalk.view.viewmodel.TalkThreadViewModel

/**
 * @author by nisie on 8/29/18.
 */

class InboxTalkAdapter(adapterTypeFactory: InboxTalkTypeFactoryImpl,
                       listTalk: ArrayList<Visitable<*>>)
    : BaseAdapter<InboxTalkTypeFactoryImpl>(adapterTypeFactory, listTalk) {

    var emptyModel = EmptyInboxTalkViewModel()

    fun showEmpty() {
        this.visitables.add(emptyModel)
        this.notifyDataSetChanged()
    }

    fun hideEmpty() {
        this.visitables.remove(emptyModel)
        this.notifyDataSetChanged()
    }

    fun addList(list: ArrayList<Visitable<*>>) {
        this.visitables.addAll(list)
        this.notifyItemInserted(visitables.size)
    }

    fun setList(list: ArrayList<Visitable<*>>) {
        this.visitables.addAll(list)
        this.notifyDataSetChanged()
    }

    fun checkCanLoadMore(index: Int): Boolean {
        return if (index == itemCount - 1) {
            visitables[index] is LoadingMoreModel
        } else false
    }

    fun showReportedTalk(adapterPosition: Int) {
        if (this.visitables[adapterPosition] is ProductTalkItemViewModel) {
            (this.visitables[adapterPosition] as ProductTalkItemViewModel).menu.isReported = false
            notifyItemChanged(adapterPosition)
        }
    }

    fun deleteTalkByTalkId(talkId: String) {
        for (talk in visitables) {
            if (talk is TalkThreadViewModel && talk.headThread.talkId == talkId) {
                val position = this.visitables.indexOf(talk)
                this.visitables.remove(talk)
                notifyItemRemoved(position)
            }
        }
    }

    fun deleteComment(talkId: String, commentId: String) {
        for (talk in visitables) {
            if (talk is TalkThreadViewModel && talk.headThread.talkId == talkId) {
                val position = this.visitables.indexOf(talk)
                for (comment in talk.listChild) {
                    if (comment is ProductTalkItemViewModel && comment.commentId == commentId) {
                        talk.listChild.remove(comment)
                    }
                }
                notifyItemChanged(position)
            }
        }

    }

}