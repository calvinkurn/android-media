package com.tokopedia.search.result.presentation.presenter.product

import com.tokopedia.discovery.common.constants.SearchApiConst
import com.tokopedia.filter.common.data.DataValue
import com.tokopedia.filter.common.data.DynamicFilterModel
import com.tokopedia.filter.common.data.Filter
import com.tokopedia.filter.common.data.Option
import com.tokopedia.search.jsonToObject
import com.tokopedia.search.listShouldBe
import com.tokopedia.search.result.complete
import com.tokopedia.search.result.domain.model.SearchProductModel
import com.tokopedia.search.shouldBe
import com.tokopedia.search.utils.createSearchProductDefaultQuickFilter
import com.tokopedia.sortfilter.SortFilterItem
import com.tokopedia.unifycomponents.ChipsUnify
import com.tokopedia.usecase.RequestParams
import io.mockk.*
import org.junit.Test
import rx.Subscriber
import java.util.*

private const val searchProductModelWithQuickFilter = "searchproduct/quickfilter/with-quick-filter.json"
private const val searchProductModelWithQuickFilterAndNoResult = "searchproduct/quickfilter/with-quick-filter-no-result.json"
private const val searchProductModelNoQuickFilter = "searchproduct/quickfilter/no-quick-filter.json"
private const val searchProductModelWithMultipleOptionQuickFilter = "searchproduct/quickfilter/with-multiple-option-quick-filter.json"

internal class SearchProductHandleQuickFilterTest : ProductListPresenterTestFixtures() {
    private val requestParamsSlot = slot<RequestParams>()
    private val actualQuickFilterList = slot<List<Filter>>()
    private val listItemSlot = slot<ArrayList<SortFilterItem>>()

    @Test
    fun `Search Product has Quick Filter`() {
        val searchProductModel = searchProductModelWithQuickFilter.jsonToObject<SearchProductModel>()
        `Given Search Product API will return SearchProductModel`(searchProductModel)

        `When Load Data`()

        `Then verify new quick filter interactions`(searchProductModel.quickFilterModel)
    }

    private fun `Given Search Product API will return SearchProductModel`(searchProductModel: SearchProductModel) {
        every { searchProductFirstPageUseCase.execute(capture(requestParamsSlot), any()) }.answers {
            secondArg<Subscriber<SearchProductModel>>().complete(searchProductModel)
        }
    }

    private fun `When Load Data`() {
        presenterLoadData()
    }

    private fun `Given data has been loaded`() {
        presenterLoadData()
    }

    private fun presenterLoadData() {
        val searchParameter: Map<String, Any> = mutableMapOf<String, Any>().also {
            it[SearchApiConst.Q] = "samsung"
            it[SearchApiConst.START] = "0"
            it[SearchApiConst.UNIQUE_ID] = "unique_id"
            it[SearchApiConst.USER_ID] = productListPresenter.userId
        }
        productListPresenter.loadData(searchParameter)
    }

    private fun `Then verify new quick filter interactions`(quickFilterModel: DataValue) {
        `Then verify set quick filter is called`()
        `Then verify filter list for init filter controller`(quickFilterModel.filter)
        `Then verify SortFilterItem list`(quickFilterModel)
        `Then verify option list from response`(quickFilterModel.getOptionList())
    }

    private fun `Then verify set quick filter is called`() {
        verifyOrder {
            productListView.hideQuickFilterShimmering()
            productListView.initFilterController(capture(actualQuickFilterList))
            productListView.setQuickFilter(capture(listItemSlot))
        }
    }

    private fun `Then verify filter list for init filter controller`(quickFilterList: List<Filter>) {
        val actualQuickFilterList = actualQuickFilterList.captured
        actualQuickFilterList.listShouldBe(quickFilterList) { actualFilter, expectedFilter ->
            actualFilter.title shouldBe expectedFilter.title
            actualFilter.templateName shouldBe expectedFilter.templateName
            actualFilter.chipName shouldBe expectedFilter.chipName

            assertOptionList(actualFilter.options, expectedFilter.options)
        }
    }

    private fun assertOptionList(actualOptionList: List<Option>, expectedOptionList: List<Option>) {
        actualOptionList.listShouldBe(expectedOptionList) { actualOption, expectedOption ->
            actualOption.key shouldBe expectedOption.key
            actualOption.name shouldBe expectedOption.name
            actualOption.value shouldBe expectedOption.value
            actualOption.isNew shouldBe expectedOption.isNew
        }
    }

    private fun `Then verify SortFilterItem list`(quickFilterModel: DataValue) {
        val sortFilterItemList = listItemSlot.captured
        sortFilterItemList.listShouldBe(quickFilterModel.filter) { sortFilterItem, filter ->
            sortFilterItem.title shouldBe filter.chipName
        }
    }

