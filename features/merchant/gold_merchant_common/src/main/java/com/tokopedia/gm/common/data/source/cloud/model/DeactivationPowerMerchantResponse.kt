package com.tokopedia.gm.common.data.source.cloud.model

import com.google.gson.annotations.SerializedName

/**
 * Created By @ilhamsuaib on 31/03/21
 */

data class DeactivationPowerMerchantResponse(
        @SerializedName("goldTurnOffSubscription")
        val goldTurnOffSubscription: GoldTurnOffSubscriptionModel? = null,
) {

    val isSuccess: Boolean
        get() = goldTurnOffSubscription?.header?.errorCode.isNullOrBlank()
}

data class GoldTurnOffSubscriptionModel(
        @SerializedName("header")
        val header: TurnOffSubscriptionHeaderModel? = null,
        @SerializedName("data")
        val data: TurnOffSubscriptionDataModel? = null
)

data class TurnOffSubscriptionHeaderModel(
        @SerializedName("error_code")
        val errorCode: String
)

data class TurnOffSubscriptionDataModel(
        @SerializedName("expiredTime")
        val expiredTime: String
)