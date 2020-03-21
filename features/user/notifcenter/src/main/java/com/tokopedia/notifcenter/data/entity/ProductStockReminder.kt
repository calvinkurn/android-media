package com.tokopedia.notifcenter.data.entity

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ProductStockReminder(
        @Expose
        @SerializedName("notifcenter_setReminderBump")
        var pojo: ProductStockReminderData = ProductStockReminderData()
)

data class ProductStockReminderData(
        @Expose
        @SerializedName("status")
        var status: String = ""
)