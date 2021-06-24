package com.tokopedia.tokopedianow.search.presentation.viewmodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.discovery.common.constants.SearchApiConst
import com.tokopedia.filter.common.data.Filter
import com.tokopedia.filter.common.data.Option
import com.tokopedia.filter.newdynamicfilter.helper.OptionHelper
import com.tokopedia.tokopedianow.search.domain.model.SearchModel
import com.tokopedia.tokopedianow.searchcategory.data.getTokonowQueryParam
import com.tokopedia.tokopedianow.searchcategory.jsonToObject
import com.tokopedia.tokopedianow.searchcategory.presentation.model.QuickFilterDataView
import com.tokopedia.tokopedianow.searchcategory.presentation.model.SortFilterItemDataView
import com.tokopedia.unifycomponents.ChipsUnify
import com.tokopedia.usecase.RequestParams
import io.mockk.slot
import io.mockk.verify
import org.hamcrest.CoreMatchers.nullValue
import org.junit.Assert.assertThat
import org.junit.Test
import org.hamcrest.CoreMatchers.`is` as shouldBe

class SearchQuickFilterTest: SearchTestFixtures() {

    private val searchModel = "search/first-page-8-products.json".jsonToObject<SearchModel>()

    @Test
    fun `quick filter with same option as category filter should have exclude_ prefix in option key`() {
        val quickFilterWithCategoryOptionJSON =
                "search/quickfilter/quick-filter-contains-category-filter.json"
        val searchModel = quickFilterWithCategoryOptionJSON.jsonToObject<SearchModel>()

        `Given search view model`()
        `Given get search first page use case will be successful`(searchModel)

        `When view created`()

        `Then assert quick filter key has exclude prefix`()
    }

    private fun `Then assert quick filter key has exclude prefix`() {
        val quickFilterVisitable = searchViewModel.visitableListLiveData.value.getQuickFilterDataView()
        val actualQuickFilterKeyWithCategoryOption =
                quickFilterVisitable.quickFilterItemList[0].filter.options[0].key

        val hasExcludePrefix =
                actualQuickFilterKeyWithCategoryOption.startsWith(OptionHelper.EXCLUDE_PREFIX)

        assertThat(hasExcludePrefix, shouldBe(true))
    }

    @Test
    fun `when view created, quick filter type selected should be based on query params`() {
        val selectedFilterOption = searchModel.quickFilter.filter[2].options[0]
        val queryParamWithFilter = mapOf(
                SearchApiConst.Q to defaultKeyword,
                selectedFilterOption.key to selectedFilterOption.value,
        )

        `Given search view model`(queryParamWithFilter)
        `Given get search first page use case will be successful`(searchModel)

        `When view created`()

        val quickFilterVisitable = searchViewModel.visitableListLiveData.value.getQuickFilterDataView()
        `Then assert quick filter selected type`(selectedFilterOption, quickFilterVisitable)
    }

    private fun List<Visitable<*>>?.getQuickFilterDataView(): QuickFilterDataView {
        return this?.find { it is QuickFilterDataView } as? QuickFilterDataView ?:
                throw AssertionError("Quick filter data view not found")
    }

    private fun `Then assert quick filter selected type`(
            selectedFilterOption: Option,
            quickFilterVisitable: QuickFilterDataView,
    ) {
        quickFilterVisitable.quickFilterItemList.forEach {
            val failedReason = "Quick filter option \"${it.firstOption!!.key}\" type is incorrect."
            val expectedType = getExpectedChipsUnifyType(it.firstOption!!, selectedFilterOption)

            assertThat(failedReason, it.sortFilterItem.type, shouldBe(expectedType))
        }
    }

    private fun getExpectedChipsUnifyType(actualFilterOption: Option, selectedFilterOption: Option) =
        if (actualFilterOption == selectedFilterOption)
            ChipsUnify.TYPE_SELECTED
        else
            ChipsUnify.TYPE_NORMAL

