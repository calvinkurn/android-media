package com.tokopedia.tkpd.flashsale.presentation.manageproduct.helper

import com.tokopedia.tkpd.flashsale.util.constant.NumericalNormalizationConstant.BULK_APPLY_PERCENT_NORMALIZATION
import kotlin.math.roundToInt

object DiscountUtil {
    fun calculatePrice(percentInput: Long, originalPrice: Long): Long {
        return originalPrice - (percentInput * originalPrice / BULK_APPLY_PERCENT_NORMALIZATION)
    }

    fun calculatePercent(priceInput: Long, originalPrice: Long): Int {
        return (((originalPrice.toDouble() - priceInput.toDouble()) / originalPrice.toDouble()) * BULK_APPLY_PERCENT_NORMALIZATION).roundToInt()
    }
}