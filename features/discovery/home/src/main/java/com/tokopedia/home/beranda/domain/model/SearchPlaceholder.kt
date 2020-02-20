package com.tokopedia.home.beranda.domain.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class SearchPlaceholder {

    @SerializedName("data")
    @Expose
    var data: Data = Data()

    inner class Data {
        @SerializedName("placeholder")
        @Expose
        var placeholder: String = ""

        @SerializedName("keyword")
        @Expose
        var keyword: String = ""
    }

}