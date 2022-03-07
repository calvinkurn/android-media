package com.tokopedia.search.result.presentation.presenter.product

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.discovery.common.constants.SearchApiConst
import com.tokopedia.discovery.common.constants.SearchConstant.SaveLastFilter.INPUT_PARAMS
import com.tokopedia.discovery.common.constants.SearchConstant.SearchProduct.SEARCH_PRODUCT_SKIP_GET_LAST_FILTER_WIDGET
import com.tokopedia.filter.common.data.SavedOption
import com.tokopedia.search.jsonToObject
import com.tokopedia.search.result.complete
import com.tokopedia.search.result.domain.model.LastFilterModel
import com.tokopedia.search.result.domain.model.LastFilterModel.LastFilter
import com.tokopedia.search.result.domain.model.SearchProductModel
import com.tokopedia.search.result.domain.usecase.savelastfilter.SaveLastFilterInput
import com.tokopedia.search.result.error
import com.tokopedia.search.result.presentation.model.LastFilterDataView
import com.tokopedia.search.utils.UrlParamUtils
import com.tokopedia.usecase.RequestParams
import io.mockk.CapturingSlot
import io.mockk.every
import io.mockk.slot
import io.mockk.verify
import org.hamcrest.CoreMatchers.allOf
import org.hamcrest.CoreMatchers.containsString
import org.hamcrest.CoreMatchers.instanceOf
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.Is.`is`
import org.hamcrest.core.IsCollectionContaining.hasItem
import org.junit.Test
import rx.Subscriber

internal class SearchProductLastFilterTest: ProductListPresenterTestFixtures() {

    private val saveLastFilterRequestParamSlot = slot<RequestParams>()
    private val saveLastFilterRequestParam by lazy { saveLastFilterRequestParamSlot.captured }

    @Test
    fun `update last filter will send search param and saved option list`() {
        val searchParameter = createSearchParameter()
        val savedOptionList = createSavedOptionList()

        `When update last filter`(searchParameter, savedOptionList)

        `Then verify save last filter is called`()

        val saveLastFilterInput = saveLastFilterRequestParam.getSaveLastFilterInput()
        `Then assert input params contains filters and params`(
            saveLastFilterInput,
            savedOptionList,
            searchParameter,
        )
    }

    private fun createSearchParameter() = mapOf<String, Any>(
        SearchApiConst.Q to "samsung",
        SearchApiConst.OFFICIAL to true,
    )

    private fun createSavedOptionList() = listOf(
        createDummySavedOption(1),
        createDummySavedOption(2),
        createDummySavedOption(3),
    )

    private fun createDummySavedOption(suffix: Int) =
        SavedOption(
            title = "dummy_title_$suffix",
            key = "dummy_title_$suffix",
            value = "dummy_title_$suffix",
            name = "dummy_title_$suffix",
        )

    private fun `When update last filter`(
        searchParameter: Map<String, Any>,
        savedOptionList: List<SavedOption>,
    ) {
        productListPresenter.updateLastFilter(searchParameter, savedOptionList)
    }

    private fun `Then verify save last filter is called`() {
        verify {
            saveLastFilterUseCase.execute(
                capture(saveLastFilterRequestParamSlot),
                not(isNull())
            )
        }
    }

    private fun RequestParams.getSaveLastFilterInput() =
        parameters[INPUT_PARAMS] as SaveLastFilterInput

    private fun `Then assert input params contains filters and params`(
        saveLastFilterInput: SaveLastFilterInput,
        savedOptionList: List<SavedOption>,
        searchParameter: Map<String, Any>,
    ) {
        assertThat(saveLastFilterInput.lastFilter, `is`(savedOptionList))

        val mapParameter = UrlParamUtils.getParamMap(saveLastFilterInput.param)
        searchParameter.forEach { (key, value) ->
            assertThat(mapParameter[key], `is`(value.toString()))
        }
    }

    @Test
    fun `update last filter exceptions will be caught and ignored`() {
        `Given save last filter will throw exceptions`()

        `When update last filter`(createSearchParameter(), createSavedOptionList())
    }

    private fun `Given save last filter will throw exceptions`() {
        every {
            saveLastFilterUseCase.execute(any(), any())
        } answers {
            secondArg<Subscriber<Int>>().error(Exception("Ignored exception"))
        }
    }

    @Test
    fun `show last filter view in visitable list`() {
        val searchProductModel = "searchproduct/lastfilter/last-filter.json"
            .jsonToObject<SearchProductModel>()
        val visitableListSlot = slot<List<Visitable<*>>>()
        val visitableList by lazy { visitableListSlot.captured }

        `Given search product will return search product model`(searchProductModel)

        `When load data`()

        `Then verify view will set product list`(visitableListSlot)
        `Then assert visitable list has last filter data view`(visitableList)
        `Then assert last filter data view`(visitableList, searchProductModel.lastFilter)
    }

