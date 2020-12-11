package com.tokopedia.paylater.domain.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

data class PayLaterApplicationStatusResponse(
        @SerializedName("creditapplication_getUserApplicationStatus")
        val userCreditApplicationStatus: UserCreditApplicationStatus
)

@Parcelize
data class UserCreditApplicationStatus(
        @SerializedName("tkp_user_id")
        val tkpUserId: String,
        @SerializedName("application_detail")
        val applicationDetailList: ArrayList<PayLaterApplicationDetail>
): Parcelable

@Parcelize
data class PayLaterApplicationDetail(
        @SerializedName("id")
        val payLaterApplicationId : Long,
        @SerializedName("gateway_name")
        val payLaterGatewayName: String,
        @SerializedName("gateway_code")
        val payLaterGatewayCode: String,
        @SerializedName("application_status")
        val payLaterApplicationStatus: String,
        @SerializedName("expiration_date")
        val payLaterExpirationDate: String
): Parcelable

