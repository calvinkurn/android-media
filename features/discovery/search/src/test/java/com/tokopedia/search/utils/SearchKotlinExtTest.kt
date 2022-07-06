package com.tokopedia.search.utils

import com.tokopedia.discovery.common.constants.SearchConstant
import com.tokopedia.productcard.ProductCardModel
import com.tokopedia.search.result.presentation.model.ProductItemDataView
import com.tokopedia.search.shouldBe
import com.tokopedia.search.shouldHaveKeyValue
import com.tokopedia.search.shouldHaveSize
import org.junit.Test

internal class SearchKotlinExtTest {

    @Test
    fun `Convert map values to string - Origin Map is null`() {
        val originalMap : Map<String, Any>? = null

        val mapValuesInString = originalMap.convertValuesToString()

        mapValuesInString shouldHaveSize 0
    }

    @Test
    fun `Convert map values to string - Original Map is not null`() {
        val originalMap = mutableMapOf<String, Any>().also {
            it["string"] = "string"
            it["integer"] = 1
            it["boolean"] = false
            it["double"] = 1.0
        }

        val mapValuesInString = originalMap.convertValuesToString()

        mapValuesInString.shouldHaveKeyValue("string", "string")
        mapValuesInString.shouldHaveKeyValue("integer", "1")
        mapValuesInString.shouldHaveKeyValue("boolean", "false")
        mapValuesInString.shouldHaveKeyValue("double", "1.0")
    }

    @Test
    fun `Decode query parameters in applink`() {
        null.decodeQueryParameter() shouldBe ""
        "".decodeQueryParameter() shouldBe ""
        "tokopedia".decodeQueryParameter() shouldBe "tokopedia"
        "tokopedia://search?".decodeQueryParameter() shouldBe "tokopedia://search?"
        "tokopedia://search??".decodeQueryParameter() shouldBe "tokopedia://search?"
        "tokopedia://search?q".decodeQueryParameter() shouldBe "tokopedia://search?"
        "tokopedia://search?q=".decodeQueryParameter() shouldBe "tokopedia://search?"
        "tokopedia://search?q=samsung&fcity=".decodeQueryParameter() shouldBe "tokopedia://search?q=samsung"
        "tokopedia://search?q=samsung&fcity=1,2#3".decodeQueryParameter() shouldBe "tokopedia://search?q=samsung&fcity=1%2C2%233"
        "tokopedia://search?q=samsung&fcity=1,2#3?".decodeQueryParameter() shouldBe "tokopedia://search?q=samsung&fcity=1%2C2%233%3F"
        "tokopedia://search?q=samsung&fcity=1,2#3?&".decodeQueryParameter() shouldBe "tokopedia://search?q=samsung&fcity=1%2C2%233%3F"
    }

    @Test
    fun `Test product list type reposition mapping`() {
        val productItemDataView = ProductItemDataView()
        productItemDataView.productListType = SearchConstant.ProductListType.VAR_REPOSITION
        val productListType = productItemDataView.getProductListTypeEnum()

        productListType shouldBe ProductCardModel.ProductListType.REPOSITION
    }

    @Test
    fun `Test product list type long image mapping`() {
        val productItemDataView = ProductItemDataView()
        productItemDataView.productListType = SearchConstant.ProductListType.VAR_LONG_IMG
        val productListType = productItemDataView.getProductListTypeEnum()

        productListType shouldBe ProductCardModel.ProductListType.LONG_IMAGE
    }

    @Test
    fun `Test product list type random value mapping`() {
        val productItemDataView = ProductItemDataView()
        productItemDataView.productListType = "any type"
        val productListType = productItemDataView.getProductListTypeEnum()

        productListType shouldBe ProductCardModel.ProductListType.CONTROL
    }

    @Test
    fun `Test product list type empty value mapping`() {
        val productItemDataView = ProductItemDataView()
        productItemDataView.productListType = ""
        val productListType = productItemDataView.getProductListTypeEnum()

        productListType shouldBe ProductCardModel.ProductListType.CONTROL
    }
}