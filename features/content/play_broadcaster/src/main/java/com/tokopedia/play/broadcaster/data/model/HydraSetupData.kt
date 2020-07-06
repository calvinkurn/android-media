package com.tokopedia.play.broadcaster.data.model

/**
 * Created by jegul on 06/07/20
 */
data class HydraSetupData(
        val selectedProduct: List<ProductData>,
        val selectedCoverData: SerializableCoverData
)