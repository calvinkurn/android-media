package com.tokopedia.tokopoints.view.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class LobDetails(
    @Expose
    @SerializedName("title")
    var title: String = "",
    @Expose
    @SerializedName("description")
    var description: String = "",
    @Expose
    @SerializedName("services")
    var lobs: MutableList<LobItem> = mutableListOf(),
)
