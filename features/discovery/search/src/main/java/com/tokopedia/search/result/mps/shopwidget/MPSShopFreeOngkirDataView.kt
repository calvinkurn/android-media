package com.tokopedia.search.result.mps.shopwidget

import com.tokopedia.search.result.mps.domain.model.MPSModel.SearchShopMPS.Shop.FreeOngkir

data class MPSShopFreeOngkirDataView(
    val imageUrl: String = "",
    val isActive: Boolean = false,
) {

    companion object {
        fun create(freeOngkir: FreeOngkir) = MPSShopFreeOngkirDataView(
            imageUrl = freeOngkir.imageUrl,
            isActive = freeOngkir.isActive,
        )
    }
}
