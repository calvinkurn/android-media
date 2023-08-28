package com.tokopedia.home_component.widget.shop_flash_sale

import android.annotation.SuppressLint
import com.tokopedia.home_component.util.DateHelper
import java.text.SimpleDateFormat
import java.util.Date

@SuppressLint("SimpleDateFormat")
data class ShopFlashSaleTimerDataModel (
    val endDate: String = "",
    val format: SimpleDateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZZZZ"),
    val isLoading: Boolean = false,
) {
    val expiredTime: Date
        get() = DateHelper.getExpiredTime(endDate, format)
}
