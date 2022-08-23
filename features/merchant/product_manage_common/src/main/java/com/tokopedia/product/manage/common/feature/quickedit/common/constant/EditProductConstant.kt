package com.tokopedia.product.manage.common.feature.quickedit.common.constant

import java.math.BigDecimal

object EditProductConstant {

    val MINIMUM_PRICE: BigDecimal = BigDecimal("100")
    const val MAXIMUM_PRICE_LENGTH = 11

    const val MINIMUM_STOCK = 0
    const val MAXIMUM_STOCK = 999999
    const val MAXIMUM_STOCK_LENGTH = 7
}