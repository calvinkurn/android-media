package com.tokopedia.talk.producttalk.view.adapter

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseAdapter
import com.tokopedia.talk.ProductTalkTypeFactoryImpl
import com.tokopedia.talk.inboxtalk.view.viewmodel.InboxTalkItemViewModel
import com.tokopedia.talk.producttalk.view.viewmodel.*

/**
 * @author by Steven
 */

class ProductTalkAdapter(adapterTypeFactory: ProductTalkTypeFactoryImpl,
                         listProductTalk: ArrayList<Visitable<*>>)
    : BaseAdapter<ProductTalkTypeFactoryImpl>(adapterTypeFactory, listProductTalk) {

    var emptyModel = EmptyProductTalkViewModel()
    var loadModel = LoadProductTalkThreadViewModel()

    fun showEmpty() {
        this.visitables.clear()
        this.visitables.add(emptyModel)
        this.notifyDataSetChanged()
    }

    fun hideEmpty() {
        this.visitables.clear()
        this.notifyDataSetChanged()
    }

    fun setList(list: ArrayList<Visitable<*>>, productTalkTitleViewModel: ProductTalkTitleViewModel) {
        this.visitables.clear()
        this.visitables.add(productTalkTitleViewModel)
        this.visitables.addAll(list)
        notifyDataSetChanged()
    }

    fun addList(listThread: ArrayList<Visitable<*>>) {
        this.visitables.addAll(listThread)
        this.notifyItemRangeInserted(visitables.size, listThread.size)

    }

    fun setLoadModel() {
        this.visitables.add(loadModel)
        this.notifyItemRangeInserted(visitables.size, 1)
    }

    fun dismissLoadModel() {
        this.visitables.remove(loadModel)
        this.notifyItemRemoved(visitables.size)
        showLoading()
    }

    fun deleteTalkByTalkId(talkId: String) {
        for (talk in visitables) {
            if (talk is InboxTalkItemViewModel && talk.talkThread.headThread.talkId == talkId) {
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


    fun showReportedTalk(talkId: String) {
        for (talk in visitables) {
            if (talk is TalkThreadViewModel && talk.headThread.talkId == talkId) {
                val position = this.visitables.indexOf(talk)
                talk.headThread.menu.isMasked = false
                talk.headThread.comment = talk.headThread.rawMessage
                notifyItemChanged(position)
            }
        }
    }

    fun showReportedCommentTalk(talkId: String, commentId: String) {
        for (talk in visitables) {
            if (talk is TalkThreadViewModel && talk.headThread.talkId == talkId) {
                val position = this.visitables.indexOf(talk)
                for (comment in talk.listChild) {
                    if (comment is ProductTalkItemViewModel && comment.commentId == commentId) {
                        comment.menu.isMasked = false
                        comment.comment = comment.rawMessage
                    }
                }
                notifyItemChanged(position)
            }
        }
    }

    fun setStatusFollow(talkId: String, isFollowing: Boolean) {
        for (talk in visitables) {
            if (talk is InboxTalkItemViewModel && talk.talkThread.headThread.talkId == talkId) {
                val position = this.visitables.indexOf(talk)

                talk.talkThread.headThread.menu.allowUnfollow = isFollowing
                talk.talkThread.headThread.menu.allowFollow = !isFollowing

                notifyItemChanged(position)
            }
        }
    }

    fun updateReportTalk(talkId: String) {
        for (talk in visitables) {
            if (talk is InboxTalkItemViewModel && talk.talkThread.headThread.talkId == talkId) {
                val position = this.visitables.indexOf(talk)
                talk.talkThread.headThread.menu.isReported = true
                talk.talkThread.headThread.menu.allowReport = false
                talk.talkThread.headThread.menu.allowUnmasked = false
                notifyItemChanged(position)
            }
        }
    }
}


