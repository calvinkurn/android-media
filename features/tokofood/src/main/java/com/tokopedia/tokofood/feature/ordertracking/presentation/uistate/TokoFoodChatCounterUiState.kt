package com.tokopedia.tokofood.feature.ordertracking.presentation.uistate

data class TokoFoodChatCounterUiState(
    val counter: Int = 0,
    val error: Throwable? = null
)
