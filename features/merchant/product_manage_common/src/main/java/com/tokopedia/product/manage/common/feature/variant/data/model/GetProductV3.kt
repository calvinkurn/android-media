package com.tokopedia.product.manage.common.feature.variant.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.shop.common.data.source.cloud.model.productlist.ProductStatus

data class GetProductV3 (
    @Expose
    @SerializedName("productName")
    val productName: String,
    @Expose
    @SerializedName("variant")
    val variant: Variant
) {

    fun isAllStockEmpty(): Boolean {
        return variant.products.all { isEmpty(it) }
    }

    private fun isEmpty(product: Product?): Boolean {
        return product?.stock == 0 || product?.status == ProductStatus.EMPTY
    }
}