package com.tokopedia.tokofood.feature.home.presentation.uimodel

import com.tokopedia.localizationchooseaddress.domain.model.LocalCacheModel

data class TokoFoodUiState(
    var uiState: Int = -1,
    var isLoggedIn: Boolean = false,
    var visitableId: String? = null,
    val localCacheModel: LocalCacheModel = LocalCacheModel(),
    val throwable: Throwable? = null,
    val merchantListParamsModel: TokoFoodMerchantListParams = TokoFoodMerchantListParams()
)

data class TokoFoodMerchantListParams(
    val option: Int = 0,
    val sortBy: Int = 0,
    val cuisine: String = "",
    val brandUId: String = ""
)

object UiEvent{
    const val STATE_LOADING = 1
    const val STATE_FETCH_DYNAMIC_CHANNEL_DATA = 2
    const val STATE_FETCH_MERCHANT_LIST_DATA = 2
    const val STATE_FETCH_COMPONENT_DATA = 3
    const val STATE_FETCH_LOAD_MORE = 4
    const val STATE_NO_ADDRESS = 5
    const val STATE_NO_PIN_POINT = 6
    const val STATE_ERROR = 7
    const val STATE_REMOVE_TICKER = 8
}