package com.tokopedia.product.manage.feature.quickedit.stock.data.model

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.product.manage.oldlist.data.model.mutationeditproduct.ShopParam
import com.tokopedia.shop.common.data.source.cloud.model.productlist.ProductStatus
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ProductEditStockParam(
        @SerializedName("productID")
        @Expose
        var productId: String = "",
        @SerializedName("stock")
        @Expose
        var stock: Int = 0,
        @SerializedName("status")
        @Expose
        var status: String = ProductStatus.ACTIVE.name,
        @SerializedName("shop")
        @Expose
        var shop: ShopParam = ShopParam()
) : Parcelable