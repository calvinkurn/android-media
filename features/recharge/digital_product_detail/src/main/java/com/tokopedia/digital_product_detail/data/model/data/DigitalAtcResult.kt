package com.tokopedia.digital_product_detail.data.model.data

import com.tokopedia.common_digital.atc.data.response.ErrorAtc

data class DigitalAtcResult (
    val cartId: String = "",
    val categoryId: String = "",
    val priceProduct: String = "",
    val channelId: String = "",
    val errorAtc: ErrorAtc? = null
)