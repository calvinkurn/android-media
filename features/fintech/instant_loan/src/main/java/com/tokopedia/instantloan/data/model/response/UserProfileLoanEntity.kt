package com.tokopedia.instantloan.data.model.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class UserProfileLoanEntity {


    @SerializedName("whitelist")
    @Expose
    var whitelist: Boolean = false

    @SerializedName("data_collection")
    @Expose
    var dataCollection: Boolean = false

    @SerializedName("whitelist_url")
    @Expose
    var whiteListUrl: String? = null

    @SerializedName("data_collected")
    @Expose
    var dataCollected: Boolean = false

    @SerializedName("redirect_url")
    @Expose
    var redirectUrl: String? = null

    @SerializedName("on_going_loan_id")
    @Expose
    var onGoingLoanId: Int = 0
}
