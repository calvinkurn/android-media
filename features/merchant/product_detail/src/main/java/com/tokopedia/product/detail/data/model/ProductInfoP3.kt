package com.tokopedia.product.detail.data.model

import com.tokopedia.product.detail.data.model.estimasiongkir.RatesEstimationModel

data class ProductInfoP3(
        var rateEstimation: RatesEstimationModel? = null,
        var isWishlisted: Boolean = false
)