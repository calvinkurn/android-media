package com.tokopedia.search.result.presentation.presenter.product

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.discovery.common.constants.SearchApiConst
import com.tokopedia.discovery.common.constants.SearchConstant.SaveLastFilter.INPUT_PARAMS
import com.tokopedia.filter.common.data.Filter
import com.tokopedia.filter.common.data.Option
import com.tokopedia.filter.common.data.SavedOption
import com.tokopedia.search.jsonToObject
import com.tokopedia.search.result.complete
import com.tokopedia.search.result.domain.model.LastFilterModel.LastFilter
import com.tokopedia.search.result.domain.model.SearchProductModel
import com.tokopedia.search.result.domain.usecase.savelastfilter.SaveLastFilterInput
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
    fun `update last filter will send param as update`() {
        val searchParameter = createSearchParameter()
        val savedOptionList = createSavedOptionList()

        `When update last filter`(searchParameter, savedOptionList)

        `Then verify save last filter is called`()

        val saveLastFilterInput = saveLastFilterRequestParam.getSaveLastFilterInput()
        `Then assert input params action`(saveLastFilterInput, SaveLastFilterInput.Update)
        `Then assert input params contains filters and params`(
            saveLastFilterInput,
            savedOptionList,
            searchParameter,
        )
    }

    private fun `When update last filter`(
        searchParameter: Map<String, Any>,
        savedOptionList: List<SavedOption>,
    ) {
        productListPresenter.updateLastFilter(searchParameter, savedOptionList)
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

    private fun `Then assert input params action`(
        saveLastFilterInput: SaveLastFilterInput,
        expectedAction: SaveLastFilterInput.Action,
    ) {
        assertThat(saveLastFilterInput.action, `is`(expectedAction.toString()))
    }

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
    fun `update last filter when applying filter will send param as create`() {
        val searchParameter = createSearchParameter()
        val option = Option(key = "dummy_key", value = "dummy_value", name = "dummy_name")
        val filter = Filter(title = "dummy_title", options = listOf(option))

        `When update last filter`(searchParameter, filter, option, true)

        `Then verify save last filter is called`()

        val saveLastFilterInput = saveLastFilterRequestParam.getSaveLastFilterInput()

        `Then assert input params action`(saveLastFilterInput, SaveLastFilterInput.Create)
        `Then assert input params contains filters and params`(
            saveLastFilterInput,
            listOf(SavedOption.create(option, listOf(filter))),
            searchParameter,
        )
    }

    private fun `When update last filter`(
        searchParameter: Map<String, Any>,
        filter: Filter,
        option: Option,
        isFilterApplied: Boolean,
    ) {
        productListPresenter.updateLastFilter(searchParameter, filter, option, isFilterApplied)
    }

    @Test
    fun `update last filter when removing filter will send param as delete`() {
        val searchParameter = createSearchParameter()
        val option = Option(key = "dummy_key", value = "dummy_value", name = "dummy_name")
        val filter = Filter(title = "dummy_title", options = listOf(option))

        `When update last filter`(searchParameter, filter, option, false)

        `Then verify save last filter is called`()

        val saveLastFilterInput = saveLastFilterRequestParam.getSaveLastFilterInput()

        `Then assert input params action`(saveLastFilterInput, SaveLastFilterInput.Delete)
        `Then assert input params contains filters and params`(
            saveLastFilterInput,
            listOf(SavedOption.create(option, listOf(filter))),
            searchParameter,
        )
    }

    @Test
    fun `show last filter in visitable list`() {
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
    ) {
        every {
            searchProductFirstPageUseCase.execute(any(), any())
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
}