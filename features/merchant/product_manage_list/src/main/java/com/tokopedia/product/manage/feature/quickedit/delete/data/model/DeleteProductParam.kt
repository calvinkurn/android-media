package com.tokopedia.product.manage.feature.quickedit.delete.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.product.manage.feature.quickedit.common.data.model.ShopParam
import com.tokopedia.shop.common.data.source.cloud.model.productlist.ProductStatus

data class DeleteProductParam(
        @SerializedName("productID")
        @Expose
        var productId: String = "",
        @SerializedName("status")
        @Expose
        var status: String = ProductStatus.DELETED.name,
        @SerializedName("shop")
        @Expose
        var shop: ShopParam = ShopParam()
)