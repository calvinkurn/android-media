package com.tokopedia.contactus.inboxticket2.domain

import com.google.gson.annotations.SerializedName

class RatingResponse {
    @SerializedName("data")
    var data: Data? = null
    @SerializedName("is_success")
    var isSuccess = 0

}