package com.tokopedia.product.addedit.preview.data.model.params.add

import android.annotation.SuppressLint
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import java.math.BigInteger

@Parcelize
@SuppressLint("Invalid Data Type")
data class Product (
        @SerializedName("combination")
        var combination: List<Int> = emptyList(),
        @SuppressLint("Invalid Data Type") // price currently using Integer at server
        @SerializedName("price")
        var price: BigInteger = 0.toBigInteger(),
        @SerializedName("sku")
        var sku: String = "",
        @SerializedName("status")
        var status: String = "",
        @SerializedName("stock")
        var stock: Int? = 0,
        @SerializedName("isPrimary")
        var isPrimary: Boolean = false,
        @SerializedName("pictures")
        var pictures: List<Picture> = emptyList(),
        @SerializedName("weight")
        var weight: Int = 0,
        @SerializedName("weightUnit")
        var weightUnit: String = ""
) : Parcelable