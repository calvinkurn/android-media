package com.tokopedia.topchat.chatroom.view.adapter.viewholder

import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.chat_common.view.adapter.viewholder.BaseChatViewHolder
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.reputation.common.view.AnimatedReputationView
import com.tokopedia.topchat.R
import com.tokopedia.topchat.chatroom.view.adapter.viewholder.common.BackgroundGenerator
import com.tokopedia.topchat.chatroom.view.uimodel.ReviewUiModel
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifycomponents.Label
import com.tokopedia.unifyprinciples.Typography

class ReviewViewHolder(
        itemView: View?
) : BaseChatViewHolder<ReviewUiModel>(itemView) {

    private val thumbnail: ImageUnify? = itemView?.findViewById(R.id.iv_product_thumbnail)
    private val label: Label? = itemView?.findViewById(R.id.lb_review_product)
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
        bindReputation(element)
    }

    private fun bindImage(element: ReviewUiModel) {
        ImageHandler.LoadImage(thumbnail, element.reviewCard.imageUrl)
    }

    private fun bindName(element: ReviewUiModel) {
        // TODO("Adjust margin if label exist")
        name?.text = element.reviewCard.productName
    }

    private fun bindBackground(element: ReviewUiModel) {
        container?.background = leftBg
    }

    private fun bindLabel(element: ReviewUiModel) {
        // TODO("Not yet implemented")
        label?.hide()
    }

    private fun bindReputation(element: ReviewUiModel) {
        // TODO("Not yet implemented")
    }

    companion object {
        val LAYOUT = R.layout.item_topchat_review_reminder
    }
}