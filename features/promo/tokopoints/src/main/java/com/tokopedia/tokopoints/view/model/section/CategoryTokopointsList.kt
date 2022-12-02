package com.tokopedia.tokopoints.view.model.section

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class CategoryTokopointsList(
    @SerializedName("id")
    @Expose
    var id: Int? = null,
    @SerializedName("iconImageURL")
    @Expose
    var iconImageURL: String = "",
    @SerializedName("text")
    @Expose
    var text: String = "",

    @SerializedName("url")
    @Expose
    var url: String = "",

    @SerializedName("appLink")
    @Expose
    var appLink: String = "",

    @SerializedName("isNewCategory")
    @Expose
    var isNewCategory: Boolean = false,
)
