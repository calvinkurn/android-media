package com.tokopedia.buy_more_get_more.olp.utils.tracker

import com.tokopedia.buy_more_get_more.olp.domain.entity.OfferInfoForBuyerUiModel
import com.tokopedia.buy_more_get_more.olp.domain.entity.OfferProductListUiModel.Product

object OlpTrackerUtil {

    fun generateProductCardImpressionAnalytics(mutableSet: MutableSet<Product>): List<Map<String, Any>> {
        val data = arrayListOf<Map<String, Any>>()
        mutableSet.forEach {
            val productDataMap = mapOf(
                "dimension40" to "dimension40",
                "index" to it.position,
                "item_brand" to "item brand",
                "item_category" to "item category",
                "item_id" to it.productId,
                "item_name" to it.name,
                "item_variant" to "item variant",
                "price" to it.price
            )
            data.add(productDataMap)
        }
        return data
    }

    fun generateAtcNonVariantAnalytics(
        product: Product,
        shopData: OfferInfoForBuyerUiModel.Offering.ShopData
    ): List<Map<String, Any>> {
        val data = arrayListOf<Map<String, Any>>()
        val productDataMap =
            mapOf(
                "category_id" to "-",
                "item_brand" to "-",
                "item_category" to "-",
                "item_id" to product.productId,
                "item_name" to product.name,
                "item_variant" to "-",
                "price" to product.price,
                "quantity" to product.stock,
                "shop_id" to shopData.shopId,
                "shop_name" to shopData.shopName,
                "shop_type" to "-"
            )
        data.add(productDataMap)
        return data
    }
}
