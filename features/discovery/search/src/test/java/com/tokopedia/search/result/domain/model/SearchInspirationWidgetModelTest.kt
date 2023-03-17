package com.tokopedia.search.result.domain.model

import com.tokopedia.discovery.common.constants.SearchApiConst.Companion.PMAX
import com.tokopedia.discovery.common.constants.SearchApiConst.Companion.PMIN
import com.tokopedia.filter.common.data.Option
import com.tokopedia.search.jsonToObject
import com.tokopedia.search.result.domain.model.SearchProductModel.SearchInspirationWidget
import org.hamcrest.core.Is.`is`
import org.hamcrest.core.IsCollectionContaining.hasItems
import org.junit.Assert.assertThat
import org.junit.Test

private const val SIZE_FILTER_JSON = "searchproduct/inspirationfilter/size.json"
private const val PRICE_RANGE_FILTER_JSON = "searchproduct/inspirationfilter/price-range.json"

class SearchInspirationWidgetModelTest {

    @Test
    fun `inspiration widget as filter list`() {
        val searchInspirationWidget = SIZE_FILTER_JSON.jsonToObject<SearchInspirationWidget>()
        val inspirationWidgetOptionList = searchInspirationWidget.inspirationWidgetOptionList()

        val optionList = searchInspirationWidget.asFilterList()
            .map { it.options }
            .flatten()

        optionList.forEachIndexed { optionIndex, option ->
            val inspirationWidgetOption = inspirationWidgetOptionList[optionIndex].filters

            assertThat(option.key, `is`(inspirationWidgetOption.key))
            assertThat(option.name, `is`(inspirationWidgetOption.name))
            assertThat(option.value, `is`(inspirationWidgetOption.value))
        }
    }

    private fun SearchInspirationWidget.inspirationWidgetOptionList() =
        data.map { it.inspirationWidgetOptions }.flatten()

    @Test
    fun `inspiration widget with price range filter list`() {
        val searchInspirationWidget = PRICE_RANGE_FILTER_JSON.jsonToObject<SearchInspirationWidget>()

        val optionList = searchInspirationWidget.asFilterList()
            .map { it.options }
            .flatten()

        val expectedPriceRangeOptionList = listOf(
            Option(key = PMIN, value = "", name = ""),
            Option(key = PMAX, value = "", name = "")
        )

        assertThat(
            optionList,
            hasItems(*expectedPriceRangeOptionList.toTypedArray())
        )
    }
}
