package com.tokopedia.contactus.inboxticket2.data.model

import com.google.gson.annotations.SerializedName

data class ChipGetInboxDetail(
        @SerializedName("data")
        var data: Data? = null,
        @SerializedName("messageError")
        var messageError: List<String>? = null
)