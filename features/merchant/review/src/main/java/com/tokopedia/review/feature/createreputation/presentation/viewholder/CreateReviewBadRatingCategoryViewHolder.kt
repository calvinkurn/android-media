package com.tokopedia.review.feature.createreputation.presentation.viewholder

import android.view.View
import android.widget.CompoundButton
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.review.R
import com.tokopedia.review.databinding.ItemCreateReviewBadRatingCategoryBinding
import com.tokopedia.review.feature.createreputation.model.BadRatingCategory
import com.tokopedia.review.feature.createreputation.presentation.uimodel.visitable.CreateReviewBadRatingCategoryUiModel
import com.tokopedia.review.feature.createreputation.presentation.uistate.CreateReviewBadRatingCategoryUiState

class CreateReviewBadRatingCategoryViewHolder(
    itemView: View,
    private val listener: Listener
) : BaseCreateReviewViewHolder<ItemCreateReviewBadRatingCategoryBinding, CreateReviewBadRatingCategoryUiModel>(
    itemView
) {

    companion object {
        val LAYOUT = R.layout.item_create_review_bad_rating_category
    }

    override val binding = ItemCreateReviewBadRatingCategoryBinding.bind(itemView)
    private val checkboxListener = CheckboxListener()
    private var badRatingCategory: BadRatingCategory? = null

    init {
        binding.setupCheckboxTextListener()
    }

    override fun bind(element: CreateReviewBadRatingCategoryUiModel) {
        this.badRatingCategory = element.uiState.badRatingCategory
        if (element.uiState is CreateReviewBadRatingCategoryUiState.Showing) {
            with(binding) {
                setupCheckbox(element.uiState.badRatingCategory)
                setupImpressionHandler(element.uiState)
            }
        }
    }

    override fun bind(element: CreateReviewBadRatingCategoryUiModel, payloads: MutableList<Any>) {
        bind(element)
    }

    private fun ItemCreateReviewBadRatingCategoryBinding.setupCheckboxTextListener() {
        root.setOnClickListener { reviewBadRatingCategoryCheckbox.toggle() }
    }

    private fun ItemCreateReviewBadRatingCategoryBinding.setupCheckbox(
        badRatingCategory: BadRatingCategory
    ) {
        reviewBadRatingCategoryCheckbox.setOnCheckedChangeListener(null)
        tvReviewBadRatingCategory.text = badRatingCategory.description
        if (reviewBadRatingCategoryCheckbox.isChecked != badRatingCategory.selected) {
            reviewBadRatingCategoryCheckbox.isChecked = badRatingCategory.selected
            reviewBadRatingCategoryCheckbox.skipAnimation()
        }
        reviewBadRatingCategoryCheckbox.setOnCheckedChangeListener(checkboxListener)
    }

    private fun ItemCreateReviewBadRatingCategoryBinding.setupImpressionHandler(
        uiState: CreateReviewBadRatingCategoryUiState
    ) {
        root.addOnImpressionListener(ImpressHolder()) {
            listener.onImpressBadRatingCategory(uiState.badRatingCategory.description)
        }
    }

    private inner class CheckboxListener : CompoundButton.OnCheckedChangeListener {
        override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
            badRatingCategory?.run {
                listener.onBadRatingCategoryClicked(description, isChecked, id, shouldRequestFocus)
            }
        }
    }

    interface Listener {
        fun onImpressBadRatingCategory(description: String)
        fun onBadRatingCategoryClicked(
            description: String,
            checked: Boolean,
            id: String,
            shouldRequestFocus: Boolean
        )
    }
}