    @Test
    fun `click quick filter to apply filter`() {
        val requestParamsSlot = slot<RequestParams>()
        `Given get search first page use case will be successful`(searchModel, requestParamsSlot)
        `Given view already created`()

        val quickFilterVisitable = searchViewModel.visitableListLiveData.value.getQuickFilterDataView()
        val selectedQuickFilter = quickFilterVisitable.quickFilterItemList[1]

        `When quick filter selected`(selectedQuickFilter)

        `Then verify get search first page is called twice`()
        `Then verify request params contains the applied quick filter`(
                requestParamsSlot.captured,
                selectedQuickFilter
        )
        `Then assert quick filter tracking`(selectedQuickFilter, true)
    }

    private fun `When quick filter selected`(selectedQuickFilter: SortFilterItemDataView) {
        selectedQuickFilter.sortFilterItem.listener()
    }

    private fun `Then verify get search first page is called twice`() {
        verify(exactly = 2) {
            getSearchFirstPageUseCase.cancelJobs()
            getSearchFirstPageUseCase.execute(any(), any(), any())
        }
    }

    private fun `Then verify request params contains the applied quick filter`(
            requestParams: RequestParams,
            selectedQuickFilter: SortFilterItemDataView
    ) {
        val selectedQuickFilterKey = selectedQuickFilter.firstOption!!.key
        val actualParamsValue = getTokonowQueryParam(requestParams)[selectedQuickFilterKey].toString()

        assertThat(actualParamsValue, shouldBe(selectedQuickFilter.firstOption!!.value))
    }

    private fun `Then assert quick filter tracking`(
            selectedQuickFilter: SortFilterItemDataView,
            isApplied: Boolean
    ) {
        val quickFilterTracking = searchViewModel.quickFilterTrackingLiveData.value!!

        assertThat(quickFilterTracking.first, shouldBe(selectedQuickFilter.firstOption))
        assertThat(quickFilterTracking.second, shouldBe(isApplied))
    }

    @Test
    fun `click quick filter to un-apply filter`() {
        val requestParamsSlot = slot<RequestParams>()
        val selectedQuickFilterIndex = 2
        val previouslySelectedFilterOption = searchModel.quickFilter.filter[selectedQuickFilterIndex].options[0]
        val queryParamWithFilter = mapOf(
                SearchApiConst.Q to defaultKeyword,
                previouslySelectedFilterOption.key to previouslySelectedFilterOption.value,
        )

        `Given search view model`(queryParamWithFilter)
        `Given get search first page use case will be successful`(searchModel, requestParamsSlot)
        `Given view already created`()

        val quickFilterVisitable = searchViewModel.visitableListLiveData.value.getQuickFilterDataView()
        val selectedQuickFilter = quickFilterVisitable.quickFilterItemList[selectedQuickFilterIndex]

        `When quick filter selected`(selectedQuickFilter)

        `Then verify get search first page is called twice`()
        `Then verify quick filter does not contain unapplied filter`(
                requestParamsSlot.captured,
                selectedQuickFilter
        )
        `Then assert quick filter tracking`(selectedQuickFilter, false)
    }

    private fun `Then verify quick filter does not contain unapplied filter`(
            requestParams: RequestParams,
            selectedQuickFilter: SortFilterItemDataView
    ) {
        val selectedQuickFilterKey = selectedQuickFilter.firstOption!!.key
        val actualParamsValue = requestParams.parameters[selectedQuickFilterKey]

        assertThat(actualParamsValue, nullValue())
    }

    @Test
    fun `click quick filter to open L3 filter bottom sheet`() {
        val requestParamsSlot = slot<RequestParams>()
        `Given get search first page use case will be successful`(searchModel, requestParamsSlot)
        `Given view already created`()

        val quickFilterVisitable = searchViewModel.visitableListLiveData.value.getQuickFilterDataView()
        val selectedQuickFilter = quickFilterVisitable.quickFilterItemList[0]

        `When quick filter selected`(selectedQuickFilter)

        `Then assert L3 Bottomsheet filter is open with filter`(selectedQuickFilter.filter)
    }

    private fun `Then assert L3 Bottomsheet filter is open with filter`(selectedFilter: Filter) {
        val isL3FilterPageOpen = searchViewModel.isL3FilterPageOpenLiveData.value

        assertThat(isL3FilterPageOpen, shouldBe(selectedFilter))
    }
}