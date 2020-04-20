package com.tokopedia.product.addedit.preview.data.model.params.add

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Product (
    
    @SerializedName("combination")
    @Expose
    var combination: List<Int> = emptyList(),
    @SerializedName("price")
    @Expose
    var price: Double = 0.toDouble(),
    @SerializedName("sku")
    @Expose
    var sku: String = "",
    @SerializedName("status")
    @Expose
    var status: String = "",
    @SerializedName("stock")
    @Expose
    var stock: Long = 0,
    @SerializedName("pictures")
    @Expose
    var pictures: List<Picture> = emptyList()

) : Parcelable