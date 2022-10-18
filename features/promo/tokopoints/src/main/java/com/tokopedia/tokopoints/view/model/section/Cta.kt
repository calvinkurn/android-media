package com.tokopedia.tokopoints.view.model.section

import android.text.TextUtils
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Cta(
    @SerializedName("icon")
    @Expose
    var icon: String = "",
    @SerializedName("text")
    @Expose
    var text: String = "",
    @SerializedName("url")
    @Expose
    var url: String = "",
    @SerializedName("appLink")
    @Expose
    var appLink: String = "",
    @SerializedName("type")
    @Expose
    var type: String = "",
){
    val isEmpty:Boolean
        get() = TextUtils.isEmpty(text)
}

