package com.tokopedia.search.result.presentation.model

import com.tokopedia.search.result.domain.model.SearchProductModel.OtherRelatedProductFreeOngkir
import com.tokopedia.search.result.domain.model.SearchProductV5

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

        fun create(freeShipping: SearchProductV5.Data.FreeShipping) =
            FreeOngkirDataView(true, freeShipping.url)
    }
}
