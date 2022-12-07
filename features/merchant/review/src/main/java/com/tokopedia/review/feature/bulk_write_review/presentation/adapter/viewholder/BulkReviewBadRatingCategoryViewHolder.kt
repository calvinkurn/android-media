package com.tokopedia.review.feature.bulk_write_review.presentation.adapter.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.review.R
import com.tokopedia.review.databinding.ItemBulkReviewBadRatingCategoryBinding
import com.tokopedia.review.feature.bulk_write_review.presentation.uimodel.BulkReviewBadRatingCategoryUiModel

class BulkReviewBadRatingCategoryViewHolder(
    itemView: View,
    private val listener: Listener
) : AbstractViewHolder<BulkReviewBadRatingCategoryUiModel>(itemView) {

    companion object {
        val LAYOUT = R.layout.item_bulk_review_bad_rating_category
    }

    private val binding = ItemBulkReviewBadRatingCategoryBinding.bind(itemView)

    init {
        setupListener()
    }

    override fun bind(element: BulkReviewBadRatingCategoryUiModel) {
        setupCheckBox(element)
        setupTypography(element.text)
    }

    private fun setupListener() {
        binding.root.setOnClickListener {
            binding.cbBulkReviewBadRatingCategory.performClick()
        }
    }

    private fun setupCheckBox(element: BulkReviewBadRatingCategoryUiModel) {
        binding.cbBulkReviewBadRatingCategory.setOnCheckedChangeListener(null)
        binding.cbBulkReviewBadRatingCategory.isChecked = element.selected
        binding.cbBulkReviewBadRatingCategory.skipAnimation()
        binding.cbBulkReviewBadRatingCategory.setOnCheckedChangeListener { _, checked ->
            if (checked) {
                listener.onBadRatingCategoryChecked(element.id)
            } else {
                listener.onBadRatingCategoryUnchecked(element.id)
            }
        }
    }

    private fun setupTypography(text: String) {
        binding.tvBulkReviewBadRatingCategory.text = text
    }

    interface Listener {
        fun onBadRatingCategoryChecked(badRatingCategoryID: String)
        fun onBadRatingCategoryUnchecked(badRatingCategoryID: String)
    }
}
