package com.tokopedia.tokopoints.view.util

import java.text.NumberFormat
import java.util.Locale

object CurrencyHelper {
    private val dotFormat: NumberFormat = NumberFormat.getNumberInstance(Locale("in", "id"))
    val commaFormat = NumberFormat.getNumberInstance(Locale("en", "US"))

    fun convertPriceValue(price: Double, useCommaForThousand: Boolean): String? {
        return if (useCommaForThousand) {
            commaFormat.format(price)
        } else {
            dotFormat.format(price)
        }
    }
}