package com.tokopedia.withdraw.saldowithdrawal.domain.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

data class GqlGetValidatePopUpResponse(
        @SerializedName("ValidatePopUpWithdrawal")
        var validatePopUpWithdrawal: ValidatePopUpWithdrawal
)


data class ValidatePopUpWithdrawal(
        @SerializedName("data")
        var data: ValidatePopUpData,
        @SerializedName("joinData")
        var joinData: JoinRekeningPremium,
        @SerializedName("message")
        val message: String? = null,
        @SerializedName("status")
        val status: Int = 0
)

data class ValidatePopUpData(

        @SerializedName("note")
        var note: String,

        @SerializedName("needShow")
        val needShow: Boolean,

        @SerializedName("title")
        val title: String
)
@Parcelize
data class JoinRekeningPremium(
        @SerializedName("title")
        var title: String = "",

        @SerializedName("desc")
        val descriptionStringArray: ArrayList<String> = ArrayList(),

        @SerializedName("isJoinPrompt")
        var isJoinPrompt: Boolean = false,

        @SerializedName("programID")
        val programID: Int = 0,

        @SerializedName("programName")
        val programName: String = ""
) : Parcelable