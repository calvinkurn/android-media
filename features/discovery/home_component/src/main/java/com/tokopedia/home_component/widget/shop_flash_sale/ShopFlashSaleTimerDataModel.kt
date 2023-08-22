package com.tokopedia.home_component.widget.shop_flash_sale

import com.tokopedia.home_component.util.DateHelper
import java.text.SimpleDateFormat
import java.util.Date

data class ShopFlashSaleTimerDataModel (
    val endDate: String = "",
    val format: SimpleDateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss"),
) {
    val expiredTime: Date
        get() = DateHelper.getExpiredTime(endDate, format)
}
