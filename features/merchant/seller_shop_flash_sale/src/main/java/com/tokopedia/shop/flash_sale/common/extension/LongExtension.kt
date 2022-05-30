package com.tokopedia.shop.flash_sale.common.extension

import java.util.*


fun Long.toDate(): Date {
    return Date(this)
}

fun Long.epochToDate() : Date {
    return Date(this * 1000)
}