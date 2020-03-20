package com.tokopedia.product.addedit.common.domain.model.params.add

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Options (
    
    @SerializedName("unitValueID")
    @Expose
    var unitValueID: String? = null,
    @SerializedName("value")
    @Expose
    var value: String? = null,
    @SerializedName("hexCode")
    @Expose
    var hexCode: String? = null

) : Parcelable
