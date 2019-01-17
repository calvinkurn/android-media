package com.tokopedia.instantloan.data.model.response

import com.google.gson.annotations.SerializedName

data class UserProfileLoanEntity(

        @SerializedName("whitelist")
        var whitelist: Boolean = false,

        @SerializedName("data_collection")
        var dataCollection: Boolean = false,

        @SerializedName("whitelist_url")
        var whiteListUrl: String? = null,

        @SerializedName("data_collected")
        var dataCollected: Boolean = false,

        @SerializedName("redirect_url")
        var redirectUrl: String? = null,

        @SerializedName("on_going_loan_id")
        var onGoingLoanId: Int = 0
)
