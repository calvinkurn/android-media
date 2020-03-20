package com.tokopedia.product.addedit.common.domain.model.params.add

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Product (
    
    @SerializedName("combination")
    @Expose
    var combination: List<Int>? = null,
    @SerializedName("isPrimary")
    @Expose
    var isPrimary: Boolean? = null,
    @SerializedName("price")
    @Expose
    var price: Int? = null,
    @SerializedName("sku")
    @Expose
    var sku: String? = null,
    @SerializedName("status")
    @Expose
    var status: String? = null,
    @SerializedName("stock")
    @Expose
    var stock: Int? = null,
    @SerializedName("picture")
    @Expose
    var picture: Picture? = null

) : Parcelable

@Parcelize
data class Products (
        @SerializedName("data")
        @Expose
        var data: List<Product> = emptyList()
) : Parcelable