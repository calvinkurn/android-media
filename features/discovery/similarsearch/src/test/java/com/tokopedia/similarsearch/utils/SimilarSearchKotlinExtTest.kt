package com.tokopedia.similarsearch.utils

import com.tokopedia.similarsearch.tracking.ECommerce.Companion.NONE_OTHER
import com.tokopedia.similarsearch.getsimilarproducts.model.Product
import com.tokopedia.similarsearch.getsimilarproducts.model.Shop
import com.tokopedia.similarsearch.testutils.shouldBe
import com.tokopedia.similarsearch.testutils.shouldBeInstanceOf
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe

internal class SimilarSearchKotlinExtTest: Spek({

    describe("Safe cast rupiah to int") {

        it("should return 0 when input rupiah is null OR empty string") {
            safeCastRupiahToInt("") shouldBe 0
            safeCastRupiahToInt(null) shouldBe 0
        }

        it("should return 0 when input rupiah cannot be cast to int") {
            safeCastRupiahToInt("sfsadfasdf") shouldBe 0
        }

        it("should remove `Rp` and `.` from rupiah string, and return the integer") {
            safeCastRupiahToInt("Rp 1.000.000") shouldBe 1000000
        }

        it("should ignore spaces from rupiah string, and return the integer") {
            safeCastRupiahToInt("   Rp 1.000.   000     ") shouldBe 1000000
        }
    }

    describe("Get Product as Object Data Layer Impression and Click") {
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

        it("should be a map with correct data layer key and values") {
            objectDataLayer.shouldBeInstanceOf<Map<String, Any>>()

            @Suppress("UNCHECKED_CAST")
            val objectDataLayerMap = objectDataLayer as Map<String, Any>

            objectDataLayerMap["name"] shouldBe product.name
            objectDataLayerMap["id"] shouldBe product.id
            objectDataLayerMap["price"] shouldBe safeCastRupiahToInt(product.price).toString()
            objectDataLayerMap["brand"] shouldBe NONE_OTHER
            objectDataLayerMap["category"] shouldBe product.categoryName
            objectDataLayerMap["variant"] shouldBe NONE_OTHER
            objectDataLayerMap["list"] shouldBe "/similarproduct"
            objectDataLayerMap["position"] shouldBe position
        }
    }

    describe("Shop Get Type") {
        it ("should return official_store if isOfficial = true") {
            Shop(isOfficial = true).getType() shouldBe "official_store"
        }
        it ("should return gold_merchant if isGoldShop = true") {
            Shop(isGoldShop = true).getType() shouldBe "gold_merchant"
        }
        it("should return official_store if both isOfficial = true and isGoldShop = true") {
            Shop(isOfficial = true, isGoldShop = true).getType() shouldBe "official_store"
        }
        it ("should return reguler if isOfficial and isGoldShop is false") {
            Shop(isGoldShop = false, isOfficial = false).getType() shouldBe "reguler"
        }
    }

    describe("Get Product as Object Data Layer Add to Cart") {
        val product = Product(
                name = "Samsung Galaxy A70 - 6GB/128GB - Black - Garansi Resmi 1 Tahun SEIN",
                id = "465335395",
                price = "Rp 4.950.000",
                categoryName = "Handphone & Tablet",
                shop = Shop(
                        id = 12345,
                        isOfficial = true,
                        name = "enterkomputer2"
                )
        )

        val objectDataLayer = product.asObjectDataLayerAddToCart()

        it ("should be a map with correct data layer key and values") {
            objectDataLayer.shouldBeInstanceOf<Map<String, Any>>()

            @Suppress("UNCHECKED_CAST")
            val objectDataLayerMap = objectDataLayer as Map<String, Any>

            objectDataLayerMap["name"] shouldBe product.name
            objectDataLayerMap["id"] shouldBe product.id
            objectDataLayerMap["price"] shouldBe safeCastRupiahToInt(product.price).toString()
            objectDataLayerMap["brand"] shouldBe NONE_OTHER
            objectDataLayerMap["category"] shouldBe product.categoryName
            objectDataLayerMap["variant"] shouldBe NONE_OTHER
            // quantity
            objectDataLayerMap["shop_id"] shouldBe product.shop.id
            objectDataLayerMap["shop_type"] shouldBe product.shop.getType()
            objectDataLayerMap["shop_name"] shouldBe product.shop.name
            objectDataLayerMap["category_id"] shouldBe product.categoryId
            // dimension82 cart id
        }
    }
})