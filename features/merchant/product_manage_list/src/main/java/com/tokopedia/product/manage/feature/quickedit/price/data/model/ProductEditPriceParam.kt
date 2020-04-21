package com.tokopedia.product.manage.feature.quickedit.price.data.model

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.product.manage.oldlist.data.model.mutationeditproduct.ShopParam
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ProductEditPriceParam(
        @SerializedName("productID")
        @Expose
        var productId: String = "",
        @SerializedName("price")
        @Expose
        var price: Float = 0f,
        @SerializedName("shop")
        @Expose
        var shop: ShopParam = ShopParam()
) : Parcelable