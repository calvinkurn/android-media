package com.tokopedia.similarsearch.utils

import com.tokopedia.similarsearch.getsimilarproducts.model.Product
import com.tokopedia.similarsearch.getsimilarproducts.model.Shop
import com.tokopedia.similarsearch.tracking.ECommerce
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.Is.`is`
import org.junit.Test

class SimilarSearchKotlinExtTest {

    @Test
    fun `GetProductAsObjectDataLayer impression and click`() {
        val position = 1
        val product = Product(
            name = "Samsung Galaxy A70 - 6GB/128GB - Black - Garansi Resmi 1 Tahun SEIN",
            id = "465335395",
            price = "Rp 4.950.000",
            categoryName = "Handphone & Tablet"
        ).also {
            it.position = position
        }

        val objectDataLayer = product.asObjectDataLayerImpressionAndClick()

        @Suppress("UNCHECKED_CAST")
        val objectDataLayerMap = objectDataLayer as Map<String, Any>

        assertThat(objectDataLayerMap["name"].toString(), `is`(product.name))
        assertThat(objectDataLayerMap["id"].toString(), `is`(product.id))
        assertThat(
            objectDataLayerMap["price"].toString(),
            `is`(safeCastRupiahToInt(product.price).toString())
        )
        assertThat(objectDataLayerMap["brand"].toString(), `is`(ECommerce.NONE_OTHER))
        assertThat(objectDataLayerMap["category"].toString(), `is`(product.categoryName))
        assertThat(objectDataLayerMap["variant"].toString(), `is`(ECommerce.NONE_OTHER))
        assertThat(objectDataLayerMap["list"].toString(), `is`("/similarproduct"))
        assertThat(objectDataLayerMap["position"].toString(), `is`(position.toString()))
    }

    @Test
    fun getProductAsObjectDataLayerAddToCart(){
        val product = Product(
            name = "Samsung Galaxy A70 - 6GB/128GB - Black - Garansi Resmi 1 Tahun SEIN",
            id = "465335395",
            price = "Rp 4.950.000",
            categoryName = "Handphone & Tablet",
            shop = Shop(
                id = 12345,
                isOfficial = true,
                name = "enterkomputer2"
            ),
            minOrder = 1
        )
        val cartId = "12345"

        val objectDataLayer = product.asObjectDataLayerAddToCart(cartId)

        @Suppress("UNCHECKED_CAST")
        val objectDataLayerMap = objectDataLayer as Map<String, Any>

        assertThat(objectDataLayerMap["name"].toString(), `is`(product.name))
        assertThat(objectDataLayerMap["id"].toString(), `is`(product.id))
        assertThat(
            objectDataLayerMap["price"].toString(),
            `is`(safeCastRupiahToInt(product.price).toString())
        )
        assertThat(objectDataLayerMap["brand"].toString(), `is`(ECommerce.NONE_OTHER))
        assertThat(objectDataLayerMap["category"].toString(), `is`(product.categoryName))
        assertThat(objectDataLayerMap["variant"].toString(), `is`(ECommerce.NONE_OTHER))
        assertThat(objectDataLayerMap["quantity"].toString(), `is`(product.minOrder.toString()))
        assertThat(objectDataLayerMap["shop_id"].toString(), `is`(product.shop.id.toString()))
        assertThat(objectDataLayerMap["shop_type"].toString(), `is`(product.shop.getType()))
        assertThat(objectDataLayerMap["shop_name"].toString(), `is`(product.shop.name))
        assertThat(objectDataLayerMap["category_id"].toString(), `is`(product.categoryId.toString()))
        assertThat(objectDataLayerMap["dimension82"].toString(), `is`(cartId))
    }

    @Test
    fun `Shop getType will return official_store if isOfficial = true`() {
        assertThat(
            Shop(isOfficial = true).getType(),
            `is`("official_store")
        )
    }

    @Test
    fun `Shop getType will return gold_merchant if isGoldShop = true`() {
        assertThat(
            Shop(isGoldShop = true).getType(),
            `is`("gold_merchant")
        )
    }

    @Test
    fun `Shop getType will return official_store if both isOfficial=true and isGoldShop = true`() {
        assertThat(
            Shop(isOfficial = true, isGoldShop = true).getType(),
            `is`("official_store")
        )
    }

    @Test
    fun `Shop getType will return reguler if isOfficial and isGoldShop is false`() {
        assertThat(
            Shop(isGoldShop = false, isOfficial = false).getType(),
            `is`("reguler")
        )
    }
}