package com.tokopedia.searchbar.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class HintData (
    @SerializedName("placeholder")
    @Expose
    var placeholder: String = "",
    @SerializedName("keyword")
    @Expose
    var keyword: String = ""
)