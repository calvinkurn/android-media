package com.tokopedia.product.addedit.preview.data.model.params.add

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Catalog (

    @SerializedName("catalogID")
    @Expose
    var catalogID: String? = null

) : Parcelable
