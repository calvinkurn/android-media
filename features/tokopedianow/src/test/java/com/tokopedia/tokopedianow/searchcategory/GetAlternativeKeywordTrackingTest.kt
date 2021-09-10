package com.tokopedia.tokopedianow.searchcategory

import com.tokopedia.tokopedianow.searchcategory.analytics.SearchCategoryTrackingConst.Misc.NONE
import com.tokopedia.tokopedianow.searchcategory.domain.model.AceSearchProductModel.SearchProduct
import org.junit.Assert.assertThat
import org.junit.Test
import org.hamcrest.CoreMatchers.`is` as shouldBe

class GetAlternativeKeywordTrackingTest {

    private fun `Test get alternative keyword`(
        searchProductJSON: String,
        expectedAlternativeKeyword: String,
    ) {
        val model = "searchcategory/alternativekeyword/$searchProductJSON.json"
            .jsonToObject<SearchProduct>()

        val actualAlternativeKeyword = model.getAlternativeKeyword()

        assertThat(actualAlternativeKeyword, shouldBe(expectedAlternativeKeyword))
    }

    @Test
    fun `get alternative keyword with related response code`() {
        val searchProductJSON = "alternative-keyword-from-related"
        val expectedAlternativeKeyword =
            "baju batik solo bagus, baju kaos anak, baju anak, baju kaos, kaos anak"

        `Test get alternative keyword`(searchProductJSON, expectedAlternativeKeyword)
    }

    @Test
    fun `get alternative keyword with suggestion response code`() {
        val searchProductJSON = "alternative-keyword-from-suggestion"
        val expectedAlternativeKeyword = "psp"

        `Test get alternative keyword`(searchProductJSON, expectedAlternativeKeyword)
    }

    @Test
    fun `get alternative keyword without related or suggestion response code`() {
        val searchProductJSON = "alternative-keyword-invalid-response-code"
        val expectedAlternativeKeyword = NONE

        `Test get alternative keyword`(searchProductJSON, expectedAlternativeKeyword)
    }

    @Test
    fun `get alternative keyword with no related`() {
        val searchProductJSON = "alternative-keyword-no-related"
        val expectedAlternativeKeyword = NONE

        `Test get alternative keyword`(searchProductJSON, expectedAlternativeKeyword)
    }

    @Test
    fun `get alternative keyword with no suggestion`() {
        val searchProductJSON = "alternative-keyword-no-suggestion"
        val expectedAlternativeKeyword = NONE

        `Test get alternative keyword`(searchProductJSON, expectedAlternativeKeyword)
    }
}