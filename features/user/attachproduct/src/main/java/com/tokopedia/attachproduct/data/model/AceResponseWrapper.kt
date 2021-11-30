package com.tokopedia.attachproduct.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class AceResponseWrapper {
    @SerializedName("data")
    @Expose
    var data: AttachProductApiResponseWrapper = AttachProductApiResponseWrapper()
}
