package com.tokopedia.withdraw.domain.model.validatePopUp

import com.google.gson.annotations.SerializedName

data class GqlGetValidatePopUpResponse(
        @SerializedName("ValidatePopUpWithdrawal")
        var validatePopUpWithdrawal: ValidatePopUpWithdrawal
)


data class ValidatePopUpWithdrawal(
        @SerializedName("data")
        var data: ValidatePopUpData?,
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
