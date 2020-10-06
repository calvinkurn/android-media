package com.tokopedia.search.utils

import com.tokopedia.discovery.common.constants.SearchApiConst
import com.tokopedia.search.shouldBe
import org.junit.Test

internal class UrlParamUtilsTest {

    private fun Map<String?, Any?>?.generateUrlParamStringAndValidate(expected: String) {
        val actual = UrlParamUtils.generateUrlParamString(this)

        if (actual != expected) {
            throw Exception("Test failed. Expected: $expected, Actual: $actual")
        }
    }

    @Test
    fun `Generate url param with null inputs`() {
        val inputMap = null

        inputMap.generateUrlParamStringAndValidate("")
    }

    @Test
    fun `Generate url param with empty map`() {
        val inputMap = hashMapOf<String?, Any?>()

        inputMap.generateUrlParamStringAndValidate("")
    }

    @Test
    fun `Generate url param string regular case`() {
        val inputMap = hashMapOf<String?, Any?>(
                "q" to "Samsung",
                "official" to true
        )

        inputMap.generateUrlParamStringAndValidate("q=Samsung&official=true")
    }

    @Test
    fun `Generate url param string with some null values`() {
        val inputMap = hashMapOf<String?, Any?>(
                "q" to "Samsung",
                "official" to null,
                "sc" to null
        )

        inputMap.generateUrlParamStringAndValidate("q=Samsung")
    }

    @Test
    fun `Generate url param string with null keys`() {
        val inputMap = hashMapOf<String?, Any?>(
                null to "Samsung",
                "official" to null,
                "sc" to 32
        )

        inputMap.generateUrlParamStringAndValidate("sc=32")
    }

    @Test
    fun `Generate url param string with spaces`() {
        val inputMap = hashMapOf<String?, Any?>(
                "q" to "samsung galaxy s8",
                "official" to true,
                "pmin" to 100000
        )

        inputMap.generateUrlParamStringAndValidate("q=samsung+galaxy+s8&official=true&pmin=100000")
    }

    @Test
    fun `Remove keys from url`() {
        UrlParamUtils.removeQueryParams(null, null) shouldBe ""
        UrlParamUtils.removeQueryParams("ssss", null) shouldBe "ssss"
        UrlParamUtils.removeQueryParams("tokopedia://search-autocomplete?", null) shouldBe "tokopedia://search-autocomplete"
        UrlParamUtils.removeQueryParams("tokopedia://search-autocomplete?q=sss", listOf("s")) shouldBe "tokopedia://search-autocomplete?q=sss"
        UrlParamUtils.removeQueryParams("tokopedia://search-autocomplete?q=sss", listOf("q")) shouldBe "tokopedia://search-autocomplete"
        UrlParamUtils.removeQueryParams("tokopedia://search-autocomplete?navsource=campaign&q=sss", listOf("navsource")) shouldBe "tokopedia://search-autocomplete?q=sss"
        UrlParamUtils.removeQueryParams(
                "tokopedia://search-autocomplete?navsource=campaign&q=sss&srp_page_id=160&otherkeys=othervalue",
                listOf("navsource", "srp_page_id")
        ) shouldBe "tokopedia://search-autocomplete?q=sss&otherkeys=othervalue"
        UrlParamUtils.removeQueryParams(
                "tokopedia://search-autocomplete?navsource=campaign&q=asus&navsource=campaign&srp_page_id=160&srp_page_title=Waktu%20Indonesia%20Belanja",
                listOf(SearchApiConst.NAVSOURCE, SearchApiConst.SRP_PAGE_ID, SearchApiConst.SRP_PAGE_TITLE)
        ) shouldBe "tokopedia://search-autocomplete?q=asus"
    }
}