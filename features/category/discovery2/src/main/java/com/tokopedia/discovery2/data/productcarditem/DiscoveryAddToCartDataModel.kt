package com.tokopedia.discovery2.data.productcarditem

import com.tokopedia.atc_common.domain.model.response.AddToCartDataModel
import com.tokopedia.cartcommon.data.response.updatecart.UpdateCartV2Data

data class DiscoveryAddToCartDataModel(
    val addToCartDataModel: AddToCartDataModel,
    val requestParams: DiscoATCRequestParams
)

data class DiscoveryRemoveFromCartDataModel(
    val productID: String,
    val message: String,
    val requestParams: DiscoATCRequestParams,
    val cartId: String
)

data class DiscoveryUpdateCartDataModel(
    val updateDataModel: UpdateCartV2Data,
    val requestParams: DiscoATCRequestParams,
    val cartId: String
)


