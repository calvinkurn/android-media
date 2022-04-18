package com.tokopedia.new_product_bundle.common.data.model.uimodel

import com.tokopedia.atc_common.data.model.request.AddToCartBundleRequestParams
import com.tokopedia.atc_common.domain.model.response.AddToCartBundleDataModel

data class AddToCartDataResult (
    val requestParams: AddToCartBundleRequestParams,
    val responseResult: AddToCartBundleDataModel
)