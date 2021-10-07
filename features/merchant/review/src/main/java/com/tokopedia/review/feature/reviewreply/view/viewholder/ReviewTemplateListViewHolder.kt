package com.tokopedia.review.feature.reviewreply.view.viewholder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.review.databinding.ItemChatTemplateBinding
import com.tokopedia.review.feature.reviewreply.view.model.ReplyTemplateUiModel
import com.tokopedia.unifycomponents.ChipsUnify

class ReviewTemplateListViewHolder(
        view: View,
        private val reviewTemplateListener: ReviewTemplateListener): RecyclerView.ViewHolder(view) {

    private val binding = ItemChatTemplateBinding.bind(view)

    fun bind(data: ReplyTemplateUiModel) {
        binding.chipsTemplateChat.apply {
            centerText = true
            chipText = data.title
            chipSize = ChipsUnify.SIZE_MEDIUM
            chipType = ChipsUnify.TYPE_ALTERNATE
            setOnClickListener {
                reviewTemplateListener.onItemReviewTemplateClicked(binding.root, chipText.orEmpty())
            }
        }
    }

    interface ReviewTemplateListener {
        fun onItemReviewTemplateClicked(view: View, title: String)
    }
}