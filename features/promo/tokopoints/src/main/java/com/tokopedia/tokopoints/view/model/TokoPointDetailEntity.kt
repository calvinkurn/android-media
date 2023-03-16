package com.tokopedia.tokopoints.view.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class TokoPointDetailEntity(
    @Expose
    @SerializedName("tokopoints")
    var tokoPoints: TokoPointEntity = TokoPointEntity()
)