    private fun `Then verify option list from response`(expectedOptionList: List<Option>) {
        val optionList = productListPresenter.quickFilterList.map { it.options }.flatten()

        assertOptionList(optionList, expectedOptionList)
    }

    private fun DataValue.getOptionList() = filter.map { it.options }.flatten()

    @Test
    fun `Search Product has No Quick Filter`() {
        val searchProductModel = searchProductModelNoQuickFilter.jsonToObject<SearchProductModel>()
        `Given Search Product API will return SearchProductModel`(searchProductModel)

        `When Load Data`()

        `Then verify new quick filter interactions`(createSearchProductDefaultQuickFilter())
    }

    @Test
    fun `Search Product should not init quick filter after opening filter page`() {
        val searchProductModel = searchProductModelWithQuickFilter.jsonToObject<SearchProductModel>()
        val dynamicFilterModel = "searchproduct/dynamicfilter/dynamic-filter-model-common.json".jsonToObject<DynamicFilterModel>()
        `Given Search Product API will return SearchProductModel`(searchProductModel)
        `Given get dynamic filter model API will success`(dynamicFilterModel)

        `When load data after opening filter page`()

        `Then verify filter controller only initialized once`()
    }

    private fun `Given get dynamic filter model API will success`(dynamicFilterModel: DynamicFilterModel) {
        every {
            getDynamicFilterUseCase.execute(any(), any())
        } answers {
            secondArg<Subscriber<DynamicFilterModel>>().complete(dynamicFilterModel)
        }
    }

    private fun `When load data after opening filter page`() {
        val searchParameter: Map<String, Any> = mutableMapOf<String, Any>().also {
            it[SearchApiConst.Q] = "samsung"
            it[SearchApiConst.START] = "0"
            it[SearchApiConst.UNIQUE_ID] = "unique_id"
            it[SearchApiConst.USER_ID] = productListPresenter.userId
        }
        productListPresenter.loadData(searchParameter)
        productListPresenter.openFilterPage(searchParameter)
        productListPresenter.loadData(searchParameter)
    }

    private fun `Then verify filter controller only initialized once`() {
        verify(exactly = 1) {
            productListView.initFilterController(any())
        }
    }

    @Test
    fun `Search Product has Multiple Option Quick Filter`() {
        val searchProductModel = searchProductModelWithMultipleOptionQuickFilter.jsonToObject<SearchProductModel>()
        `Given Search Product API will return SearchProductModel`(searchProductModel)

        `When Load Data`()

        `Then verify new quick filter interactions`(searchProductModel.quickFilterModel)
    }

    @Test
    fun `Open Dropdown Quick Filter`() {
        val searchProductModel = searchProductModelWithMultipleOptionQuickFilter.jsonToObject<SearchProductModel>()
        val selectedFilterIndex = 0
        val selectedFilter = searchProductModel.quickFilterModel.filter[selectedFilterIndex]
        `Given Search Product API will return SearchProductModel`(searchProductModel)
        `Given Set Quick Filter Called`()
        `Given data has been loaded`()

        `When Click Quick Filter Item`(selectedFilterIndex)

        `Then verify dropdown quick filter bottomsheet has opened and track sent`(selectedFilter)
    }

    @Test
    fun `Open Dropdown Quick Filter Using Chevron`() {
        val searchProductModel = searchProductModelWithMultipleOptionQuickFilter.jsonToObject<SearchProductModel>()
        val selectedFilterIndex = 0
        val selectedFilter = searchProductModel.quickFilterModel.filter[selectedFilterIndex]

        `Given Search Product API will return SearchProductModel`(searchProductModel)
        `Given Set Quick Filter Called`()
        `Given data has been loaded`()

        `When Click Quick Filter Using Chevron`(selectedFilterIndex)

        `Then verify dropdown quick filter bottomsheet has opened and track sent`(selectedFilter)
    }

    @Test
    fun `Click Quick Filter with Single Option`() {
        val searchProductModel = searchProductModelWithQuickFilter.jsonToObject<SearchProductModel>()
        val selectedFilterIndex = 0
        val selectedFilter = searchProductModel.quickFilterModel.filter[selectedFilterIndex]

        `Given Search Product API will return SearchProductModel`(searchProductModel)
        `Given Set Quick Filter Called`()
        `Given data has been loaded`()

        `When Click Quick Filter Item`(selectedFilterIndex)

        `Then verify quick filter clicked`(selectedFilter, selectedFilter.options.first())
    }