    private fun `Given search product will return search product model`(
        searchProductModel: SearchProductModel,
        requestParamsSlot: CapturingSlot<RequestParams> = slot(),
    ) {
        every {
            searchProductFirstPageUseCase.execute(capture(requestParamsSlot), any())
        } answers {
            secondArg<Subscriber<SearchProductModel>>().complete(searchProductModel)
        }
    }

    private fun `When load data`() {
        productListPresenter.loadData(mapOf())
    }

    private fun `Then verify view will set product list`(
        visitableListSlot: CapturingSlot<List<Visitable<*>>>,
    ) {
        verify {
            productListView.setProductList(capture(visitableListSlot))
        }
    }

    private fun `Then assert visitable list has last filter data view`(
        visitableList: List<Visitable<*>>,
    ) {
        assertThat(
            visitableList,
            hasItem(instanceOf<LastFilterDataView>(LastFilterDataView::class.java))
        )
    }

    private fun `Then assert last filter data view`(
        visitableList: List<Visitable<*>>,
        lastFilter: LastFilter,
    ) {
        val lastFilterDataView = visitableList.filterIsInstance<LastFilterDataView>().first()

        assertThat(lastFilterDataView.filterList, `is`(lastFilter.data.filters))
        assertThat(lastFilterDataView.title, `is`(lastFilter.data.title))

        val allOptionNameMatchers = lastFilter.data.filters.map { containsString(it.name) }
        assertThat(lastFilterDataView.optionNames(), allOf(allOptionNameMatchers))
    }

    @Test
    fun `do not get last filter from API if view has active filter`() {
        val requestParamsSlot = slot<RequestParams>()

        `Given search product will return search product model`(
            SearchProductModel(),
            requestParamsSlot
        )
        `Given view has active filter`()

        `When load data`()

        `Then assert request params will skip get last filter`(requestParamsSlot.captured)
    }

    private fun `Given view has active filter`() {
        every { productListView.isAnyFilterActive } returns true
    }

    private fun `Then assert request params will skip get last filter`(requestParams: RequestParams) {
        val isSkipGetLastFilter = requestParams.parameters[SEARCH_PRODUCT_SKIP_GET_LAST_FILTER_WIDGET]

        assertThat(isSkipGetLastFilter, `is`(true))
    }

    @Test
    fun `do not get last filter from API if view has active sort`() {
        val requestParamsSlot = slot<RequestParams>()

        `Given search product will return search product model`(
            SearchProductModel(),
            requestParamsSlot
        )
        `Given view has active sort`()

        `When load data`()

        `Then assert request params will skip get last filter`(requestParamsSlot.captured)
    }

    private fun `Given view has active sort`() {
        every { productListView.isAnySortActive } returns true
    }

    @Test
    fun `close last filter will delete all saved filter`() {
        val searchParameter = createSearchParameter()
        val searchProductModel = "searchproduct/lastfilter/last-filter.json"
            .jsonToObject<SearchProductModel>()

        `Given search product will return search product model`(searchProductModel)
        `Given view load data and shown last filter`(searchParameter)

        `When user close last filter`(searchParameter)

        `Then verify save last filter is called`()

        val saveLastFilterInput = saveLastFilterRequestParam.getSaveLastFilterInput()

        `Then assert input params contains filters and params`(
            saveLastFilterInput,
            listOf(),
            searchParameter,
        )
    }

    private fun `Given view load data and shown last filter`(searchParameter: Map<String, Any>) {
        productListPresenter.loadData(searchParameter)
    }

    private fun `When user close last filter`(searchParameter: Map<String, Any>) {
        productListPresenter.closeLastFilter(searchParameter)
    }

    @Test
    fun `category id l2 in save last filter param is taken from get last filter`() {
        val searchParameter = createSearchParameter()
        val searchProductModel = "searchproduct/lastfilter/last-filter.json"
            .jsonToObject<SearchProductModel>()
        val savedOptionList = createSavedOptionList()

        `Given search product will return search product model`(searchProductModel)
        `Given view load data and shown last filter`(searchParameter)

        `When update last filter`(searchParameter, savedOptionList)

        `Then verify save last filter is called`()

        val saveLastFilterInput = saveLastFilterRequestParam.getSaveLastFilterInput()
        `Then assert category id L2 param is taken from get last filter`(
            saveLastFilterInput,
            searchProductModel.lastFilter.data,
        )
    }

    private fun `Then assert category id L2 param is taken from get last filter`(
        saveLastFilterInput: SaveLastFilterInput,
        lastFilterData: LastFilterModel.Data,
    ) {
        assertThat(
            saveLastFilterInput.categoryIdL2,
            `is`(lastFilterData.categoryIdL2)
        )
    }
}