package com.tokopedia.discovery2.data.producthighlight

import com.tokopedia.atc_common.domain.model.response.AddToCartDataModel
import com.tokopedia.discovery2.data.DataItem

data class DiscoveryOCSDataModel(
    val dataItem: DataItem,
    val atcData: AddToCartDataModel
)
