package com.tokopedia.product.addedit.preview.data.model.params.add

import android.annotation.SuppressLint
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import java.math.BigInteger

@Parcelize
data class Wholesale(
    @SerializedName("minQty")
    var minQty: Int = 0,
    @SuppressLint("Invalid Data Type") // price currently using Integer at server
    @SerializedName("price")
    var price: BigInteger = 0.toBigInteger()
): Parcelable

@Parcelize
data class Wholesales (
    @SerializedName("data")
    var data: List<Wholesale> = emptyList()
): Parcelable