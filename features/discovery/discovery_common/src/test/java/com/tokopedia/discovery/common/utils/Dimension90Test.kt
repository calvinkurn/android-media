package com.tokopedia.discovery.common.utils

import com.tokopedia.discovery.common.constants.SearchApiConst
import com.tokopedia.discovery.common.constants.SearchConstant.CustomDimension.DEFAULT_VALUE_CUSTOM_DIMENSION_90_GLOBAL
import com.tokopedia.discovery.common.constants.SearchConstant.CustomDimension.DEFAULT_VALUE_CUSTOM_DIMENSION_90_GLOBAL_MPS
import com.tokopedia.discovery.common.constants.SearchConstant.CustomDimension.DEFAULT_VALUE_CUSTOM_DIMENSION_90_GLOBAL_SHOP
import com.tokopedia.discovery.common.constants.SearchConstant.CustomDimension.DIMENSION_90_GLOBAL_MPS
import com.tokopedia.discovery.common.utils.Dimension90Utils.LOCAL_SEARCH
import com.tokopedia.discovery.common.utils.Dimension90Utils.NONE
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
        val inputMap = mapOf<String, Any>(
            SearchApiConst.Q to "samsung"
        )

        inputMap.getDimension90AndValidate(DEFAULT_VALUE_CUSTOM_DIMENSION_90_GLOBAL)
    }

    @Test
    fun `Get dimension90 with for local search`() {
        val navSource = "clp"
        val pageTitle = "Waktu Indonesia Belanja"
        val pageId = "1234"
        val inputMap = mapOf<String, Any>(
            SearchApiConst.Q to "samsung",
            SearchApiConst.NAVSOURCE to navSource,
            SearchApiConst.SRP_PAGE_TITLE to pageTitle,
            SearchApiConst.SRP_PAGE_ID to pageId,
        )

        inputMap.getDimension90AndValidate("$pageTitle.$navSource.$LOCAL_SEARCH.$pageId")
    }

    @Test
    fun `Get dimension90 with for mps search`() {
        val inputMap = mapOf<String, Any>(
            SearchApiConst.Q1 to "samsung",
            SearchApiConst.Q2 to "apple",
            SearchApiConst.ACTIVE_TAB to SearchApiConst.ACTIVE_TAB_MPS,
        )

        inputMap.getDimension90AndValidate(DEFAULT_VALUE_CUSTOM_DIMENSION_90_GLOBAL_MPS)
    }

    @Test
    fun `Get dimension90 with for mps search with navsource`() {
        val navSource = "home"
        val inputMap = mapOf<String, Any>(
            SearchApiConst.Q1 to "samsung",
            SearchApiConst.Q2 to "apple",
            SearchApiConst.ACTIVE_TAB to SearchApiConst.ACTIVE_TAB_MPS,
            SearchApiConst.NAVSOURCE to navSource,
        )

        inputMap.getDimension90AndValidate("none.$navSource.$DIMENSION_90_GLOBAL_MPS.none")
    }

    @Test
    fun `Get dimension90 with for shop search`() {
        val inputMap = mapOf<String, Any>(
            SearchApiConst.Q to "samsung",
            SearchApiConst.ACTIVE_TAB to SearchApiConst.ACTIVE_TAB_SHOP,
        )

        inputMap.getDimension90AndValidate(DEFAULT_VALUE_CUSTOM_DIMENSION_90_GLOBAL_SHOP)
    }

    @Test
    fun `Get dimension90 with search ref`() {
        val searchRef = "homepage.4_banners_auto.2.1768.38550"
        val inputMap = mapOf<String, Any>(
            SearchApiConst.Q to "samsung",
            SearchApiConst.SEARCH_REF to searchRef,
        )

        inputMap.getDimension90AndValidate(searchRef)
    }

    @Test
    fun `Get dimension90 for tokonow will be considered as local search`() {
        val navSourceTokonow = SearchApiConst.DEFAULT_VALUE_OF_NAVSOURCE_TOKONOW
        val inputMap = mapOf<String, Any>(
            SearchApiConst.Q to "samsung",
            SearchApiConst.NAVSOURCE to SearchApiConst.DEFAULT_VALUE_OF_NAVSOURCE_TOKONOW,
        )

        val expectedDimension90 = "$NONE.$navSourceTokonow.$LOCAL_SEARCH.$NONE"
        inputMap.getDimension90AndValidate(expectedDimension90)
    }

    @Test
    fun `Get dimension90 for tokonow directory will be considered as local search`() {
        val navSourceTokonowDirectory = "tokonow_directory"
        val inputMap = mapOf<String, Any>(
            SearchApiConst.Q to "samsung",
            SearchApiConst.NAVSOURCE to navSourceTokonowDirectory,
        )

        val expectedDimension90 = "$NONE.$navSourceTokonowDirectory.$LOCAL_SEARCH.$NONE"
        inputMap.getDimension90AndValidate(expectedDimension90)
    }
}
