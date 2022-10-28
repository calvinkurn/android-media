package com.tokopedia.review.feature.createreputation.presentation.viewholder

import android.view.View
import com.tokopedia.review.R
import com.tokopedia.review.databinding.ItemCreateReviewTemplateItemBinding
import com.tokopedia.review.feature.createreputation.model.CreateReviewTemplate
import com.tokopedia.review.feature.createreputation.presentation.uimodel.visitable.CreateReviewTemplateItemUiModel

class CreateReviewTemplateItemViewHolder(
    itemView: View,
    private val listener: Listener
) : BaseCreateReviewViewHolder<ItemCreateReviewTemplateItemBinding, CreateReviewTemplateItemUiModel>(itemView) {

    companion object {
        val LAYOUT = R.layout.item_create_review_template_item
    }

    override val binding = ItemCreateReviewTemplateItemBinding.bind(itemView)

    override fun bind(element: CreateReviewTemplateItemUiModel) {
        binding.showTemplate(element.data)
    }

    override fun bind(element: CreateReviewTemplateItemUiModel, payloads: MutableList<Any>) {
        bind(element)
    }

    private fun ItemCreateReviewTemplateItemBinding.showTemplate(
        data: CreateReviewTemplate
    ) {
        setupTemplate(data)
        setupListener(data)
    }

    private fun ItemCreateReviewTemplateItemBinding.setupTemplate(
        data: CreateReviewTemplate
    ) {
        layoutTemplateItem.chipText = data.text
    }

    private fun ItemCreateReviewTemplateItemBinding.setupListener(
        data: CreateReviewTemplate
    ) {
        layoutTemplateItem.setOnClickListener {
            listener.onTemplateSelected(data)
        }
    }

    interface Listener {
        fun onTemplateSelected(template: CreateReviewTemplate)
    }
}