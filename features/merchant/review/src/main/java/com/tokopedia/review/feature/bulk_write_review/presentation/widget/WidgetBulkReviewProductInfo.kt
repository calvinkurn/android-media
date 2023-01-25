package com.tokopedia.review.feature.bulk_write_review.presentation.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.media.loader.loadImage
import com.tokopedia.review.R
import com.tokopedia.review.databinding.WidgetBulkReviewProductInfoBinding
import com.tokopedia.review.feature.bulk_write_review.presentation.uistate.BulkReviewProductInfoUiState
import com.tokopedia.unifycomponents.BaseCustomView

class WidgetBulkReviewProductInfo(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = Int.ZERO
) : BaseCustomView(context, attrs, defStyleAttr) {
    private val binding = WidgetBulkReviewProductInfoBinding.inflate(
        LayoutInflater.from(context),
        this,
        true
    )

    fun updateUiState(uiState: BulkReviewProductInfoUiState) {
        when (uiState) {
            is BulkReviewProductInfoUiState.Showing -> {
                setupProductImage(uiState.productImageUrl)
                setupProductPurchaseDate(uiState.productPurchaseDate)
                setupProductName(uiState.productName)
                setupProductVariant(uiState.productVariantName)
                show()
            }
        }
    }

    private fun setupProductImage(productImageUrl: String) {
        binding.ivBulkReviewProductImage.loadImage(productImageUrl)
    }

    private fun setupProductPurchaseDate(productPurchaseDate: String) {
        binding.tvBulkReviewProductPurchaseDate.text = productPurchaseDate
    }

    private fun setupProductName(productName: String) {
        binding.tvBulkReviewProductName.text = productName
    }

    private fun setupProductVariant(productVariantName: String) {
        if (productVariantName.isBlank()) {
            binding.tvBulkReviewProductVariant.gone()
        } else {
            binding.tvBulkReviewProductVariant.text = binding.root.context.getString(
                R.string.tv_bulk_review_variant, productVariantName
            )
            binding.tvBulkReviewProductVariant.show()
        }
    }
}
