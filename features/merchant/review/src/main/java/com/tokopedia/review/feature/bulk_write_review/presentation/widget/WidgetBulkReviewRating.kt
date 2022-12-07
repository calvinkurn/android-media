package com.tokopedia.review.feature.bulk_write_review.presentation.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.reputation.common.view.AnimatedRatingPickerCreateReviewView
import com.tokopedia.review.databinding.WidgetBulkReviewRatingBinding
import com.tokopedia.review.feature.bulk_write_review.presentation.uistate.BulkReviewRatingUiState
import com.tokopedia.unifycomponents.BaseCustomView

class WidgetBulkReviewRating @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = Int.ZERO
) : BaseCustomView(context, attrs, defStyleAttr) {
    private val binding = WidgetBulkReviewRatingBinding.inflate(
        LayoutInflater.from(context),
        this,
        true
    )

    fun updateUiState(uiState: BulkReviewRatingUiState) {
        when (uiState) {
            is BulkReviewRatingUiState.Hidden -> {
                gone()
            }
            is BulkReviewRatingUiState.Showing -> {
                binding.reviewFormRating.setRating(uiState.rating)
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
