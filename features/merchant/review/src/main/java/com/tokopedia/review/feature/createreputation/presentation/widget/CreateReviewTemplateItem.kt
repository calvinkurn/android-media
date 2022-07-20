package com.tokopedia.review.feature.createreputation.presentation.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.review.databinding.WidgetCreateReviewTemplateItemBinding
import com.tokopedia.review.feature.createreputation.presentation.uistate.CreateReviewTemplateItemUiState

class CreateReviewTemplateItem @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = Int.ZERO
) : BaseCreateReviewCustomView<WidgetCreateReviewTemplateItemBinding>(context, attrs, defStyleAttr) {

    private var createReviewTemplateListener: Listener? = null

    override val binding = WidgetCreateReviewTemplateItemBinding.inflate(
        LayoutInflater.from(context),
        this,
        true
    )

    init {
        binding.root.setOnClickListener {
            createReviewTemplateListener?.onTemplateSelected()
        }
    }

    private fun WidgetCreateReviewTemplateItemBinding.showTemplate(
        uiState: CreateReviewTemplateItemUiState.Showing
    ) {
        setupTemplate(uiState)
    }

    private fun WidgetCreateReviewTemplateItemBinding.setupTemplate(
        uiState: CreateReviewTemplateItemUiState.Showing
    ) {
        layoutTemplateItem.chipText = uiState.data.text
    }

    fun updateUi(uiState: CreateReviewTemplateItemUiState) {
        when (uiState) {
            is CreateReviewTemplateItemUiState.Showing -> binding.showTemplate(uiState)
        }
    }

    fun setListener(
        newCreateReviewTemplateListener: Listener,
        newBaseCreateReviewCustomViewListener: BaseCreateReviewCustomView.Listener
    ) {
        createReviewTemplateListener = newCreateReviewTemplateListener
        baseCreateReviewCustomViewListener = newBaseCreateReviewCustomViewListener
    }

    interface Listener {
        fun onTemplateSelected()
    }
}