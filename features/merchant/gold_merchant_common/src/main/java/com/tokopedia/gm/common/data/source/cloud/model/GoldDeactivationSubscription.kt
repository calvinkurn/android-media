package com.tokopedia.gm.common.data.source.cloud.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class GoldDeactivationSubscription(
        @SerializedName("goldTurnOffSubscription")
        @Expose
        val GMDeactivation: GMDeactivation = GMDeactivation()
)

data class GMDeactivation(
        @SerializedName("subscriptionDataResponse")
        @Expose
        val data: GoldData = GoldData(),

        @SerializedName("subscriptionHeaderResponse")
        @Expose
        val header: GoldHeader = GoldHeader()
)

data class GoldData(
        @SerializedName("shopID")
        @Expose
        val shopId: Int = 0,

        @SerializedName("product")
        @Expose
        val product: GoldProduct = GoldProduct(),

        @SerializedName("expiredTime")
        @Expose
        val expiredTime: String = ""
)

data class GoldProduct(
        @SerializedName("id")
        @Expose
        val id: Int = 0,

        @SerializedName("autoextend")
        @Expose
        val autoExtend: Boolean = false
)

data class GoldHeader(
        @SerializedName("process_time")
        @Expose
        val message: List<String> = listOf(),

        @SerializedName("reason")
        @Expose
        val reason: String = "",

        @SerializedName("error_code")
        @Expose
        val errorCode: String = ""

)
