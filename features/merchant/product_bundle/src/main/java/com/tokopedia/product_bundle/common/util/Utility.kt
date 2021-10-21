package com.tokopedia.product_bundle.common.util

import java.text.NumberFormat
import java.util.*

object Utility {

    private const val RUPIAH_FORMAT = "Rp %s"
    private val locale = Locale("in", "ID")

    fun formatToRupiahFormat(value: Int): String {
        return String.format(RUPIAH_FORMAT, NumberFormat.getNumberInstance(locale).format(value))
    }
}