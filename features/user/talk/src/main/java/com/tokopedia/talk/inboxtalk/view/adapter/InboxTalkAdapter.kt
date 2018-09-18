package com.tokopedia.talk.inboxtalk.view.adapter

import android.content.Context
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseAdapter
import com.tokopedia.abstraction.base.view.adapter.model.LoadingMoreModel
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.talk.R
import com.tokopedia.talk.inboxtalk.view.adapter.viewholder.EmptyInboxTalkViewHolder
import com.tokopedia.talk.inboxtalk.view.adapter.viewholder.InboxTalkItemViewHolder
import com.tokopedia.talk.inboxtalk.view.viewmodel.EmptyInboxTalkViewModel
import com.tokopedia.talk.inboxtalk.view.viewmodel.InboxTalkItemViewModel
import com.tokopedia.talk.producttalk.view.viewmodel.ProductTalkItemViewModel

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
            if (talk is InboxTalkItemViewModel && talk.talkThread.headThread.talkId == talkId) {
                val position = this.visitables.indexOf(talk)
                for (comment in talk.talkThread.listChild) {
                    if (comment is ProductTalkItemViewModel && comment.commentId == commentId) {
                        talk.talkThread.listChild.remove(comment)
                    }
                }
                notifyItemChanged(position)
            }
        }

    }

    override fun onViewRecycled(holder: AbstractViewHolder<out Visitable<*>>) {
        super.onViewRecycled(holder)

        when (holder) {
            is EmptyInboxTalkViewHolder -> holder.onViewRecycled()
            is InboxTalkItemViewHolder -> holder.onViewRecycled()
        }

    }

    fun setStatusFollow(talkId: Any, isFollowing: Boolean) {
        for (talk in visitables) {
            if (talk is InboxTalkItemViewModel && talk.talkThread.headThread.talkId == talkId) {
                val position = this.visitables.indexOf(talk)

                talk.talkThread.headThread.menu.allowUnfollow = isFollowing
                talk.talkThread.headThread.menu.allowFollow = !isFollowing

                notifyItemChanged(position)
            }
        }
    }


    fun showReportedTalk(talkId: String) {
        for (talk in visitables) {
            if (talk is InboxTalkItemViewModel && talk.talkThread.headThread.talkId == talkId) {
                val position = this.visitables.indexOf(talk)
                talk.talkThread.headThread.menu.isMasked = false
                talk.talkThread.headThread.comment = talk.talkThread.headThread.rawMessage
                notifyItemChanged(position)
            }
        }
    }

    fun showReportedCommentTalk(talkId: String, commentId: String) {
        for (talk in visitables) {
            if (talk is InboxTalkItemViewModel && talk.talkThread.headThread.talkId == talkId) {
                val position = this.visitables.indexOf(talk)
                for (comment in talk.talkThread.listChild) {
                    if (comment is ProductTalkItemViewModel && comment.commentId == commentId) {
                        comment.menu.isMasked = false
                        comment.comment = comment.rawMessage
                        talk.talkThread.headThread.menu.allowUnmasked = false
                    }
                }
                notifyItemChanged(position)
            }
        }
    }

    fun updateReportTalk(talkId: String, context: Context) {
        for (talk in visitables) {
            if (talk is InboxTalkItemViewModel && talk.talkThread.headThread.talkId == talkId) {
                val position = this.visitables.indexOf(talk)
                talk.talkThread.headThread.menu.isReported = true
                talk.talkThread.headThread.menu.allowReport = false
                talk.talkThread.headThread.menu.allowUnmasked = false
                talk.talkThread.headThread.comment = context.getString(R.string.success_report_talk_masked_message)
                notifyItemChanged(position)
            }
        }
    }

    fun addComment(talkId: String, commentData: ProductTalkItemViewModel) {
        for (talk in visitables) {
            if (talk is InboxTalkItemViewModel && talk.talkThread.headThread.talkId == talkId) {
                val position = this.visitables.indexOf(talk)
                talk.talkThread.listChild.add(commentData)
                notifyItemChanged(position)
            }
        }
    }

    fun updateLastCommentWithId(talkId: String, commentId: String) {
        for (talk in visitables) {
            if (talk is InboxTalkItemViewModel && talk.talkThread.headThread.talkId == talkId) {
                val position = this.visitables.indexOf(talk)
                val lastItem = talk.talkThread.listChild.last()
                val lastIndex = talk.talkThread.listChild.indexOf(lastItem)
                if (lastItem is ProductTalkItemViewModel) {
                    lastItem.commentId = commentId
                    lastItem.isSending = false
                    talk.talkThread.listChild.removeAt(lastIndex)
                    talk.talkThread.listChild.add(lastIndex, lastItem)
                    notifyItemChanged(position)
                }
            }
        }
    }

    fun updateReadStatus(talkId: String) {
        for (talk in visitables) {
            if (talk is InboxTalkItemViewModel && talk.talkThread.headThread.talkId == talkId) {
                val position = this.visitables.indexOf(talk)
                talk.talkThread.headThread.isRead = true
                notifyItemChanged(position)
            }
        }
    }

    fun getItemById(talkId: String): InboxTalkItemViewModel? {
        for (talk in visitables) {
            if (talk is InboxTalkItemViewModel && talk.talkThread.headThread.talkId == talkId) {
                return talk
            }
        }

        return null
    }

    fun getCommentById(talkId: String, commentId: String): ProductTalkItemViewModel? {
        for (talk in visitables) {
            if (talk is InboxTalkItemViewModel && talk.talkThread.headThread.talkId == talkId) {
                val position = this.visitables.indexOf(talk)
                for (comment in talk.talkThread.listChild) {
                    if (comment is ProductTalkItemViewModel && comment.commentId == commentId) {
                        return comment
                    }
                }
            }
        }
        return null
    }

}