package com.tokopedia.review.feature.createreputation.presentation.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.review.databinding.WidgetCreateReviewExpandedTextAreaHelperBinding
import com.tokopedia.review.feature.createreputation.presentation.uistate.CreateReviewTextAreaUiState

class CreateReviewExpandedTextAreaHelper @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = Int.ZERO
) : BaseReviewCustomView<WidgetCreateReviewExpandedTextAreaHelperBinding>(context, attrs, defStyleAttr) {
    override val binding = WidgetCreateReviewExpandedTextAreaHelperBinding.inflate(LayoutInflater.from(context), this, true)

    fun updateUi(uiState: CreateReviewTextAreaUiState) {
        if (uiState is CreateReviewTextAreaUiState.Showing) {
            val text = uiState.helper.getStringValue(context)
            if (text.isNotBlank()) {
                binding.root.text = text
                binding.root.show()
            } else {
                binding.root.gone()
            }
        } else {
            binding.root.gone()
        }
    }
}