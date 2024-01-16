package com.tokopedia.content.product.picker.seller.util

import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import javax.inject.Inject

/**
 * Created By : Jonathan Darwin on November 14, 2023
 */
class PriceFormatUtil @Inject constructor() {

    private val priceFormatSymbol = DecimalFormatSymbols().apply {
        groupingSeparator = '.'
    }

    private val priceFormat = DecimalFormat("Rp ###,###", priceFormatSymbol)

    fun format(obj: Any): String {
        return priceFormat.format(obj)
    }
}
