package com.tokopedia.shopdiscount.product_detail.data.uimodel

import com.tokopedia.shopdiscount.common.data.response.ResponseHeader

data class ShopDiscountProductDetailDeleteUiModel(
    val responseHeader: ResponseHeader = ResponseHeader(),
    val productId: String = ""
)