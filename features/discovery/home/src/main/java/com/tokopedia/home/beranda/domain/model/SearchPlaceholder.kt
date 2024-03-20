package com.tokopedia.home.beranda.domain.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class SearchPlaceholder {

    @SerializedName("data")
    @Expose
    var data: Data? = Data()

    inner class Data : PlaceHolder() {
        @SerializedName("placeholder_list")
        @Expose
        var placeholders: ArrayList<PlaceHolder>? = arrayListOf()

        @SerializedName("words_source")
        @Expose
        val wordsSource: String = ""

        @SerializedName("impr_id")
        @Expose
        val imprId: String = ""
    }

    open inner class PlaceHolder {
        @SerializedName("placeholder")
        @Expose
        var placeholder: String? = ""

        @SerializedName("keyword")
        @Expose
        var keyword: String? = ""

        @SerializedName("group_id")
        @Expose
        val groupId: String = ""
    }

}
