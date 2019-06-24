package com.tokopedia.gm.common.data.source.cloud.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class PowerMerchantActivationResult(
    @SerializedName("expired_time")
    @Expose
    val expiredTime: String, // 2017-07-01 15:04:05
    @SerializedName("product")
    @Expose
    val product: Product = Product(),
    @SerializedName("shop_id")
    @Expose
    val shopId: Int = 0
) {
    data class Product(
        @SerializedName("id")
        @Expose
        val id: Long = 0,

        @SerializedName("name")
        @Expose
        val name: String = "", // e.g: Power Merchant 30 Days

        @SerializedName("initial_duration")
        @Expose
        val initialDurationInDays: Int = 30, // initial Duration in days

        @SerializedName("auto_extend")
        @Expose
        val autoExtend: Boolean = false
    )
}

