package com.tokopedia.gm.common.data.source.cloud.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class GoldActivationSubscription(
        @SerializedName("goldActivationSubscription")
        @Expose
        val goldActivationData: GMActivation = GMActivation()
) {
    fun isSuccess(): Boolean {
        return goldActivationData.header.errorCode == ""
    }
}

data class GMActivation(
        @SerializedName("ActivationSubscriptionHeader")
        @Expose
        val header: GMActivationHeader = GMActivationHeader(),

        @SerializedName("ActivationSubscriptionData")
        @Expose
        val data: GMActivationData = GMActivationData()
)

data class GMActivationData(
        @SerializedName("shop_tier")
        @Expose
        val shopTier: Int = 0,
        @SerializedName("product")
        @Expose
        val product: GMActivationProduct = GMActivationProduct(),

        @SerializedName("expired_time")
        @Expose
        val expiredTime: String = ""
)

data class GMActivationHeader(
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
        val initialDurationInDays: Int = 0, // initial Duration in days

        @SerializedName("auto_extend")
        @Expose
        val autoExtend: Boolean = false
)



