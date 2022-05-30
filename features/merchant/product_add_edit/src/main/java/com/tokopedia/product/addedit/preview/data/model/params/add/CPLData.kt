package com.tokopedia.product.addedit.preview.data.model.params.add

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class CPLData (
    @SerializedName("shipperServices")
    var shipperServices: ArrayList<Long>? = arrayListOf()
): Parcelable