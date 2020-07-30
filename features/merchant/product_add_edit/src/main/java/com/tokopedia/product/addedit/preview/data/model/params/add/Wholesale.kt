package com.tokopedia.product.addedit.preview.data.model.params.add

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import java.math.BigInteger

@Parcelize
data class Wholesale(
        @SerializedName("minQty")
    @Expose
        var minQty: Int = 0,
        @SerializedName("price")
    @Expose
        var price: BigInteger = 0.toBigInteger()
) : Parcelable

@Parcelize
data class Wholesales (
        @SerializedName("data")
        @Expose
        var data: List<Wholesale> = emptyList()
) : Parcelable