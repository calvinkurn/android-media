package com.tokopedia.product.manage.feature.quickedit.stock.data.model

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.product.manage.oldlist.data.model.mutationeditproduct.ShopParam
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ProductEditStockParam(
        @SerializedName("productID")
        @Expose
        var productId: String = "",
        @SerializedName("stock")
        @Expose
        var stock: Int = 0,
        @SerializedName("shop")
        @Expose
        var shop: ShopParam = ShopParam()
) : Parcelable