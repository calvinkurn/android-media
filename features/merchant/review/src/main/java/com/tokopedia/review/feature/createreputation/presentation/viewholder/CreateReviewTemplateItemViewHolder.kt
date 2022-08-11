package com.tokopedia.review.feature.createreputation.presentation.viewholder

import android.view.View
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.review.R
import com.tokopedia.review.databinding.ItemCreateReviewTemplateItemBinding
import com.tokopedia.review.feature.createreputation.model.CreateReviewTemplate
import com.tokopedia.review.feature.createreputation.presentation.uimodel.visitable.CreateReviewTemplateItemUiModel
import com.tokopedia.review.feature.createreputation.presentation.uistate.CreateReviewTemplateItemUiState
import com.tokopedia.review.feature.createreputation.presentation.widget.BaseCreateReviewCustomView
import com.tokopedia.review.feature.createreputation.presentation.widget.CreateReviewTemplateItem

class CreateReviewTemplateItemViewHolder(
    itemView: View,
    private val createReviewTemplateItemViewHolderListener: Listener,
    baseCreateReviewCustomViewListener: BaseCreateReviewCustomView.Listener
) : BaseCreateReviewViewHolder<ItemCreateReviewTemplateItemBinding, CreateReviewTemplateItemUiModel>(itemView) {

    companion object {
        val LAYOUT = R.layout.item_create_review_template_item
    }

    override val binding = ItemCreateReviewTemplateItemBinding.bind(itemView)
    private var template: CreateReviewTemplateItemUiModel? = null

    init {
        binding.createReviewTemplate.setListener(CreateReviewTemplateItemListener(), baseCreateReviewCustomViewListener)
    }

    override fun bind(element: CreateReviewTemplateItemUiModel) {
        template = element
        binding.createReviewTemplate.updateUi(element.uiState)
    }

    override fun bind(element: CreateReviewTemplateItemUiModel, payloads: MutableList<Any>) {
        bind(element)
    }

    override fun calculateWrapHeight(): Int {
        val wrapContentWidthMeasureSpec = View.MeasureSpec.makeMeasureSpec(Int.ZERO, View.MeasureSpec.UNSPECIFIED)
        val wrapContentHeightMeasureSpec = View.MeasureSpec.makeMeasureSpec(Int.ZERO, View.MeasureSpec.UNSPECIFIED)
        binding.root.measure(wrapContentWidthMeasureSpec, wrapContentHeightMeasureSpec)
        return binding.root.measuredHeight
    }

    private inner class CreateReviewTemplateItemListener: CreateReviewTemplateItem.Listener {
        override fun onTemplateSelected() {
            template?.uiState?.let {
                if (it is CreateReviewTemplateItemUiState.Showing) {
                    createReviewTemplateItemViewHolderListener.onTemplateSelected(it.data)
                }
            }
        }
    }

    interface Listener {
        fun onTemplateSelected(template: CreateReviewTemplate)
    }
}