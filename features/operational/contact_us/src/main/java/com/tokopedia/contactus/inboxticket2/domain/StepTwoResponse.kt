package com.tokopedia.contactus.inboxticket2.domain

import com.google.gson.annotations.SerializedName

class StepTwoResponse {
    @SerializedName("comment")
    val comment: Comment? = null
    @SerializedName("is_success")
    var isSuccess = 0

}