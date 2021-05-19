package com.tokopedia.tokomart.search.presentation.viewmodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.discovery.common.constants.SearchApiConst
import com.tokopedia.filter.common.data.Option
import com.tokopedia.tokomart.search.domain.model.SearchModel
import com.tokopedia.tokomart.searchcategory.data.getTokonowQueryParam
import com.tokopedia.tokomart.searchcategory.jsonToObject
import com.tokopedia.tokomart.searchcategory.presentation.model.QuickFilterDataView
import com.tokopedia.tokomart.searchcategory.presentation.model.SortFilterItemDataView
import com.tokopedia.unifycomponents.ChipsUnify
import com.tokopedia.usecase.RequestParams
import io.mockk.CapturingSlot
import io.mockk.slot
import io.mockk.verify
import org.hamcrest.CoreMatchers.nullValue
import org.junit.Assert.assertThat
import org.junit.Test
import org.hamcrest.CoreMatchers.`is` as shouldBe

class SearchQuickFilterTest: SearchTestFixtures() {

    private val searchModel = "search/first-page-8-products.json".jsonToObject<SearchModel>()

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
            val failedReason = "Quick filter option \"${it.option.key}\" type is incorrect."
            val expectedType = getExpectedChipsUnifyType(it.option, selectedFilterOption)

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
        val selectedQuickFilterKey = selectedQuickFilter.option.key
        val actualParamsValue = getTokonowQueryParam(requestParams)[selectedQuickFilterKey].toString()

        assertThat(actualParamsValue, shouldBe(selectedQuickFilter.option.value))
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
    }

    private fun `Then verify quick filter does not contain unapplied filter`(
            requestParams: RequestParams,
            selectedQuickFilter: SortFilterItemDataView
    ) {
        val selectedQuickFilterKey = selectedQuickFilter.option.key
        val actualParamsValue = requestParams.parameters[selectedQuickFilterKey]

        assertThat(actualParamsValue, nullValue())
    }
}