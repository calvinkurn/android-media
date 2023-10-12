package com.tokopedia.logisticCommon.data.request.shoplocation

import com.google.gson.annotations.SerializedName
import com.tokopedia.graphql.data.GqlParam

data class KeroGetRolloutEligibilityParam(
    @SerializedName("id")
    val shopId: Long,
    @SerializedName("feature_id")
    val featureId: Long = MULTILOC_EXP_ROLLOUT,
    @SerializedName("device")
    val device: String = DEVICE_ANDROID
) : GqlParam {

    companion object {
        private const val MULTILOC_EXP_ROLLOUT = 3L
        private const val DEVICE_ANDROID = "android"
    }
}
