package com.tokopedia.developer_options.api

import com.google.gson.annotations.SerializedName

class FeedbackResponse {
    @SerializedName("id")
    var id: String = ""
    @SerializedName("key")
    var key: String = ""
    @SerializedName("self")
    var self: String = ""

}