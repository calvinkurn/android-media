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

        getSortFilterCount(
                mapOf(
                        SearchApiConst.Q to "samsung",
                        SearchApiConst.OFFICIAL to true,
                        SearchApiConst.SRP_PAGE_TITLE to "Waktu Indonesia Belanja",
                        SearchApiConst.SRP_PAGE_ID to "1234"
                )
        ) shouldBe 1

        getSortFilterCount(
                mapOf(
                        SearchApiConst.Q to "samsung",
                        SearchApiConst.OFFICIAL to true,
                        SearchApiConst.USER_LAT to "123.123",
                        SearchApiConst.USER_LONG to "123.123",
                        SearchApiConst.USER_ADDRESS_ID to "123444",
                )
        ) shouldBe 1
    }

    @Test
    fun `Get sort filter params string`() {
        getSortFilterParamsString(mapOf(SearchApiConst.Q to "samsung")) shouldBe ""

        getSortFilterParamsString(mapOf(SearchApiConst.Q to "samsung", SearchApiConst.OFFICIAL to true)) shouldBe
                "${SearchApiConst.OFFICIAL}=true"

        getSortFilterParamsString(mapOf(SearchApiConst.Q to "samsung", SearchApiConst.PMIN to 100_000)) shouldBe
                "${SearchApiConst.PMIN}=100000"

        getSortFilterParamsString(mapOf(SearchApiConst.Q to "samsung", SearchApiConst.PMIN to 0)) shouldBe
                "${SearchApiConst.PMIN}=0"

        getSortFilterParamsString(mapOf(SearchApiConst.Q to "samsung", SearchApiConst.PMAX to 0)) shouldBe
                "${SearchApiConst.PMAX}=0"

        getSortFilterParamsString(mapOf(SearchApiConst.Q to "samsung", SearchApiConst.PMIN to 0, SearchApiConst.PMAX to 0)) shouldBe
                "${SearchApiConst.PMIN}=0&${SearchApiConst.PMAX}=0"

        getSortFilterParamsString(
                mapOf(SearchApiConst.Q to "samsung", SearchApiConst.PMIN to 100_000, SearchApiConst.PMAX to 1_000_000)
        ) shouldBe "${SearchApiConst.PMIN}=100000&${SearchApiConst.PMAX}=1000000"

        getSortFilterParamsString(
                mapOf(SearchApiConst.Q to "samsung", SearchApiConst.OB to SearchApiConst.DEFAULT_VALUE_OF_PARAMETER_SORT)
        ) shouldBe "${SearchApiConst.OB}=${SearchApiConst.DEFAULT_VALUE_OF_PARAMETER_SORT}"

        getSortFilterParamsString(
                mapOf(SearchApiConst.Q to "samsung", SearchApiConst.OB to 3)
        ) shouldBe "${SearchApiConst.OB}=3"

        getSortFilterParamsString(
                mapOf(SearchApiConst.Q to "samsung", SearchApiConst.OFFICIAL to true, SearchApiConst.FCITY to "1,2,3#4,5")
        ) shouldBe "${SearchApiConst.OFFICIAL}=true&${SearchApiConst.FCITY}=1%2C2%2C3%234%2C5"

        getSortFilterParamsString(
                mapOf(
                        SearchApiConst.Q to "samsung",
                        SearchApiConst.OFFICIAL to true,
                        SearchApiConst.SRP_PAGE_TITLE to "Waktu Indonesia Belanja",
                        SearchApiConst.SRP_PAGE_ID to "1234"
                )
        ) shouldBe "${SearchApiConst.OFFICIAL}=true"

        getSortFilterParamsString(
                mapOf(
                        SearchApiConst.Q to "samsung",
                        SearchApiConst.OFFICIAL to true,
                        SearchApiConst.USER_LAT to "123.123",
                        SearchApiConst.USER_LONG to "123.123",
                        SearchApiConst.USER_ADDRESS_ID to "123444",
                )
        ) shouldBe "${SearchApiConst.OFFICIAL}=true"
    }

    @Test
    fun `Get Filter params map`() {
        getFilterParams(mapOf(SearchApiConst.Q to "samsung")) shouldBe mapOf<String, String>()

        getFilterParams(mapOf(SearchApiConst.Q to "samsung", SearchApiConst.OFFICIAL to true.toString())) shouldBe
                mapOf(SearchApiConst.OFFICIAL to true.toString())

        getFilterParams(mapOf(SearchApiConst.Q to "samsung", SearchApiConst.PMIN to 100_000.toString())) shouldBe
                mapOf(SearchApiConst.PMIN to 100_000.toString())

        getFilterParams(mapOf(SearchApiConst.Q to "samsung", SearchApiConst.PMIN to 0.toString())) shouldBe
                mapOf(SearchApiConst.PMIN to 0.toString())

        getFilterParams(mapOf(SearchApiConst.Q to "samsung", SearchApiConst.PMAX to 0.toString())) shouldBe
                mapOf(SearchApiConst.PMAX to 0.toString())

        getFilterParams(
                mapOf(SearchApiConst.Q to "samsung", SearchApiConst.PMIN to 0.toString(), SearchApiConst.PMAX to 0.toString())
        ) shouldBe mapOf(SearchApiConst.PMIN to 0.toString(), SearchApiConst.PMAX to 0.toString())

        getFilterParams(
                mapOf(SearchApiConst.Q to "samsung", SearchApiConst.PMIN to 100_000.toString(), SearchApiConst.PMAX to 1_000_000.toString())
        ) shouldBe mapOf(SearchApiConst.PMIN to 100_000.toString(), SearchApiConst.PMAX to 1_000_000.toString())

        getFilterParams(
                mapOf(SearchApiConst.Q to "samsung", SearchApiConst.OB to SearchApiConst.DEFAULT_VALUE_OF_PARAMETER_SORT)
        ) shouldBe mapOf<String, String>()

        getFilterParams(
                mapOf(SearchApiConst.Q to "samsung", SearchApiConst.OB to 3.toString())
        ) shouldBe mapOf<String, String>()

        getFilterParams(
                mapOf(SearchApiConst.Q to "samsung", SearchApiConst.OFFICIAL to true.toString(), SearchApiConst.FCITY to "1,2,3#4,5")
        ) shouldBe mapOf(SearchApiConst.OFFICIAL to true.toString(), SearchApiConst.FCITY to "1,2,3#4,5")

        getFilterParams(
                mapOf(
                        SearchApiConst.Q to "samsung",
                        SearchApiConst.OFFICIAL to true.toString(),
                        SearchApiConst.SRP_PAGE_TITLE to "Waktu Indonesia Belanja",
                        SearchApiConst.SRP_PAGE_ID to "1234"
                )
        ) shouldBe mapOf(SearchApiConst.OFFICIAL to true.toString())

        getFilterParams(
                mapOf(
                        SearchApiConst.Q to "samsung",
                        SearchApiConst.OFFICIAL to true.toString(),
                        SearchApiConst.USER_LAT to "123.123",
                        SearchApiConst.USER_LONG to "123.123",
                        SearchApiConst.USER_ADDRESS_ID to "123444",
                )
        ) shouldBe mapOf(SearchApiConst.OFFICIAL to true.toString())
    }
}