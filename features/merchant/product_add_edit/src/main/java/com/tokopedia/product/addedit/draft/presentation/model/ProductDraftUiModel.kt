package com.tokopedia.product.addedit.draft.presentation.model

data class ProductDraftUiModel (
        val draftId: Long,
        val imageUrl: String,
        val productName: String,
        val completionPercent: Int,
)