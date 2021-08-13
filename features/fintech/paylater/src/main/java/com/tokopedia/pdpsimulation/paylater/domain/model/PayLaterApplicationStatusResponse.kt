package com.tokopedia.pdpsimulation.paylater.domain.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

data class PayLaterApplicationStatusResponse(
        @SerializedName("creditapplication_getUserApplicationStatus")
        val userCreditApplicationStatus: UserCreditApplicationStatus,
)

@Parcelize
data class UserCreditApplicationStatus(
        @SerializedName("tkp_user_id")
        val tkpUserId: String,
        @SerializedName("application_detail")
        val applicationDetailList: ArrayList<PayLaterApplicationDetail>?,
) : Parcelable

@Parcelize
data class PayLaterApplicationDetail(
        @SerializedName("id")
        val payLaterApplicationId: Long? = 0,
        @SerializedName("gateway_name")
        val payLaterGatewayName: String,
        @SerializedName("gateway_code")
        val payLaterGatewayCode: String,
        @SerializedName("application_status")
        val payLaterApplicationStatus: String,
        @SerializedName("expiration_date")
        val payLaterExpirationDate: String?,
        @SerializedName("app_status_content")
        var payLaterStatusContent: PayLaterStatusContent?,
        var payLaterApplicationStatusLabelStringId: Int = 0,
        var payLaterApplicationStatusLabelType: Int = 0,
) : Parcelable

@Parcelize
data class PayLaterStatusContent(
        @SerializedName("email")
        val verificationContentEmail: String?,
        @SerializedName("subheader")
        val verificationContentSubHeader: String?,
        @SerializedName("phone_number")
        val verificationContentPhoneNumber: String?,
        @SerializedName("pop_up_detail")
        val verificationContentPopUpDetail: String?,
        @SerializedName("additional_info")
        val verificationContentInfo: String?,
) : Parcelable