package com.tokopedia.tokopoints.view.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class CatalogStatusItem(
    @Expose @SerializedName("catalogID")
    var catalogID: Int = 0,
    @Expose
    @SerializedName("isDisabled")
    var isDisabled:Boolean = false,
    @Expose
    @SerializedName("isDisabledButton")
    var isDisabledButton:Boolean = false,
    @Expose
    @SerializedName("quota")
    var quota:Int = 0,
    @Expose
    @SerializedName("upperTextDesc")
    var upperTextDesc: MutableList<String> = mutableListOf()
)
