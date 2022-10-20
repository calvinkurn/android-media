package com.tokopedia.search.result.presentation.model

import com.tokopedia.search.result.domain.model.SearchProductModel.OtherRelatedProductFreeOngkir

data class FreeOngkirDataView(
    val isActive: Boolean = false,
    val imageUrl: String = ""
) {

    companion object {
        fun create(otherRelatedFreeOngkir: OtherRelatedProductFreeOngkir) =
            FreeOngkirDataView(
                otherRelatedFreeOngkir.isActive,
                otherRelatedFreeOngkir.imageUrl,
            )
    }
}
