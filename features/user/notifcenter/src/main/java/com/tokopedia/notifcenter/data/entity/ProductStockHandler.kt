package com.tokopedia.notifcenter.data.entity

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ProductStockHandler(
        @Expose
        @SerializedName("notifcenter_productStock")
        var pojo: ProductStockHandlerData = ProductStockHandlerData()
)

data class ProductStockHandlerData(
        @Expose @SerializedName("list") var list: List<NotificationUpdateItem> = emptyList()
)