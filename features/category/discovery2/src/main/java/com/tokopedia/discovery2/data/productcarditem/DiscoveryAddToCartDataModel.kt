package com.tokopedia.discovery2.data.productcarditem

import com.tokopedia.atc_common.domain.model.response.AddToCartDataModel

data class DiscoveryAddToCartDataModel(
    val addToCartDataModel: AddToCartDataModel,
    val requestParams: DiscoATCRequestParams
)