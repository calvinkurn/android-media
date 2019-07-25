package com.tokopedia.instantloan.data.model.response

import com.google.gson.annotations.SerializedName

data class GqlFilterDataResponse(

        @SerializedName("le_getfilter")
        var gqlFilterData: GqlFilterData

)

data class GqlFilterData(
        @SerializedName("JumlahPinjamanMobile")
        var gqlLoanAmountResponse: ArrayList<GqlLoanAmountResponse>,

        @SerializedName("PeriodePinjamanMobile")
        var gqlLoanPeriodResponse: GqlLoanPeriodResponse

)


data class GqlLoanAmountResponse(
        @SerializedName("Step")
        var step: String,

        @SerializedName("Label")
        var label: String,

        @SerializedName("Value")
        var value: String
)

data class GqlLoanPeriodResponse(
        @SerializedName("Year")
        var loanYear: GqlLoanDurationResponse,

        @SerializedName("Month")
        var loanMonth: GqlLoanDurationResponse
)

data class GqlLoanDurationResponse(
        @SerializedName("Max")
        var max: Int,

        @SerializedName("Min")
        var min: Int
)