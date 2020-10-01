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
import com.tokopedia.usecase.RequestParams
import io.mockk.*
import org.junit.Test
import rx.Subscriber
import java.util.*

private const val searchProductModelWithQuickFilter = "searchproduct/quickfilter/with-quick-filter.json"
private const val searchProductModelWithQuickFilterAndNoResult = "searchproduct/quickfilter/with-quick-filter-no-result.json"
private const val searchProductModelNoQuickFilter = "searchproduct/quickfilter/no-quick-filter.json"

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
            productListView.initFilterControllerForQuickFilter(capture(actualQuickFilterList))
            productListView.hideQuickFilterShimmering()
            productListView.setQuickFilter(capture(listItemSlot))
        }
    }

    private fun `Then verify filter list for init filter controller`(quickFilterList: List<Filter>) {
        val actualQuickFilterList = actualQuickFilterList.captured
        actualQuickFilterList.listShouldBe(quickFilterList) { actualFilter, expectedFilter ->
            actualFilter.title shouldBe expectedFilter.title
            actualFilter.templateName shouldBe expectedFilter.templateName

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
            sortFilterItem.title shouldBe filter.title
        }
    }

    private fun `Then verify option list from response`(expectedOptionList: List<Option>) {
        val optionList = productListPresenter.quickFilterOptionList

        assertOptionList(optionList, expectedOptionList)
    }

    private fun DataValue.getOptionList() = filter.map { it.options }.flatten()

    @Test
    fun `Search Product has No Quick Filter`() {
        setUp()

        val searchProductModel = searchProductModelNoQuickFilter.jsonToObject<SearchProductModel>()
        `Given Search Product API will return SearchProductModel`(searchProductModel)

        `When Load Data`()

        `Then verify new quick filter interactions`(createSearchProductDefaultQuickFilter())
    }

    @Test
    fun `Search Product should not init and show quick filter in empty search`() {
        val searchProductModel = searchProductModelWithQuickFilterAndNoResult.jsonToObject<SearchProductModel>()
        `Given Search Product API will return SearchProductModel`(searchProductModel)

        `When Load Data`()

        `Then verify new quick filter interactions for empty search`()
    }

    private fun `Then verify new quick filter interactions for empty search`() {
        verify(exactly = 0) {
            productListView.initFilterControllerForQuickFilter(capture(actualQuickFilterList))
            productListView.setQuickFilter(capture(listItemSlot))
        }

        verify {
            productListView.hideQuickFilterShimmering()
        }
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
            productListView.initFilterControllerForQuickFilter(any())
        }
    }
}