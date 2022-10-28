package com.tokopedia.shopdiscount.manage_discount.data.uimodel

import com.tokopedia.shopdiscount.common.data.response.ResponseHeader

data class ShopDiscountSlashPriceStopUiModel(
    val responseHeader: ResponseHeader = ResponseHeader(),
    val productId: String = ""
)