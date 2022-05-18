package com.tokopedia.product.addedit.preview.data.model.params.add

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Option (
    @SerializedName("value")
    var value: String = "",
    @SerializedName("unitValueID")
    var valueId: String = "",
    @SerializedName("hexCode")
    var hexCode: String = ""
) : Parcelable
