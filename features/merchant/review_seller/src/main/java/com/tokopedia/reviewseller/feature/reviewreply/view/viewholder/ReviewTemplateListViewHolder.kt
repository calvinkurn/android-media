package com.tokopedia.reviewseller.feature.reviewreply.view.viewholder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.reviewseller.feature.reviewreply.view.model.ReplyTemplateUiModel
import com.tokopedia.unifycomponents.ChipsUnify
import kotlinx.android.synthetic.main.item_chat_template.view.*

class ReviewTemplateListViewHolder(
        val view: View,
        private val reviewTemplateListener: ReviewTemplateListener): RecyclerView.ViewHolder(view) {

    fun bind(data: ReplyTemplateUiModel) {
        with(view) {
            chipsTemplateChat.apply {
                centerText = true
                chipText = data.title
                chipSize = ChipsUnify.SIZE_MEDIUM
                chipType = ChipsUnify.TYPE_ALTERNATE
                setOnClickListener {
                    reviewTemplateListener.onItemReviewTemplateClicked(view, chipText.orEmpty())
                }
            }
        }
    }

    interface ReviewTemplateListener {
        fun onItemReviewTemplateClicked(view: View, title: String)
    }
}