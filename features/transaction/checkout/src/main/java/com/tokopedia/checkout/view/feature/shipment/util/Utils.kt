package com.tokopedia.checkout.view.feature.shipment.util

import com.tokopedia.design.utils.CurrencyFormatUtil

/**
 * Created by fwidjaja on 2019-04-25.
 */
object Utils {
    @JvmStatic
    fun getFormattedCurrency(price: Int): String {
        return if (price == 0) "" else CurrencyFormatUtil.getThousandSeparatorString(price.toDouble(), false, 0).formattedString

    }
}