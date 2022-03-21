package com.tokopedia.product.addedit.preview.data.model.params.add

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Option (
    @SerializedName("value")
    @Expose
    var value: String = "",
    @SerializedName("unitValueID")
    @Expose
    var valueId: String = "",
    @SerializedName("hexCode")
    @Expose
    var hexCode: String = ""
) : Parcelable
