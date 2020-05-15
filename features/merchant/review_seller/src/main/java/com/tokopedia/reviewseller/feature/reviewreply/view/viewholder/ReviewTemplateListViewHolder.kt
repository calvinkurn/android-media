package com.tokopedia.reviewseller.feature.reviewreply.view.viewholder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.reviewseller.R
import com.tokopedia.reviewseller.feature.reviewreply.view.model.ReplyTemplateUiModel
import com.tokopedia.unifycomponents.ChipsUnify

class ReviewTemplateListViewHolder(
        val view: View,
        private val reviewTemplateListener: ReviewTemplateListener): RecyclerView.ViewHolder(view) {

    private val chipsTemplate: ChipsUnify = itemView.findViewById(R.id.chipsTemplateChat)

    fun bind(data: ReplyTemplateUiModel) {
        chipsTemplate.apply {
            chipText = data.title
            chipType = ChipsUnify.TYPE_SELECTED
        }

        itemView.setOnClickListener {
            reviewTemplateListener.onItemReviewTemplateClicked(view, chipsTemplate.chipText.orEmpty())
        }
    }

    interface ReviewTemplateListener {
        fun onItemReviewTemplateClicked(view: View, title: String)
    }
}