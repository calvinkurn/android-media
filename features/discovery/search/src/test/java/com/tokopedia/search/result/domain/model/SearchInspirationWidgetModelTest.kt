package com.tokopedia.search.result.domain.model

import com.tokopedia.discovery.common.constants.SearchApiConst.Companion.PMAX
import com.tokopedia.discovery.common.constants.SearchApiConst.Companion.PMIN
import com.tokopedia.filter.common.data.Option
import com.tokopedia.search.jsonToObject
import com.tokopedia.search.result.domain.model.SearchProductModel.SearchInspirationWidget
import com.tokopedia.search.shouldBe
import org.hamcrest.core.Is.`is`
import org.hamcrest.core.IsCollectionContaining.hasItems
import org.junit.Assert.assertThat
import org.junit.Test

private const val SIZE_FILTER_JSON = "searchproduct/inspirationfilter/size.json"
private const val PRICE_RANGE_FILTER_JSON = "searchproduct/inspirationfilter/price-range.json"
private const val MULTI_FILTER_JSON = "searchproduct/inspirationfilter/multi-filters.json"

class SearchInspirationWidgetModelTest {

    @Test
    fun `inspiration widget as filter list`() {
        val searchInspirationWidget = SIZE_FILTER_JSON.jsonToObject<SearchInspirationWidget>()
        val inspirationWidgetOptionList = searchInspirationWidget.inspirationWidgetOptionList()

        val optionList = searchInspirationWidget.asFilterList()
            .map { it.options }
            .flatten()

        optionList.forEachIndexed { optionIndex, option ->
            val inspirationWidgetOption = inspirationWidgetOptionList[optionIndex].multiFilters!!.first()

            assertThat(option.key, `is`(inspirationWidgetOption.key))
            assertThat(option.name, `is`(inspirationWidgetOption.name))
            assertThat(option.value, `is`(inspirationWidgetOption.value))
        }
    }

    private fun SearchInspirationWidget.inspirationWidgetOptionList() =
        data.map { it.inspirationWidgetOptions }.flatten()

    @Test
    fun `inspiration widget with price range filter list`() {
        val searchInspirationWidget =
            PRICE_RANGE_FILTER_JSON.jsonToObject<SearchInspirationWidget>()

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

    private fun SearchInspirationWidget.inspirationWidgetOptionFilterList() = data
        .map {
            it.inspirationWidgetOptions
                .map { option -> option.multiFilters ?: emptyList() }
                .flatten()
        }
        .flatten()

    @Test
    fun `inspiration widget with multi filters filter list`() {
        val searchInspirationWidget = MULTI_FILTER_JSON.jsonToObject<SearchInspirationWidget>()
        val inspirationWidgetOptionFilters = searchInspirationWidget.inspirationWidgetOptionFilterList()

        val filterList = searchInspirationWidget.asFilterList()
            .map { it.options }
            .flatten()

        filterList.forEachIndexed { index, filter ->
            filter.assertOption(inspirationWidgetOptionFilters[index])
        }
    }

    private fun Option.assertOption(
        inspirationWidgetFilter: SearchProductModel.InspirationWidgetFilter,
    ) {
        key shouldBe inspirationWidgetFilter.key
        name shouldBe inspirationWidgetFilter.name
        value shouldBe inspirationWidgetFilter.value
        valMin shouldBe inspirationWidgetFilter.valMin
        valMax shouldBe inspirationWidgetFilter.valMax
    }
}
