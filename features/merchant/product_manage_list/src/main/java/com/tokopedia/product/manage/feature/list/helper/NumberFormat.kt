package com.tokopedia.product.manage.feature.list.helper

import java.text.DecimalFormat

fun Number.getStockFormatted(): String {
    val format =  DecimalFormat("###,###").format(this)
    return format.replace(",",".")
}