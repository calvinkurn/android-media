package com.tokopedia.centralizedpromo.view.model

data class CentralizedPromoUiState(
    val isLoading: Boolean = false,
    val errorOnePage: String? = null,
    val data: List<BaseUiModel> = listOf()
)
