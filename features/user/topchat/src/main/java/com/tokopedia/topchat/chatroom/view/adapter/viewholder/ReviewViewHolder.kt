package com.tokopedia.topchat.chatroom.view.adapter.viewholder

import android.os.Handler
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.constraintlayout.widget.ConstraintLayout
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.applink.RouteManager
import com.tokopedia.chat_common.data.DeferredAttachment
import com.tokopedia.chat_common.view.adapter.viewholder.BaseChatViewHolder
import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.reputation.common.view.AnimatedReputationView
import com.tokopedia.topchat.R
import com.tokopedia.topchat.chatroom.domain.pojo.chatattachment.ErrorAttachment
import com.tokopedia.topchat.chatroom.view.adapter.viewholder.common.AdapterListener
import com.tokopedia.topchat.chatroom.view.adapter.viewholder.common.BackgroundGenerator
import com.tokopedia.topchat.chatroom.view.adapter.viewholder.common.DeferredViewHolderAttachment
import com.tokopedia.topchat.chatroom.view.uimodel.ReviewUiModel
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifycomponents.Label
import com.tokopedia.unifycomponents.LoaderUnify
import com.tokopedia.unifycomponents.toPx
import com.tokopedia.unifyprinciples.Typography

class ReviewViewHolder constructor(
        itemView: View?,
        private val listener: Listener?,
        private val deferredAttachment: DeferredViewHolderAttachment,
        private val adapterListener: AdapterListener
) : BaseChatViewHolder<ReviewUiModel>(itemView) {

    private val thumbnail: ImageUnify? = itemView?.findViewById(R.id.iv_product_thumbnail)
    private val label: Label? = itemView?.findViewById(R.id.lb_review_product)
    private val buyerLabelStatus: Label? = itemView?.findViewById(R.id.lb_review_buyer_status)
    private val name: Typography? = itemView?.findViewById(R.id.tv_product_name)
    private val container: ConstraintLayout? = itemView?.findViewById(R.id.cl_container)
    private val loader: LoaderUnify? = itemView?.findViewById(R.id.loader_review)
    private val reputation: AnimatedReputationView? = itemView?.findViewById(
            R.id.ar_review_reminder
    )

    private val leftBg = BackgroundGenerator.generateLeftBackgroundReviewReminder(container)

    interface Listener {
        fun startReview(starCount: Int, review: ReviewUiModel, lastKnownPosition: Int)
        fun trackReviewCardImpression(element: ReviewUiModel)
        fun trackReviewCardClick(element: ReviewUiModel)
    }

    override fun alwaysShowTime(): Boolean = true


    override fun bind(element: ReviewUiModel, payloads: MutableList<Any>) {
        if (payloads.isEmpty()) return
        when (payloads[0]) {
            DeferredAttachment.PAYLOAD_DEFERRED -> bind(element)
            PAYLOAD_REVIEWED -> bindReviewed(element)
            PAYLOAD_NOT_REVIEWED -> bindNotReviewed(element)
        }
    }

    override fun bind(element: ReviewUiModel) {
        super.bind(element)
        bindGravity(element)
        bindSyncReview(element)
        bindLoading(element)
        if (element.isError) {
            adapterListener.changeToFallbackUiModel(element, adapterPosition)
            return
        }
        if (!element.isLoading) {
            bindChatReadStatus(element)
            bindImage(element)
            bindBackground(element)
            bindLabel(element)
            bindName(element)
            bindBuyerLabel(element)
            bindStar(element)
            bindStarClick(element)
            bindItemClick(element)
            bindImpressionTrack(element)
        }
    }

    private fun bindImpressionTrack(element: ReviewUiModel) {
        container?.addOnImpressionListener(element.impressHolder) {
            listener?.trackReviewCardImpression(element)
        }
    }

    private fun bindReviewed(element: ReviewUiModel) {
        bindBuyerLabel(element)
        bindLabel(element)
        bindStar(element)
        bindStarClick(element)
    }

    private fun bindNotReviewed(element: ReviewUiModel) {
        bindStar(element)
        bindStarClick(element)
    }

    private fun bindItemClick(element: ReviewUiModel) {
        container?.setOnClickListener {
            if (element.isReviewed && !element.isSender) {
                listener?.trackReviewCardClick(element)
                RouteManager.route(itemView.context, element.reviewCard.reviewUrl)
            }
        }
    }

    private fun bindGravity(element: ReviewUiModel) {
        if (element.isSender) {
            setGravity(Gravity.END)
        } else {
            setGravity(Gravity.START)
        }
    }

    private fun bindSyncReview(element: ReviewUiModel) {
        if (!element.isLoading) return
        val chatAttachments = deferredAttachment.getLoadedChatAttachments()
        val attachment = chatAttachments[element.attachmentId] ?: return
        if (attachment is ErrorAttachment) {
            element.syncError()
        } else {
            element.updateData(attachment.parsedAttributes)
        }
    }

    private fun bindLoading(element: ReviewUiModel) {
        if (element.isLoading) {
            loader?.show()
        } else {
            loader?.hide()
        }
    }

    private fun setGravity(gravity: Int) {
        val lp = container?.layoutParams as? FrameLayout.LayoutParams
        lp?.let {
            lp.gravity = gravity
            container?.layoutParams = it
        }
    }

    private fun bindImage(element: ReviewUiModel) {
        ImageHandler.LoadImage(thumbnail, element.reviewCard.imageUrl)
    }

    private fun bindBackground(element: ReviewUiModel) {
        container?.background = leftBg
    }

    private fun bindLabel(element: ReviewUiModel) {
        label?.apply {
            when {
                (!element.isSender || element.isSender) && element.hasExpired() -> {
                    show()
                    setLabelType(Label.GENERAL_LIGHT_RED)
                    setText(R.string.title_topchat_review_expire)
                }
                element.isSender && element.waitingForReview() -> {
                    show()
                    setLabelType(Label.GENERAL_LIGHT_GREEN)
                    setText(R.string.title_topchat_review_waiting)
                }
                element.isSender && element.isReviewed -> {
                    show()
                    setLabelType(Label.GENERAL_LIGHT_GREY)
                    setText(R.string.title_topchat_reviewed)
                }
                else -> hide()
            }
        }
    }

    private fun bindName(element: ReviewUiModel) {
        name?.text = element.reviewCard.productName
        val lp = name?.layoutParams
        if (lp is ViewGroup.MarginLayoutParams) {
            if (label?.isVisible == true) {
                name?.setMargin(lp.leftMargin, 4.toPx(), lp.rightMargin, lp.bottomMargin)
            } else {
                name?.setMargin(lp.leftMargin, 0, lp.rightMargin, lp.bottomMargin)
            }
        }
    }

    private fun bindBuyerLabel(element: ReviewUiModel) {
        if (!element.isSender && element.isReviewed) {
            buyerLabelStatus?.show()
        } else {
            buyerLabelStatus?.hide()
        }
    }

    private fun bindStar(element: ReviewUiModel) {
        if (!element.isSender && element.shouldShowStar()) {
            reputation?.show()
            reputation?.resetStars()
            if (element.isReviewed) {
                reputation?.renderInitialReviewWithData(element.ratingInt)
            }
        } else {
            reputation?.hide()
        }
    }

    private fun bindStarClick(element: ReviewUiModel) {
        reputation?.reviewable = element.waitingForReview()
        reputation?.setListener(object : AnimatedReputationView.AnimatedReputationListener {
            override fun onClick(position: Int) {
                Handler().postDelayed({
                    listener?.trackReviewCardClick(element)
                    listener?.startReview(position, element, adapterPosition)
                }, 200)
            }
        })
    }

    companion object {
        const val PAYLOAD_REVIEWED = "payload_reviewed"
        const val PAYLOAD_NOT_REVIEWED = "payload_not_reviewed"
        const val PARAM_FEEDBACK_ID = "feedbackId"
        val LAYOUT = R.layout.item_topchat_review_reminder
    }
}