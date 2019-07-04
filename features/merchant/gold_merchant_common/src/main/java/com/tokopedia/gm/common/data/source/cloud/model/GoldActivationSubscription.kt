package com.tokopedia.gm.common.data.source.cloud.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class GoldActivationSubscription(
        @SerializedName("GoldActivationSubscription")
        @Expose
        val goldActivationData: GMActivation = GMActivation()
)

data class GMActivation(
        @SerializedName("header")
        @Expose
        val header: GMActivationHeader = GMActivationHeader(),

        @SerializedName("data")
        @Expose
        val data: GMActivationData = GMActivationData(),

        @SerializedName("expired_time")
        @Expose
        val expiredTime: String = ""
)

data class GMActivationData(
        @SerializedName("shop_id")
        @Expose
        val shopId: Int = 0,

        @SerializedName("product")
        @Expose
        val product: GMActivationProduct = GMActivationProduct()
)

data class GMActivationHeader(
        @SerializedName("process_time")
        @Expose
        val processTime: Float = 0F,

        @SerializedName("message")
        @Expose
        val message: List<String> = listOf(),

        @SerializedName("reason")
        @Expose
        val reason: String = "",

        @SerializedName("error_code")
        @Expose
        val errorCode: String = ""
)

data class GMActivationProduct(
        @SerializedName("id")
        @Expose
        val id: Int = 0,

        @SerializedName("name")
        @Expose
        val name: String = "", // Power Merchant 30 days

        @SerializedName("initial_duration")
        @Expose
        val initialDurationInDays: Int = 30, // initial Duration in days

        @SerializedName("auto_extend")
        @Expose
        val autoExtend: Boolean = false
)



