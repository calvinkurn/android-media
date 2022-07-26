package com.tokopedia.review.feature.createreputation.presentation.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.review.databinding.WidgetCreateReviewTopicsBinding
import com.tokopedia.review.feature.createreputation.presentation.uistate.CreateReviewTopicsUiState
import kotlin.coroutines.Continuation
import kotlin.coroutines.resume

class CreateReviewTopics @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = Int.ZERO
) : BaseCreateReviewCustomView<WidgetCreateReviewTopicsBinding>(context, attrs, defStyleAttr) {

    companion object {
        private const val TOPIC_SEPARATOR = " • "
    }

    override val binding = WidgetCreateReviewTopicsBinding.inflate(
        LayoutInflater.from(context), this, true
    )

    private fun WidgetCreateReviewTopicsBinding.showTopics(topics: List<String>) {
        tvReviewFormTopics.text = topics.joinToString(TOPIC_SEPARATOR)
    }

    fun updateUI(uiState: CreateReviewTopicsUiState, continuation: Continuation<Unit>) {
        when (uiState) {
            is CreateReviewTopicsUiState.Showing -> {
                binding.showTopics(uiState.topics)
                animateShow(onAnimationEnd = {
                    continuation.resume(Unit)
                })
            }
            is CreateReviewTopicsUiState.Hidden -> {
                animateHide(onAnimationEnd = {
                    continuation.resume(Unit)
                })
            }
        }
    }
}
