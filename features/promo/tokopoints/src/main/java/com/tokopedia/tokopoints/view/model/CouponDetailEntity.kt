package com.tokopedia.tokopoints.view.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class CouponDetailEntity (
    @Expose
    @SerializedName("id")
    var id:Int = 0,

    @Expose
    @SerializedName("code")
    var code: String? = null,

    @Expose
    @SerializedName("title")
    var title: String? = null,

    @Expose
    @SerializedName("description")
    var description: String? = null,

    @Expose
    @SerializedName("cta")
    var cta: String? = null
    )