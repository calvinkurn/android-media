package com.tokopedia.attachproduct.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class NewAceResponseWrapper {
    @SerializedName("data")
    @Expose
    var data: NewAttachProductApiResponseWrapper? = null
}
