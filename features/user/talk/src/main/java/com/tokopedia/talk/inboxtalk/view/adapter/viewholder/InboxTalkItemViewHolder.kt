package com.tokopedia.talk.inboxtalk.view.adapter.viewholder

import android.graphics.Typeface
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.talk.R
import com.tokopedia.talk.common.adapter.CommentTalkAdapter
import com.tokopedia.talk.common.adapter.CommentTalkTypeFactoryImpl
import com.tokopedia.talk.common.adapter.TalkProductAttachmentAdapter
import com.tokopedia.talk.common.adapter.viewholder.CommentTalkViewHolder
import com.tokopedia.talk.inboxtalk.view.viewmodel.InboxTalkItemViewModel
import com.tokopedia.talk.producttalk.view.viewmodel.TalkState
import kotlinx.android.synthetic.main.inbox_talk_item.view.*
import kotlinx.android.synthetic.main.inbox_talk_item_product_header.view.*
import kotlinx.android.synthetic.main.reported_talk.view.*
import kotlinx.android.synthetic.main.talk_item.view.*
import kotlinx.android.synthetic.main.thread_talk.view.*

/**
 * @author by nisie on 8/30/18.
 */

class InboxTalkItemViewHolder(val v: View,
                              val listener: TalkItemListener,
                              private val talkCommentListener: CommentTalkViewHolder.TalkCommentItemListener,
                              private val talkProductAttachmentItemClickListener: TalkProductAttachmentAdapter.ProductAttachmentItemClickListener) :
        AbstractViewHolder<InboxTalkItemViewModel>(v) {

    interface TalkItemListener {
        fun onReplyTalkButtonClick(allowReply: Boolean, talkId: String, shopId: String)
        fun onMenuButtonClicked(menu: TalkState, shopId: String, talkId: String, productId: String)
        fun onYesReportTalkItemClick(talkId: String, shopId: String, productId: String)
        fun onNoShowTalkItemClick(talkId: String)
    }

    private val productName: TextView = itemView.productName
    private val productAvatar: ImageView = itemView.productAvatar
    private val profileAvatar: ImageView = itemView.prof_pict
    private val notification: View = itemView.notification
    private val profileName: TextView = itemView.username
    private val timestamp: TextView = itemView.timestamp
    private val menuButton: ImageView = itemView.menu
    private val talkContent: TextView = itemView.talk_content
    private val listComment: RecyclerView = itemView.list_child
    private val replyButton: TextView = itemView.replyButton
    private val separatorChild: View = itemView.separatorChild

    private val reportedLayout: View = itemView.layout_reported
    private val reportedMessage: TextView = itemView.reportedMessage
    private val yesReportButton: TextView = itemView.reportYes
    private val noReportButton: TextView = itemView.reportNo
    private val rawMessage: TextView = itemView.rawMessage
    private val separatorReport: View = itemView.separatorReport


    private lateinit var adapter: CommentTalkAdapter

    companion object {
        val LAYOUT = R.layout.inbox_talk_item
    }

    override fun bind(element: InboxTalkItemViewModel?) {
        element?.run {

            setReadNotification(element)
            setProductHeader(element)
            setupMenuButton(element)
            setProfileHeader(element)
            setCommentList(element)

            if (element.talkThread.headThread.menu.isMasked) {
                setupMaskedMessage(element)
            } else {
                setupNormalTalk(element)
            }

            replyButton.setOnClickListener {
                listener.onReplyTalkButtonClick(
                        element.talkThread.headThread.menu.allowReply,
                        element.talkThread.headThread.talkId,
                        element.talkThread.headThread.shopId)
            }
        }

    }

    private fun setReadNotification(element: InboxTalkItemViewModel) {
        if (element.talkThread.headThread.isRead) {
            notification.visibility = View.GONE
            productName.typeface = Typeface.create("sans-serif-medium", Typeface.NORMAL)
        } else {
            notification.visibility = View.VISIBLE
            productName.typeface = Typeface.create("sans-serif", Typeface.BOLD)
        }
    }

    private fun setupMaskedMessage(element: InboxTalkItemViewModel) {
        reportedLayout.visibility = View.VISIBLE
        talkContent.visibility = View.GONE

        reportedMessage.text = element.talkThread.headThread.comment

        if (element.talkThread.headThread.menu.allowUnmasked) {
            rawMessage.visibility = View.VISIBLE
            separatorReport.visibility = View.VISIBLE
            rawMessage.text = element.talkThread.headThread.rawMessage
            yesReportButton.visibility = View.VISIBLE
            noReportButton.visibility = View.VISIBLE
        } else {
            rawMessage.visibility = View.GONE
            separatorReport.visibility = View.GONE
            yesReportButton.visibility = View.GONE
            noReportButton.visibility = View.GONE
        }

        yesReportButton.setOnClickListener {
            listener.onYesReportTalkItemClick(
                    element.talkThread.headThread.talkId,
                    element.talkThread.headThread.shopId,
                    element.talkThread.headThread.productId)
        }
        noReportButton.setOnClickListener {
            listener.onNoShowTalkItemClick(element.talkThread
                    .headThread.talkId)
        }

    }

    private fun setupNormalTalk(element: InboxTalkItemViewModel) {
        reportedLayout.visibility = View.GONE

        talkContent.visibility = View.VISIBLE
        talkContent.text = element.talkThread.headThread.comment

    }

    private fun setProfileHeader(element: InboxTalkItemViewModel) {
        ImageHandler.loadImageCircle2(profileAvatar.context, profileAvatar, element.talkThread
                .headThread.avatar)
        profileName.text = element.talkThread.headThread.name

        timestamp.text = element.talkThread.headThread.timestamp
    }

    private fun setProductHeader(element: InboxTalkItemViewModel) {
        productName.text = MethodChecker.fromHtml(element.productHeader.productName)
        ImageHandler.LoadImage(productAvatar, element.productHeader.productAvatar)
    }

    private fun setCommentList(element: InboxTalkItemViewModel) {
        if (!element.talkThread.listChild.isEmpty()) {
            val typeFactoryImpl = CommentTalkTypeFactoryImpl(talkCommentListener, talkProductAttachmentItemClickListener)
            adapter = CommentTalkAdapter(typeFactoryImpl, element.talkThread.listChild)
            listComment.layoutManager = LinearLayoutManager(itemView.context, LinearLayoutManager
                    .VERTICAL, false)
            listComment.adapter = adapter
            listComment.visibility = View.VISIBLE
            separatorChild.visibility = View.VISIBLE
        } else {
            listComment.visibility = View.GONE
            separatorChild.visibility = View.GONE
        }
    }

    private fun setupMenuButton(element: InboxTalkItemViewModel) {
        val menu: TalkState = element.talkThread.headThread.menu

        if (menu.allowDelete || menu.allowFollow || menu.allowReport || menu.allowUnfollow) {
            menuButton.visibility = View.VISIBLE
        } else {
            menuButton.visibility = View.GONE
        }

        menuButton.setOnClickListener {
            listener.onMenuButtonClicked(menu,
                    element.talkThread.headThread.shopId,
                    element.talkThread.headThread.talkId,
                    element.talkThread.headThread.productId)
        }
    }

    fun onViewRecycled() {
        ImageHandler.clearImage(profileAvatar)
        ImageHandler.clearImage(productAvatar)
    }

}