package com.tokopedia.logisticaddaddress.features.district_recommendation.uimodel

import com.tokopedia.logisticCommon.uimodel.AddressUiState

sealed interface DiscomSource {
    companion object {
        const val SOURCE_LCA = "lca"
        const val SOURCE_SHOP = "shop"
    }
    data class UserAddress(
        val state: AddressUiState = AddressUiState.AddAddress,
        val isPinpoint: Boolean
    ) : DiscomSource

    object LCA : DiscomSource
    object ShopAddress : DiscomSource
}
