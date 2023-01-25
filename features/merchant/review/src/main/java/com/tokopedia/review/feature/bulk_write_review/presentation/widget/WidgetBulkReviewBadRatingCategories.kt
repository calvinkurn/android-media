package com.tokopedia.review.feature.bulk_write_review.presentation.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.review.R
import com.tokopedia.review.databinding.WidgetBulkReviewBadRatingCategoryBinding
import com.tokopedia.review.feature.bulk_write_review.presentation.uistate.BulkReviewBadRatingCategoryUiState
import com.tokopedia.review.feature.createreputation.presentation.widget.BaseReviewCustomView

class WidgetBulkReviewBadRatingCategories(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = Int.ZERO
) : BaseReviewCustomView<WidgetBulkReviewBadRatingCategoryBinding>(context, attrs, defStyleAttr) {
    override val binding = WidgetBulkReviewBadRatingCategoryBinding.inflate(
        LayoutInflater.from(context),
        this,
        true
    )

    fun updateUiState(uiState: BulkReviewBadRatingCategoryUiState, animate: Boolean) {
        when (uiState) {
            is BulkReviewBadRatingCategoryUiState.Hidden -> {
                animateHide(animate = animate, onAnimationEnd = { gone() })
            }
            is BulkReviewBadRatingCategoryUiState.Showing -> {
                val badRatingCategoriesString = uiState
                    .badRatingCategory
                    .filter { it.selected }
                    .joinToString { it.text }
                binding.tvBadRatingReasons.text = context.getString(
                    R.string.tv_bulk_review_bad_rating_category_format,
                    badRatingCategoriesString
                )
                animateShow(animate = animate, onAnimationStart = { show() })
            }
        }
    }

    fun setListener(listener: Listener) {
        binding.tvChangeBadRatingReasons.setOnClickListener {
            listener.onClickChangeBadRatingCategory()
        }
    }

    interface Listener {
        fun onClickChangeBadRatingCategory()
    }
}
