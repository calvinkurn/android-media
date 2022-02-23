package com.tokopedia.gifting.domain.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Rules {
    @SerializedName("MaxOrder")
    @Expose
    var maxOrder: Long? = null

    @SerializedName("CustomNotes")
    @Expose
    var customNotes: Boolean? = null
}