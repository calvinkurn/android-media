package com.tokopedia.shop.flash_sale.presentation.draft.uimodel

data class DraftUiModel(
    val isFull: Boolean = false,
    val list: List<DraftItemModel> = emptyList()
)