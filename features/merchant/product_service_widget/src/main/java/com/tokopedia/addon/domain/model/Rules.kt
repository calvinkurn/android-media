package com.tokopedia.addon.domain.model

import com.google.gson.annotations.SerializedName

data class Rules (
    @SerializedName("MaxOrder")
    var maxOrder: Long = 0L,

    @SerializedName("CustomNotes")
    var customNotes: Boolean = false,

    @SerializedName("Mandatory")
    var mandatory: Boolean = false,

    @SerializedName("RequireCustomShipment")
    var requireCustomShipment: Boolean = false,

    @SerializedName("AutoSelect")
    var autoSelect: Boolean = false
)
