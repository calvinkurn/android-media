package com.tokopedia.logisticCommon.data.response.shoplocation

import com.google.gson.annotations.SerializedName

data class KeroGetRolloutEligibilityResponse(
    @SerializedName("keroGetRolloutEligibility")
    var keroGetRolloutEligibility: KeroGetRolloutEligibility = KeroGetRolloutEligibility()
)

data class KeroGetRolloutEligibility(
    @SerializedName("status")
    var status: String = "",
    @SerializedName("error")
    var shipperServiceError: ShipperServiceError = ShipperServiceError(),
    @SerializedName("data")
    var data: DataEligibility = DataEligibility()
)

data class DataEligibility(
    /* if value is 1, user is eligible for multilocation */
    @SerializedName("eligibility_state")
    var eligibilityState: Int = -1
)

data class ShipperServiceError(
    @SerializedName("id")
    var id: Long = 0,
    @SerializedName("detail")
    var detail: String = ""
)
