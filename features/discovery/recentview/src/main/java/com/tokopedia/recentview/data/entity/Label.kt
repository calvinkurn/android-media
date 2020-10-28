package com.tokopedia.recentview.data.entity

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Label {
    @SerializedName("title")
    @Expose
    var title: String = ""
    @SerializedName("color")
    @Expose
    var color: String = ""

}