package com.tokopedia.tokopedianow.searchcategory

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.filter.common.data.Option
import com.tokopedia.filter.newdynamicfilter.helper.OptionHelper
import com.tokopedia.tokopedianow.searchcategory.data.getTokonowQueryParam
import com.tokopedia.tokopedianow.searchcategory.presentation.model.CategoryFilterDataView
import com.tokopedia.tokopedianow.searchcategory.presentation.model.EmptyProductDataView
import com.tokopedia.tokopedianow.searchcategory.presentation.model.QuickFilterDataView
import com.tokopedia.tokopedianow.searchcategory.presentation.model.SortFilterItemDataView
import com.tokopedia.tokopedianow.searchcategory.presentation.viewmodel.BaseSearchCategoryViewModel
import com.tokopedia.usecase.RequestParams
import org.hamcrest.CoreMatchers.instanceOf
import org.hamcrest.CoreMatchers.nullValue
import org.junit.Assert.assertThat
import org.hamcrest.CoreMatchers.`is` as shouldBe

class EmptyProductTestHelper(
        private val baseViewModel: BaseSearchCategoryViewModel,
        private val callback: Callback,
) {

    fun `empty product list should show empty product view`() {
        callback.`Given first page product list is empty`()

        `When view created`()

        `Then assert empty result visitable list`()
        `Then assert header background is hidden`()
    }

    private fun `When view created`() {
        baseViewModel.onViewCreated()
    }

    private fun `Then assert empty result visitable list`() {
        val visitableList = baseViewModel.visitableListLiveData.value!!

        visitableList.first().assertChooseAddressDataView()
        visitableList.last().assertEmptyProductDataView()
    }

    private fun Visitable<*>.assertEmptyProductDataView() {
        assertThat(this, instanceOf(EmptyProductDataView::class.java))
    }

    private fun `Then assert header background is hidden`() {
        assertThat(baseViewModel.isHeaderBackgroundVisibleLiveData.value, shouldBe(false))
    }

    fun `empty product list because of filter should show filter list`() {
        callback.`Given first page product list will be successful`()
        `Given view already created`()
        callback.`Given first page product list is empty`()

        val visitableList = baseViewModel.visitableListLiveData.value!!
        val quickFilterDataView = visitableList.filterIsInstance<QuickFilterDataView>().first()
        val quickFilterItemList = quickFilterDataView.quickFilterItemList
        val chosenQuickFilter = quickFilterItemList[1]

        `When view apply quick filter`(chosenQuickFilter)

        val newVisitableList = baseViewModel.visitableListLiveData.value!!
        val emptyStateDataView = newVisitableList.filterIsInstance<EmptyProductDataView>().first()

        `Then assert chosen quick filter is in empty state data view`(
                emptyStateDataView,
                listOf(chosenQuickFilter.firstOption),
        )
    }

    private fun `Given view already created`() {
        baseViewModel.onViewCreated()
    }

    private fun `When view apply quick filter`(chosenQuickFilter: SortFilterItemDataView) {
        chosenQuickFilter.sortFilterItem.listener.invoke()
    }

    private fun `Then assert chosen quick filter is in empty state data view`(
            emptyStateDataView: EmptyProductDataView,
            expectedOptionList: List<Option?>,
    ) {
        assertThat(emptyStateDataView.activeFilterList, shouldBe(expectedOptionList))
    }

    fun `empty state remove filter`() {
        callback.`Given first page product list will be successful`()
        `Given view already created`()

        val visitableList = baseViewModel.visitableListLiveData.value!!
        val quickFilterDataView = visitableList.filterIsInstance<QuickFilterDataView>().first()
        val quickFilterItemList = quickFilterDataView.quickFilterItemList
        val chosenQuickFilter = quickFilterItemList[1]

        callback.`Given first page product list is empty`()
        `Given view apply quick filter`(chosenQuickFilter)

        val removedOption = chosenQuickFilter.firstOption!!
        `When view remove filter from empty state`(removedOption)

        val requestParamsSlot = mutableListOf<RequestParams>()
        val requestParams by lazy { requestParamsSlot.last() }

        callback.`Then verify first page API is called`(3, requestParamsSlot)
        `Then assert request params does not contain removed filter`(requestParams, removedOption)
    }

    private fun `Given view apply quick filter`(chosenQuickFilter: SortFilterItemDataView) {
        chosenQuickFilter.sortFilterItem.listener.invoke()
    }

    private fun `When view remove filter from empty state`(removedOption: Option) {
        baseViewModel.onViewRemoveFilter(removedOption)
    }

    private fun `Then assert request params does not contain removed filter`(
            requestParams: RequestParams,
            removedOption: Option,
    ) {
        val tokonowQueryParam = getTokonowQueryParam(requestParams)

        assertThat(tokonowQueryParam[removedOption.key], nullValue())
    }

    fun `empty state remove filter with exclude_ prefix`() {
        callback.`Given first page product list will be successful`()
        `Given view already created`()

        val visitableList = baseViewModel.visitableListLiveData.value!!
        val categoryFilterDataView = visitableList.filterIsInstance<CategoryFilterDataView>().first()
        val categoryFilterItemList = categoryFilterDataView.categoryFilterItemList
        val chosenCategoryFilter = categoryFilterItemList[0]
        val removedOption = chosenCategoryFilter.option

        callback.`Given first page product list is empty`()
        `Given view apply category filter`(removedOption)

        `When view remove filter from empty state`(removedOption)

        val requestParamsSlot = mutableListOf<RequestParams>()
        val requestParams by lazy { requestParamsSlot.last() }

        callback.`Then verify first page API is called`(3, requestParamsSlot)
        `Then verify request params does not contain param with exclude_ prefix`(requestParams, removedOption)
    }

    private fun `Then verify request params does not contain param with exclude_ prefix`(
            requestParams: RequestParams,
            removedOption: Option,
    ) {
        val tokonowQueryParam = getTokonowQueryParam(requestParams)
        val removedOptionKey = removedOption.key.removePrefix(OptionHelper.EXCLUDE_PREFIX)

        assertThat(tokonowQueryParam[removedOptionKey], nullValue())
    }

    private fun `Given view apply category filter`(appliedOption: Option) {
        baseViewModel.onViewClickCategoryFilterChip(appliedOption, true)
    }

    interface Callback {
        fun `Given first page product list is empty`()
        fun `Given first page product list will be successful`()
        fun `Then verify first page API is called`(
                count: Int,
                requestParamsSlot: MutableList<RequestParams>,
        )
    }
}