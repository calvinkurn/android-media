package com.tokopedia.gm.common.data.source.cloud.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created By @ilhamsuaib on 31/03/21
 */

data class DeactivationPowerMerchantResponse(
        @Expose
        @SerializedName("goldTurnOffSubscription")
        val goldTurnOffSubscription: GoldTurnOffSubscriptionModel? = null,
) {

    val isSuccess: Boolean
        get() = goldTurnOffSubscription?.header?.errorCode.isNullOrBlank()
}

data class GoldTurnOffSubscriptionModel(
        @Expose
        @SerializedName("header")
        val header: TurnOffSubscriptionHeaderModel? = null,
        @Expose
        @SerializedName("data")
        val data: TurnOffSubscriptionDataModel? = null
)

data class TurnOffSubscriptionHeaderModel(
        @Expose
        @SerializedName("error_code")
        val errorCode: String
)

data class TurnOffSubscriptionDataModel(
        @Expose
        @SerializedName("expiredTime")
        val expiredTime: String
)