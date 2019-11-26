package com.tokopedia.similarsearch

import com.tokopedia.similarsearch.tracking.ECommerce.Companion.NONE_OTHER
import com.tokopedia.similarsearch.getsimilarproducts.model.Product
import com.tokopedia.similarsearch.utils.asObjectDataLayer
import com.tokopedia.similarsearch.utils.safeCastRupiahToInt
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

    describe("Get Product as Object Data Layer") {
        val position = 1
        val product = Product(
                name = "Samsung Galaxy A70 - 6GB/128GB - Black - Garansi Resmi 1 Tahun SEIN",
                id = "465335395",
                price = "Rp 4.950.000",
                categoryName = "Handphone & Tablet"
        ).also {
            it.position = position
        }

        val productAsObjectDataLayer = product.asObjectDataLayer()

        it("should be a hashmap with correct data layer key and values") {
            productAsObjectDataLayer.shouldBeInstanceOf<Map<String, Any>>()

            @Suppress("UNCHECKED_CAST")
            val productDataLayerMap = productAsObjectDataLayer as Map<String, Any>

            productDataLayerMap["name"] shouldBe product.name
            productDataLayerMap["id"] shouldBe product.id
            productDataLayerMap["price"] shouldBe safeCastRupiahToInt(product.price).toString()
            productDataLayerMap["brand"] shouldBe NONE_OTHER
            productDataLayerMap["category"] shouldBe product.categoryName
            productDataLayerMap["variant"] shouldBe NONE_OTHER
            productDataLayerMap["list"] shouldBe "/similarproduct"
            productDataLayerMap["position"] shouldBe position
        }
    }
})