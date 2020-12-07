package com.tokopedia.topchat.chatroom.view.adapter.viewholder

import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.chat_common.view.adapter.viewholder.BaseChatViewHolder
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.setMargin
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.reputation.common.view.AnimatedReputationView
import com.tokopedia.topchat.R
import com.tokopedia.topchat.chatroom.view.adapter.viewholder.common.BackgroundGenerator
import com.tokopedia.topchat.chatroom.view.uimodel.ReviewUiModel
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifycomponents.Label
import com.tokopedia.unifycomponents.toPx
import com.tokopedia.unifyprinciples.Typography

class ReviewViewHolder(
        itemView: View?
) : BaseChatViewHolder<ReviewUiModel>(itemView) {

    private val thumbnail: ImageUnify? = itemView?.findViewById(R.id.iv_product_thumbnail)
    private val label: Label? = itemView?.findViewById(R.id.lb_review_product)
    private val buyerLabelStatus: Label? = itemView?.findViewById(R.id.lb_review_buyer_status)
    private val name: Typography? = itemView?.findViewById(R.id.tv_product_name)
    private val container: ConstraintLayout? = itemView?.findViewById(R.id.cl_container)
    private val reputation: AnimatedReputationView? = itemView?.findViewById(
            R.id.ar_review_reminder
    )

    private val leftBg = BackgroundGenerator.generateLeftBackgroundReviewReminder(container)

    override fun alwaysShowTime(): Boolean = true

    override fun bind(element: ReviewUiModel) {
        super.bind(element)
        bindImage(element)
        bindName(element)
        bindBackground(element)
        bindLabel(element)
        bindBuyerLabel(element)
        bindStar(element)
        bindStarClick(element)
    }

    private fun bindImage(element: ReviewUiModel) {
        ImageHandler.LoadImage(thumbnail, element.reviewCard.imageUrl)
    }

    private fun bindName(element: ReviewUiModel) {
        name?.text = element.reviewCard.productName
        val lp = name?.layoutParams
        if (lp is ViewGroup.MarginLayoutParams) {
            if (element.isSender) {
                name?.setMargin(lp.leftMargin, 4.toPx(), lp.rightMargin, lp.bottomMargin)
            } else {
                name?.setMargin(lp.leftMargin, 0, lp.rightMargin, lp.bottomMargin)
            }
        }
    }

    private fun bindBackground(element: ReviewUiModel) {
        container?.background = leftBg
    }

    private fun bindLabel(element: ReviewUiModel) {
        // TODO("Not yet implemented")
        if (element.isSender) {
            label?.show()
        } else {
            label?.hide()
        }
    }

    private fun bindBuyerLabel(element: ReviewUiModel) {
        if (!element.isSender && element.isReviewed && !element.allowReview) {
            buyerLabelStatus?.show()
        } else {
            buyerLabelStatus?.hide()
        }
    }

    private fun bindStar(element: ReviewUiModel) {
        if (!element.isSender && (element.allowReview || element.isReviewed)) {
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
        reputation?.reviewable = !element.isReviewed || element.allowReview
    }

    companion object {
        val LAYOUT = R.layout.item_topchat_review_reminder
    }
}