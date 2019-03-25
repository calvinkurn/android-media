package com.tokopedia.talk.common.adapter.viewholder

import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.talk.R
import com.tokopedia.talk.common.adapter.TalkProductAttachmentAdapter
import com.tokopedia.talk.common.util.BranchLinkHandlerListener
import com.tokopedia.talk.common.util.BranchLinkHandlerMovementMethod
import com.tokopedia.talk.producttalk.view.viewmodel.ProductTalkItemViewModel
import com.tokopedia.talk.producttalk.view.viewmodel.TalkState
import kotlinx.android.synthetic.main.reported_talk.view.*
import kotlinx.android.synthetic.main.talk_item.view.*

/**
 * @author by nisie on 9/5/18.
 */
class CommentTalkViewHolder(val v: View,
                            val listener: TalkCommentItemListener,
                            private val productListener: TalkProductAttachmentAdapter.ProductAttachmentItemClickListener) :
        AbstractViewHolder<ProductTalkItemViewModel>(v) {

    private val SELLER_LABEL_ID: Int = 3

    interface TalkCommentItemListener : BranchLinkHandlerListener{
        fun onCommentMenuButtonClicked(menu: TalkState, shopId: String, talkId: String, commentId: String, productId: String)
        fun onYesReportTalkCommentClick(talkId: String, shopId: String, productId: String, commentId: String)
        fun onNoShowTalkCommentClick(talkId: String, commentId: String)
        fun onGoToUserProfile(userId: String)
        fun onGoToShopPage(shopId: String)

    }

    private val profileAvatar: ImageView = itemView.prof_pict
    private val profileName: TextView = itemView.username
    private val datetime: TextView = itemView.timestamp
    private val menuButton: ImageView = itemView.menu
    private val talkContent: TextView = itemView.talk_content
    private val listProduct: RecyclerView = itemView.productAttachment
    private val profileLabel: TextView = itemView.seller_label

    private lateinit var adapter: TalkProductAttachmentAdapter

    private val reportedLayout: View = itemView.layout_reported
    private val reportedMessage: TextView = itemView.reportedMessage
    private val yesReportButton: TextView = itemView.reportYes
    private val noReportButton: TextView = itemView.reportNo
    private val rawMessage: TextView = itemView.rawMessage
    private val separatorReport: View = itemView.separatorReport

    companion object {
        val LAYOUT = R.layout.talk_item
    }


    override fun bind(element: ProductTalkItemViewModel?) {
        element?.run {

            setupProductAttachment(element)
            setupMenuButton(element)
            setProfileHeader(element)

            if (element.menu.isMasked) {
                setupMaskedMessage(element)
            } else {
                setupNormalTalk(element)
            }
            talkContent.text = MethodChecker.fromHtml(element.comment)
            if (element.isSending) itemView.setBackgroundResource(R.color.talk_send_background)
            else itemView.setBackgroundResource(R.color.transparent)

            if (element.hasSeparator) itemView.commentSeparator.visibility = View.VISIBLE
            else itemView.commentSeparator.visibility = View.GONE

        }

    }

    private fun setupNormalTalk(element: ProductTalkItemViewModel) {
        reportedLayout.visibility = View.GONE
        talkContent.visibility = View.VISIBLE
        talkContent.text = MethodChecker.fromHtml(element.comment)
        talkContent.movementMethod = BranchLinkHandlerMovementMethod(listener)

    }

    private fun setupMaskedMessage(element: ProductTalkItemViewModel) {
        reportedLayout.visibility = View.VISIBLE
        talkContent.visibility = View.GONE
        reportedMessage.text = MethodChecker.fromHtml(element.comment)

        if (element.isOwner) {
            rawMessage.visibility = View.VISIBLE
            separatorReport.visibility = View.VISIBLE
            rawMessage.text = MethodChecker.fromHtml(element.rawMessage)
            yesReportButton.visibility = View.VISIBLE
            noReportButton.visibility = View.VISIBLE
        } else {
            rawMessage.visibility = View.GONE
            separatorReport.visibility = View.GONE
            yesReportButton.visibility = View.GONE
            noReportButton.visibility = View.GONE
        }

        yesReportButton.setOnClickListener {
            listener.onYesReportTalkCommentClick(
                    element.talkId, element.shopId, element.productId,
                    element.commentId
            )
        }
        noReportButton.setOnClickListener {
            listener.onNoShowTalkCommentClick(element.talkId,
                    element.commentId)
        }
    }


    private fun setProfileHeader(element: ProductTalkItemViewModel) {
        ImageHandler.loadImageCircle2(profileAvatar.context, profileAvatar, element.avatar)
        profileName.text = MethodChecker.fromHtml(element.name)
        datetime.text = element.timestamp
        if (element.labelId == SELLER_LABEL_ID) {
            profileLabel.visibility = View.VISIBLE
            profileLabel.text = element.labelString
            profileAvatar.setOnClickListener {
                listener.onGoToShopPage(element.shopId)
            }
            profileName.setOnClickListener {
                listener.onGoToShopPage(element.shopId)
            }
        } else {
            profileLabel.visibility = View.GONE

            profileAvatar.setOnClickListener {
                listener.onGoToUserProfile(element.userId)
            }

            profileName.setOnClickListener {
                listener.onGoToUserProfile(element.userId)
            }
        }


    }

    private fun setupProductAttachment(element: ProductTalkItemViewModel) {
        if (!element.productAttachment.isEmpty()) {

            adapter = TalkProductAttachmentAdapter(productListener, element.productAttachment)
            listProduct.layoutManager = LinearLayoutManager(itemView.context,
                    LinearLayoutManager
                            .VERTICAL, false)
            listProduct.adapter = adapter
            listProduct.visibility = View.VISIBLE
        }
    }

    private fun setupMenuButton(element: ProductTalkItemViewModel) {
        val menu: TalkState = element.menu

        if (menu.allowDelete || menu.allowFollow || menu.allowReport || menu.allowUnfollow) {
            menuButton.visibility = View.VISIBLE
        } else {
            menuButton.visibility = View.GONE
        }

        menuButton.setOnClickListener {
            listener.onCommentMenuButtonClicked(menu,
                    element.shopId,
                    element.talkId,
                    element.commentId,
                    element.productId)
        }
    }

    override fun onViewRecycled() {
        ImageHandler.clearImage(profileAvatar)
    }

}