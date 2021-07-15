package com.tokopedia.buyerorder.detail.view.adapter.uimodel

import com.google.gson.annotations.SerializedName
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.buyerorder.detail.view.adapter.typefactory.BuyerProductBundlingTypeFactory

data class BuyerProductBundlingUiModel(
        @SerializedName("bundle_name")
        val bundleName: String = "",
        @SerializedName("order_detail")
        val productList: List<ProductBundleItem> = listOf()
): Visitable<BuyerProductBundlingTypeFactory> {

        override fun type(typeFactory: BuyerProductBundlingTypeFactory): Int = typeFactory.type(this)
}

data class ProductBundleItem(
        @SerializedName("product_thumbnail")
        val productThumbnailUrl: String,
        @SerializedName("product_name")
        val productName: String,
        @SerializedName("product_price")
        val productPrice: Double
)