package com.tokopedia.discovery.common.utils

import com.tokopedia.discovery.common.constants.SearchApiConst
import org.junit.Assert.assertThat
import org.junit.Test
import org.hamcrest.core.Is.`is` as shouldBe

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
    fun `Remove keys from query params`() {
        assertThat(UrlParamUtils.removeKeysFromQueryParams(null, null), shouldBe(""))
        assertThat(UrlParamUtils.removeKeysFromQueryParams("ssss", null), shouldBe("ssss"))
        assertThat(UrlParamUtils.removeKeysFromQueryParams("", null), shouldBe(""))
        assertThat(UrlParamUtils.removeKeysFromQueryParams("q=sss", listOf("s")), shouldBe("q=sss"))
        assertThat(UrlParamUtils.removeKeysFromQueryParams("q=sss", listOf("q")), shouldBe(""))
        assertThat(UrlParamUtils.removeKeysFromQueryParams("navsource=campaign&q=sss", listOf("navsource")), shouldBe("q=sss"))
        assertThat(UrlParamUtils.removeKeysFromQueryParams(
                "navsource=campaign&q=sss&srp_page_id=160&otherkeys=othervalue",
                listOf("navsource", "srp_page_id")
        ), shouldBe("q=sss&otherkeys=othervalue"))
        assertThat(UrlParamUtils.removeKeysFromQueryParams(
                "navsource=campaign&q=asus&navsource=campaign&srp_page_id=160&srp_page_title=Waktu%20Indonesia%20Belanja",
                listOf(SearchApiConst.NAVSOURCE, SearchApiConst.SRP_PAGE_ID, SearchApiConst.SRP_PAGE_TITLE)
        ), shouldBe("q=asus"))
    }

    @Test
    fun `Get query params from url`() {
        assertThat(UrlParamUtils.getQueryParams(null), shouldBe(""))
        assertThat(UrlParamUtils.getQueryParams(""), shouldBe(""))
        assertThat(UrlParamUtils.getQueryParams("xxx"), shouldBe(""))
        assertThat(UrlParamUtils.getQueryParams("?xxx"), shouldBe("xxx"))
    }
}