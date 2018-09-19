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
        val iterator = visitables.iterator()

        while(iterator.hasNext()) {
            val talk = iterator.next()
            if (talk is TalkThreadViewModel && talk.headThread.talkId == talkId) {
                val position = this.visitables.indexOf(talk)
                iterator.remove()
                notifyItemRemoved(position)
            }
        }
    }

    fun deleteComment(talkId: String, commentId: String) {
        val iter = visitables.iterator()

        while (iter.hasNext()) {
            val talk = iter.next()
            if (talk is InboxTalkItemViewModel && talk.talkThread.headThread.talkId == talkId) {
                val position = this.visitables.indexOf(talk)
                val iterComment = talk.talkThread.listChild.iterator()

                while (iterComment.hasNext()) {
                    val comment = iterComment.next()
                    if (comment is ProductTalkItemViewModel && comment.commentId == commentId) {
                        talk.talkThread.listChild.remove(comment)
                        break
                    }
                }
                notifyItemChanged(position)
                break
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
            if (talk is TalkThreadViewModel && talk.headThread.talkId == talkId) {
                val position = this.visitables.indexOf(talk)

                talk.headThread.menu.allowUnfollow = isFollowing
                talk.headThread.menu.allowFollow = !isFollowing

                notifyItemChanged(position)
            }
        }
    }

    fun updateReportTalk(talkId: String) {
        for (talk in visitables) {
            if (talk is TalkThreadViewModel && talk.headThread.talkId == talkId) {
                val position = this.visitables.indexOf(talk)
                talk.headThread.menu.isReported = true
                talk.headThread.menu.allowReport = false
                talk.headThread.menu.allowUnmasked = false
                notifyItemChanged(position)
            }
        }
    }
}