    @Test
    fun `Quick Filter Item Type Is Selected When There Is Active Filter`() {
        val searchProductModel = searchProductModelWithQuickFilter.jsonToObject<SearchProductModel>()
        val selectedFilterIndex = 0
        val selectedFilter = searchProductModel.quickFilterModel.filter[selectedFilterIndex]
        val selectedFilterOption = selectedFilter.options[0]

        `Given Search Product API will return SearchProductModel`(searchProductModel)
        `Given Set Quick Filter Called`()
        `Given Selected Option will return true`(selectedFilterOption)

        `When Load Data`()

        `Then Assert Quick Filter That Has Active Option Will Have Selected Type`(
            selectedFilterIndex,
            selectedFilterOption.name,
        )
    }

    @Test
    fun `Quick Filter Item Type Is Selected When There Is Multiple Active Options In A Single Filter`() {
        val searchProductModel = searchProductModelWithMultipleOptionQuickFilter.jsonToObject<SearchProductModel>()
        val selectedFilterIndex = 0
        val selectedFilter = searchProductModel.quickFilterModel.filter[selectedFilterIndex]
        val firstSelectedFilterOption = selectedFilter.options[0]
        val secondSelectedFilterOption = selectedFilter.options[1]
        val expectedChipName = "2 Jenis Toko"

        `Given Search Product API will return SearchProductModel`(searchProductModel)
        `Given Set Quick Filter Called`()
        `Given Selected Option will return true`(firstSelectedFilterOption)
        `Given Selected Option will return true`(secondSelectedFilterOption)

        `When Load Data`()

        `Then Assert Quick Filter That Has Active Option Will Have Selected Type`(
            selectedFilterIndex,
            expectedChipName,
        )
    }

    private fun `Then Assert Quick Filter That Has Active Option Will Have Selected Type`(
        selectedFilterIndex: Int,
        expectedChipName: String,
    ) {
        val sortFilterItemList = listItemSlot.captured

        assert(sortFilterItemList[selectedFilterIndex].type == ChipsUnify.TYPE_SELECTED)
        assert(sortFilterItemList[selectedFilterIndex].title == expectedChipName)
    }

    private fun `Given Selected Option will return true`(selectedOption: Option) {
        every {
            productListView.isFilterSelected(selectedOption)
        } answers { true }
    }

    private fun `Then verify quick filter clicked`(filter: Filter, option: Option) {
        verify {
            productListView.onQuickFilterSelected(filter, option, any())
        }
    }

    private fun `When Click Quick Filter Item`(selectedIndex: Int) {
        clickQuickFilter(selectedIndex)
    }

    private fun `Given Quick Filter Item Has Been Clicked`(selectedIndex: Int) {
        clickQuickFilter(selectedIndex)
    }

    private fun `Given Set Quick Filter Called`() {
        every {
            productListView.setQuickFilter(capture(listItemSlot))
        } just runs
    }

    private fun clickQuickFilter(selectedIndex: Int) {
        val sortFilterItemList = listItemSlot.captured

        sortFilterItemList[selectedIndex].listener()
    }

    private fun `When Click Quick Filter Using Chevron`(selectedIndex: Int) {
        val sortFilterItemList = listItemSlot.captured

        sortFilterItemList[selectedIndex].chevronListener?.invoke()
    }

    private fun `Then verify dropdown quick filter bottomsheet has opened and track sent`(filter: Filter) {
        verify {
            productListView.openBottomsheetMultipleOptionsQuickFilter(filter)
            productListView.trackEventClickDropdownQuickFilter(filter.title)
        }
    }

    @Test
    fun `Open Dropdown Quick Filter and Apply Option`() {
        val searchProductModel = searchProductModelWithMultipleOptionQuickFilter.jsonToObject<SearchProductModel>()
        val selectedQuickFilterIndex = 0
        val selectedFilter = searchProductModel.quickFilterModel.filter[selectedQuickFilterIndex]

        `Given Search Product API will return SearchProductModel`(searchProductModel)
        `Given Set Quick Filter Called`()
        `Given data has been loaded`()
        `Given Quick Filter Item Has Been Clicked`(selectedQuickFilterIndex)

        `When Apply Dropdown Quick Filter`(selectedFilter.options)

        `Then verify dropdown quick filter bottomsheet has applied and track sent`(selectedFilter.options)
    }

    private fun `When Apply Dropdown Quick Filter`(optionList: List<Option>?) {
        productListPresenter.onApplyDropdownQuickFilter(optionList)
    }

    private fun `Then verify dropdown quick filter bottomsheet has applied and track sent`(optionList: List<Option>) {
        verify {
            productListView.applyDropdownQuickFilter(optionList)
            productListView.trackEventApplyDropdownQuickFilter(optionList, any())
        }
    }
}
