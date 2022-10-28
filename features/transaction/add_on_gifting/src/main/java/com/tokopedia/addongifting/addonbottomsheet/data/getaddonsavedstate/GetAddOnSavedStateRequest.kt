package com.tokopedia.addongifting.addonbottomsheet.data.getaddonsavedstate

import com.google.gson.annotations.SerializedName

data class GetAddOnSavedStateRequest(
        @SerializedName("add_on_keys")
        var addOnKeys: List<String> = emptyList(),
        @SerializedName("source")
        var source: String = ""
)