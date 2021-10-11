package com.tokopedia.discovery.common.utils

import com.tokopedia.discovery.common.constants.SearchApiConst
import com.tokopedia.discovery.common.constants.SearchConstant
import org.junit.Test

internal class Dimension90Test {

    private fun Map<String, Any>.getDimension90AndValidate(expected: String) {
        val actual = Dimension90Utils.getDimension90(this)

        if (actual != expected) {
            throw Exception("Test failed. Expected: $expected, Actual: $actual")
        }
    }

    @Test
    fun `Get dimension90 for global search`() {
        val inputMap = hashMapOf<String, Any>(
                SearchApiConst.Q to "samsung"
        )

        inputMap.getDimension90AndValidate(SearchConstant.CustomDimension.DEFAULT_VALUE_CUSTOM_DIMENSION_90_GLOBAL)
    }

    @Test
    fun `Get dimension90 with for local search`() {
        val navSource = "clp"
        val pageTitle = "Waktu Indonesia Belanja"
        val pageId = "1234"
        val inputMap = hashMapOf<String, Any>(
                SearchApiConst.Q to "samsung",
                SearchApiConst.NAVSOURCE to navSource,
                SearchApiConst.SRP_PAGE_TITLE to pageTitle,
                SearchApiConst.SRP_PAGE_ID to pageId,
        )

        inputMap.getDimension90AndValidate("$pageTitle.$navSource.local_search.$pageId")
    }

    @Test
    fun `Get dimension90 with search ref`() {
        val searchRef = "homepage.4_banners_auto.2.1768.38550"
        val inputMap = hashMapOf<String, Any>(
                SearchApiConst.Q to "samsung",
                SearchApiConst.SEARCH_REF to searchRef,
        )

        inputMap.getDimension90AndValidate(searchRef)
    }
}