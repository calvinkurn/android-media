package com.tokopedia.product.addedit.common.domain.model.params.add

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Wholesale (
    
    @SerializedName("minQty")
    @Expose
    var minQty: Int? = null,
    @SerializedName("price")
    @Expose
    var price: Int? = null

) : Parcelable

@Parcelize
data class Wholesales (
        @SerializedName("data")
        @Expose
        var data: List<Wholesale> = emptyList()
) : Parcelable