package com.tokopedia.topads.common.utils

import android.view.View
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.topads.common.data.util.Utils
import com.tokopedia.unifycomponents.Toaster
import java.text.NumberFormat

object TopadsCommonUtil {

    fun View.showErrorAutoAds(error: String) {
        Toaster.build(
            view = this,
            text = error,
            type = Toaster.TYPE_ERROR
        ).show()
    }

    fun convertToCurrencyString(value: Long): String {
        return (NumberFormat.getNumberInstance(Utils.locale).format(value))
    }

    fun convertMoneyToValue(price: String): Int {
        return price.replace("Rp", "").replace(".", "").replace(",", "").trim().toIntOrZero()
    }
}
