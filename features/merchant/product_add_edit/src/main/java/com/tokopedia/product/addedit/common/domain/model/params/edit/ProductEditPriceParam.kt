package com.tokopedia.product.addedit.common.domain.model.params.edit

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.product.addedit.common.domain.model.responses.ShopParam
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ProductEditPriceParam(
        @SerializedName("productID")
        @Expose
        var productId: String = "",

        @SerializedName("price")
        @Expose
        var price: Float = 0f,

        @SerializedName("priceCurrency")
        @Expose
        val priceCurrency: String = "Rp",

        @SerializedName("shop")
        @Expose
        var shop: ShopParam = ShopParam()
) : Parcelable