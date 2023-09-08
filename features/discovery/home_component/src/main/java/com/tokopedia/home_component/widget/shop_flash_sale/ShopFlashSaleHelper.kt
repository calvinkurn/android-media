package com.tokopedia.home_component.widget.shop_flash_sale

import java.util.*

object ShopFlashSaleHelper {
    fun getCountdownTimer(
        expiredTime: Date,
        serverTimeOffset: Long = 0L,
    ): Calendar {
        return Calendar.getInstance().apply {
            val currentDate = Date()
            val currentMillisecond: Long = currentDate.time + serverTimeOffset
            val timeDiff = expiredTime.time - currentMillisecond
            if(timeDiff > 0) {
                add(Calendar.SECOND, (timeDiff / 1000 % 60).toInt())
                add(Calendar.MINUTE, (timeDiff / (60 * 1000) % 60).toInt())
                add(Calendar.HOUR, (timeDiff / (60 * 60 * 1000)).toInt())
            } else {
                add(Calendar.MILLISECOND, 900)
            }
        }
    }
}
