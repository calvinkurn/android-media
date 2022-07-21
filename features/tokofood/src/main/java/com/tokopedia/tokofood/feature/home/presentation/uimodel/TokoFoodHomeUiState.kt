package com.tokopedia.tokofood.feature.home.presentation.uimodel

import com.tokopedia.localizationchooseaddress.domain.model.LocalCacheModel

data class TokoFoodHomeUiState(
    var uiState: Int = -1,
    val localCacheModel: LocalCacheModel = LocalCacheModel()
)
object UiEvent{
    const val STATE_LOADING = 1
    const val STATE_FETCH_DYNAMIC_CHANNEL_DATA = 2
    const val STATE_FETCH_COMPONENT_DATA = 3
    const val STATE_FETCH_LOAD_MORE = 4
    const val STATE_PROGRESS_BAR = 5
}