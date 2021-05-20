package com.tokopedia.sellerfeedback.presentation.viewholder

import android.view.View
import androidx.annotation.ColorRes
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.sellerfeedback.R
import com.tokopedia.sellerfeedback.presentation.uimodel.SellerFeedbackFormUiModel
import kotlinx.android.synthetic.main.item_seller_feedback_form.view.*

class SellerFeedbackFormViewHolder(
    itemView: View,
    private val listener: SellerFeedbackFormListener
): AbstractViewHolder<SellerFeedbackFormUiModel>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_seller_feedback_form
    }

    override fun bind(data: SellerFeedbackFormUiModel) {
        itemView.run {
            badFeedbackBtn.setOnClickListener {
                listener.onClickFeedbackBtn(com.tokopedia.unifyprinciples.R.color.Unify_Y500)
            }

            neutralFeedbackBtn.setOnClickListener {
                listener.onClickFeedbackBtn(com.tokopedia.unifyprinciples.R.color.Unify_Y400)
            }

            goodFeedbackBtn.setOnClickListener {
                listener.onClickFeedbackBtn(com.tokopedia.unifyprinciples.R.color.Unify_G500)
            }
        }
    }

    interface SellerFeedbackFormListener {
        fun onClickFeedbackBtn(@ColorRes colorId: Int)
    }
}
