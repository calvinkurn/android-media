package com.tokopedia.talk.producttalk.view.adapter

import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.design.component.ticker.SelectableSpannedMovementMethod
import com.tokopedia.talk.R
import com.tokopedia.talk.common.adapter.CommentTalkAdapter
import com.tokopedia.talk.common.adapter.CommentTalkTypeFactoryImpl
import com.tokopedia.talk.common.adapter.TalkProductAttachmentAdapter
import com.tokopedia.talk.common.adapter.viewholder.CommentTalkViewHolder
import com.tokopedia.talk.common.adapter.viewholder.LoadMoreCommentTalkViewHolder
import com.tokopedia.talk.common.util.BranchLinkHandlerListener
import com.tokopedia.talk.common.util.BranchLinkHandlerMovementMethod
import com.tokopedia.talk.producttalk.view.viewmodel.TalkState
import com.tokopedia.talk.producttalk.view.viewmodel.TalkThreadViewModel
import kotlinx.android.synthetic.main.product_talk_item.view.*
import kotlinx.android.synthetic.main.reported_talk.view.*
import kotlinx.android.synthetic.main.talk_item.view.*
import kotlinx.android.synthetic.main.thread_talk.view.*

class ProductTalkThreadViewHolder(val v: View,
                                  val listener: TalkItemListener,
                                  private val commentTalkListener: CommentTalkViewHolder.TalkCommentItemListener,
                                  private val talkProductAttachmentListener:
                                  TalkProductAttachmentAdapter
                                  .ProductAttachmentItemClickListener,
                                  private val talkCommentLoadMoreListener: LoadMoreCommentTalkViewHolder.LoadMoreListener) :
        AbstractViewHolder<TalkThreadViewModel>(v) {


    interface TalkItemListener : BranchLinkHandlerListener{
        fun onReplyTalkButtonClick(allowReply: Boolean, talkId: String, shopId: String)
        fun onMenuButtonClicked(menu: TalkState, shopId: String, talkId: String, productId: String)
        fun onYesReportTalkItemClick(talkId: String, shopId: String, productId: String)
        fun onNoShowTalkItemClick(talkId: String)
        fun onItemTalkClick(allowReply: Boolean, talkId: String, shopId: String)
    }

    companion object {
        val LAYOUT = R.layout.product_talk_item
    }

    val thread: View = itemView.findViewById(R.id.thread_head)
    val avatar: ImageView = thread.findViewById(R.id.prof_pict)
    val userName: TextView = thread.findViewById(R.id.username)
    val timestamp: TextView = thread.findViewById(R.id.timestamp)
    val menu: View = thread.findViewById(R.id.menu)
    val content: TextView = thread.findViewById(R.id.talk_content)
    val commentRecyclerView: RecyclerView = itemView.findViewById(R.id.list_child)
    val menuButton: ImageView = itemView.menu
    protected val separatorChild: View = itemView.separatorChild

    private val reportedLayout: View = itemView.layout_reported
    private val reportedMessage: TextView = itemView.reportedMessage
    private val yesReportButton: TextView = itemView.reportYes
    private val noReportButton: TextView = itemView.reportNo
    private val rawMessage: TextView = itemView.rawMessage
    private val separatorReport: View = itemView.separatorReport


    private lateinit var adapter: CommentTalkAdapter

    override fun bind(element: TalkThreadViewModel) {
        element?.run {

            setCommentList(element)
            setHeader(element)
            setupMenuButton(element)
            if (element.headThread.menu.isMasked) {
                setupMaskedMessage(element)
            } else {
                setupNormalTalk(element)
            }
            itemView.replyButton.setOnClickListener {
                listener.onReplyTalkButtonClick(element.headThread.menu.allowReply,
                        element.headThread.talkId,
                        element.headThread.shopId)
            }

            itemView.setOnClickListener {
                listener.onItemTalkClick(element.headThread.menu.allowReply,
                        element.headThread.talkId,
                        element.headThread.shopId)
            }

        }
    }

    private fun setHeader(element: TalkThreadViewModel) {
        ImageHandler.loadImageCircle2(avatar.context, avatar, element.headThread.avatar)
        userName.text = MethodChecker.fromHtml(element.headThread.name)
        content.text = MethodChecker.fromHtml(element.headThread.comment)
        timestamp.text = element.headThread.timestamp
        content.movementMethod = BranchLinkHandlerMovementMethod(listener)

    }

    private fun setCommentList(element: TalkThreadViewModel) {
        if (!element.listChild.isEmpty()) {
            val typeFactoryImpl = CommentTalkTypeFactoryImpl(commentTalkListener,
                    talkProductAttachmentListener, talkCommentLoadMoreListener)
            adapter = CommentTalkAdapter(typeFactoryImpl, element.listChild)
            commentRecyclerView.layoutManager = LinearLayoutManager(itemView.context, LinearLayoutManager
                    .VERTICAL, false)
            commentRecyclerView.adapter = adapter
            commentRecyclerView.visibility = View.VISIBLE
            separatorChild.visibility = View.VISIBLE

        } else {
            commentRecyclerView.visibility = View.GONE
            separatorChild.visibility = View.GONE
        }
    }

    private fun setupMenuButton(element: TalkThreadViewModel) {
        val menu: TalkState = element.headThread.menu

        if (menu.allowDelete || menu.allowFollow || menu.allowReport || menu.allowUnfollow) {
            menuButton.visibility = View.VISIBLE
        } else {
            menuButton.visibility = View.GONE
        }


        menuButton.setOnClickListener {
            listener.onMenuButtonClicked(menu,
                    element.headThread.shopId,
                    element.headThread.talkId,
                    element.headThread.productId)
        }
    }


    private fun setupMaskedMessage(element: TalkThreadViewModel) {
        reportedLayout.visibility = View.VISIBLE
        content.visibility = View.GONE

        reportedMessage.text = MethodChecker.fromHtml(element.headThread.comment)

        if (element.headThread.isOwner) {
            rawMessage.visibility = View.VISIBLE
            separatorReport.visibility = View.VISIBLE
            rawMessage.text = MethodChecker.fromHtml(element.headThread.rawMessage)
        } else {
            rawMessage.visibility = View.GONE
            separatorReport.visibility = View.GONE
        }

        yesReportButton.setOnClickListener {
            listener.onYesReportTalkItemClick(
                    element.headThread.talkId,
                    element.headThread.shopId,
                    element.headThread.productId)
        }
        noReportButton.setOnClickListener {
            listener.onNoShowTalkItemClick(element
                    .headThread.talkId)
        }

    }


    private fun setupNormalTalk(element: TalkThreadViewModel) {
        reportedLayout.visibility = View.GONE

        content.visibility = View.VISIBLE
        content.text = MethodChecker.fromHtml(element.headThread.comment)

    }
}
