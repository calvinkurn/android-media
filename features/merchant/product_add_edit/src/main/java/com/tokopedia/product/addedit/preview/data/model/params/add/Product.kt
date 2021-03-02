package com.tokopedia.product.addedit.preview.data.model.params.add

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import java.math.BigInteger

@Parcelize
data class Product (

        @SerializedName("combination")
        @Expose
        var combination: List<Int> = emptyList(),
        @SerializedName("price")
        @Expose
        var price: BigInteger = 0.toBigInteger(),
        @SerializedName("sku")
        @Expose
        var sku: String = "",
        @SerializedName("status")
        @Expose
        var status: String = "",
        @SerializedName("stock")
        @Expose
        var stock: Int? = 0,
        @SerializedName("isPrimary")
        @Expose
        var isPrimary: Boolean = false,
        @SerializedName("pictures")
        @Expose
        var pictures: List<Picture> = emptyList()

) : Parcelable