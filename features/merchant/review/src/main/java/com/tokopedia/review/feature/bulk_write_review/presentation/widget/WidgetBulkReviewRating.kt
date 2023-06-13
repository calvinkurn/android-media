package com.tokopedia.review.feature.bulk_write_review.presentation.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.reputation.common.view.AnimatedRatingPickerCreateReviewView
import com.tokopedia.review.databinding.WidgetBulkReviewRatingBinding
import com.tokopedia.review.feature.bulk_write_review.presentation.uistate.BulkReviewRatingUiState
import com.tokopedia.unifycomponents.BaseCustomView

class WidgetBulkReviewRating(
    context: Context,
    attrs: AttributeSet?
) : BaseCustomView(context, attrs) {
    private val binding = WidgetBulkReviewRatingBinding.inflate(
        LayoutInflater.from(context),
        this,
        true
    )

    init {
        reset()
    }

    fun reset() {
        binding.reviewFormRating.resetStars()
    }

    fun updateUiState(uiState: BulkReviewRatingUiState) {
        when (uiState) {
            is BulkReviewRatingUiState.Showing -> {
                binding.reviewFormRating.setRating(uiState.rating, uiState.animate)
                show()
            }
        }
    }

    fun setListener(listener: Listener) {
        binding.reviewFormRating.setListener(
            object : AnimatedRatingPickerCreateReviewView.AnimatedReputationListener {
                override fun onClick(position: Int) {
                    listener.onRatingChanged(position)
                }
            }
        )
    }

    interface Listener {
        fun onRatingChanged(rating: Int)
    }
}
