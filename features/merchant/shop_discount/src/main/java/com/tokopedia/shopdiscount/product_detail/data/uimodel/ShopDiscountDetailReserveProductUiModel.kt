package com.tokopedia.shopdiscount.product_detail.data.uimodel

import com.tokopedia.shopdiscount.common.data.response.ResponseHeader

data class ShopDiscountDetailReserveProductUiModel(
    val responseHeader: ResponseHeader = ResponseHeader(),
    val selectedProductVariantId: String = "",
    val requestId: String = ""
)