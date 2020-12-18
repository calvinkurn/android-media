package com.tokopedia.product.manage.feature.list.extension

import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.product.manage.common.feature.list.data.model.ProductViewModel
import com.tokopedia.product.manage.feature.list.constant.ProductManageListConstant.PATTERN_DATE
import java.text.SimpleDateFormat
import java.time.temporal.ChronoUnit
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.math.abs

fun MutableList<ProductViewModel>.findIndex(productId: String): Int? {
    return find { it.id == productId }?.let { indexOf(it) }
}

val String.isMoreOneMonth: Boolean
    get() {
        val simpleDateFormat = SimpleDateFormat(PATTERN_DATE, Locale.getDefault())
        val pastDate = simpleDateFormat.parse(this)
        val oneMonth = 1
        val dateNow = System.currentTimeMillis()
        val diffInLong = abs(dateNow - pastDate?.time.orZero())
        val diff = TimeUnit.DAYS.convert(diffInLong, TimeUnit.MILLISECONDS)
        return (diff / 30) > oneMonth
    }