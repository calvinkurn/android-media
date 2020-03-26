package com.tokopedia.product.addedit.common.domain.model.params.edit

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ProductEditParam(
        @SerializedName("productID")
        @Expose
        var productId: String = "",

        @SerializedName("status")
        @Expose
        var productStatus: String = "",

        @SerializedName("menu")
        @Expose
        var productEtalase: ProductEtalase = ProductEtalase(),

        @SerializedName("shop")
        @Expose
        var shop: ShopParam = ShopParam()
) : Parcelable

@Parcelize
data class ProductEtalase(
        @SerializedName("menuID")
        @Expose
        var etalaseId: String = "",

        @SerializedName("name")
        @Expose
        var etalaseName: String = ""
) : Parcelable

@Parcelize
data class ShopParam(
        @SerializedName("id")
        @Expose
        var shopId: String = ""
) : Parcelable