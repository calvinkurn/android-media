package com.tokopedia.checkout.data.model.response.shipment_address_form

import com.google.gson.annotations.SerializedName
import com.tokopedia.logisticdata.data.repository.InsuranceTnCDataStore

/**
 * Created by fwidjaja on 06/03/20.
 */
data class PotentialGainedPoints (
        @SerializedName("reward_points_amount")
        val rewardPointsAmount: Int? = -1,

        @SerializedName("reward_points_amount_str")
        val rewardPointsAmountStr: String? = "",

        @SerializedName("is_eligible")
        val isEligible: Boolean? = false,

        @SerializedName("reward_points_info")
        val rewardPointsInfo: RewardPointsInfo? = RewardPointsInfo()) {

        data class RewardPointsInfo(
                @SerializedName("message")
                val message: String? = "",

                @SerializedName("tnc")
                val tnc: TnC? = TnC()) {

                data class TnC(
                        @SerializedName("title")
                        var title: String? = "",

                        @SerializedName("detail")
                        var detail: String = "")
        }
}