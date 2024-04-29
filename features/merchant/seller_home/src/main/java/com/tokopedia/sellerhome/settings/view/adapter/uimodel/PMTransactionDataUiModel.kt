package com.tokopedia.sellerhome.settings.view.adapter.uimodel

data class PMTransactionDataUiModel(
    val totalTransaction: Long = -1L,
    val isChargeable: Boolean = false,
    val canBeShown: Boolean = false
)
