package com.tokopedia.shop.flashsale.presentation.draft.uimodel

data class DraftUiModel(
    val isFull: Boolean = false,
    val list: List<DraftItemModel> = emptyList()
)