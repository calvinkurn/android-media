package com.tokopedia.search.utils

import com.tokopedia.discovery.common.constants.SearchApiConst
import com.tokopedia.search.shouldBe
import org.junit.Test

internal class SearchFilterUtilsTest {

    @Test
    fun `Get sort filter count`() {
        getSortFilterCount(mapOf(SearchApiConst.Q to "samsung")) shouldBe 0

        getSortFilterCount(mapOf(SearchApiConst.Q to "samsung", SearchApiConst.OFFICIAL to true)) shouldBe 1

        getSortFilterCount(mapOf(SearchApiConst.Q to "samsung", SearchApiConst.PMIN to 100_000)) shouldBe 1

        getSortFilterCount(mapOf(SearchApiConst.Q to "samsung", SearchApiConst.PMIN to 0)) shouldBe 0

        getSortFilterCount(mapOf(SearchApiConst.Q to "samsung", SearchApiConst.PMAX to 0)) shouldBe 0

        getSortFilterCount(mapOf(SearchApiConst.Q to "samsung", SearchApiConst.PMIN to 0, SearchApiConst.PMAX to 0)) shouldBe 0

        getSortFilterCount(
                mapOf(SearchApiConst.Q to "samsung", SearchApiConst.PMIN to 100_000, SearchApiConst.PMAX to 1_000_000)
        ) shouldBe 1

        getSortFilterCount(
                mapOf(SearchApiConst.Q to "samsung", SearchApiConst.OB to SearchApiConst.DEFAULT_VALUE_OF_PARAMETER_SORT)
        ) shouldBe 0

        getSortFilterCount(
                mapOf(SearchApiConst.Q to "samsung", SearchApiConst.OB to 3)
        ) shouldBe 1

        getSortFilterCount(
                mapOf(SearchApiConst.Q to "samsung", SearchApiConst.OFFICIAL to true, SearchApiConst.FCITY to "1,2,3#4,5")
        ) shouldBe 3
    }
}