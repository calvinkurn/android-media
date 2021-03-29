package com.tokopedia.oneclickcheckout.order.data.get

import com.google.gson.annotations.SerializedName

class OccRemoveProfileResponse(
        @SerializedName("ui_type")
        val type: Int = 0,
        @SerializedName("message")
        val message: OccRemoveProfileMessageResponse = OccRemoveProfileMessageResponse()
)

class OccRemoveProfileMessageResponse(
        @SerializedName("title")
        val title: String = "",
        @SerializedName("description")
        val description: String = ""
)