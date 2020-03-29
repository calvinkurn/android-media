package com.tokopedia.product.addedit.common.domain.model.params.add

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Option (
    
    @SerializedName("value")
    @Expose
    var value: String = "",
    @SerializedName("unit_value_id")
    @Expose
    var valueId: Int = 0,
    @SerializedName("hex_code")
    @Expose
    var hexCode: String = ""

) : Parcelable
