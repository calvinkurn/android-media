package com.tokopedia.tokopedianow.searchcategory

import androidx.lifecycle.Observer
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.filter.common.data.Filter
import com.tokopedia.filter.common.data.Option
import com.tokopedia.tokopedianow.searchcategory.data.getTokonowQueryParam
import com.tokopedia.tokopedianow.searchcategory.presentation.model.QuickFilterDataView
import com.tokopedia.tokopedianow.searchcategory.presentation.model.SortFilterItemDataView
import com.tokopedia.tokopedianow.searchcategory.presentation.viewmodel.BaseSearchCategoryViewModel
import com.tokopedia.usecase.RequestParams
import org.hamcrest.CoreMatchers.nullValue
import org.junit.Assert.assertThat
import org.hamcrest.CoreMatchers.`is` as shouldBe

class CategoryChooserFilterTestHelper(
        private val baseViewModel: BaseSearchCategoryViewModel,
        private val callback: Callback,
) {

    private var chosenCategoryFilter = Option()

    fun `test category chooser cannot be spammed` () {
        var isOpenCount = 0
        val observer = Observer<Filter?> {
            if (it != null) isOpenCount++
        }

        callback.`Given first page use case will be successful`()
        `Given view already created`()
        `Given view will observe is L3 Category filter open live data`(observer)

        val categoryL3QuickFilterDataView =
                baseViewModel.visitableListLiveData.value.getCategoryL3FilterFromQuickFilter()

        `When user spam click open category chooser`(categoryL3QuickFilterDataView)

        `Then assert is L3 filter open is only called once`(isOpenCount)
        `Then remove observer is L3 Category filter open live data `(observer)
    }

    private fun `Given view already created`() {
        baseViewModel.onViewCreated()
    }

    private fun `Given view will observe is L3 Category filter open live data`(observer: Observer<Filter?>) {
        baseViewModel.isL3FilterPageOpenLiveData.observeForever(observer)
    }

    private fun List<Visitable<*>>?.getCategoryL3FilterFromQuickFilter(): SortFilterItemDataView {
        if (this == null)
            throw AssertionError("Visitable List is null")

        val quickFilterDataView = getQuickFilterDataView()

        return quickFilterDataView.quickFilterItemList.find {
            it.filter.options.size > 1
        } ?: throw AssertionError("Category L3 Filter not found")
    }

    private fun List<Visitable<*>>?.getQuickFilterDataView(): QuickFilterDataView {
        if (this == null)
            throw AssertionError("Visitable List is null")

        return find { it is QuickFilterDataView } as? QuickFilterDataView
                ?: throw AssertionError("Quick filter data view not found")
    }

    private fun `When user spam click open category chooser`(
            categoryL3QuickFilterDataView: SortFilterItemDataView
    ) {
        categoryL3QuickFilterDataView.sortFilterItem.chevronListener!!.invoke()
        categoryL3QuickFilterDataView.sortFilterItem.chevronListener!!.invoke()
        categoryL3QuickFilterDataView.sortFilterItem.chevronListener!!.invoke()
    }

    private fun `Then assert is L3 filter open is only called once`(isOpenCount: Int) {
        assertThat(isOpenCount, shouldBe(1))
    }

    private fun `Then remove observer is L3 Category filter open live data `(observer: Observer<Filter?>) {
        baseViewModel.isL3FilterPageOpenLiveData.removeObserver(observer)
    }

    private fun `Given view setup from created until open category chooser`() {
        callback.`Given first page use case will be successful`()
        `Given view already created`()

        val categoryL3QuickFilterDataView =
                baseViewModel.visitableListLiveData.value.getCategoryL3FilterFromQuickFilter()

        `Given view already open category chooser`(categoryL3QuickFilterDataView)
        `Given view choose option from category chooser`(categoryL3QuickFilterDataView)
    }

    private fun `Given view already open category chooser`(
            categoryL3QuickFilterDataView: SortFilterItemDataView
    ) {
        categoryL3QuickFilterDataView.sortFilterItem.chevronListener!!.invoke()
    }

    private fun `Given view choose option from category chooser`(
            categoryL3QuickFilterDataView: SortFilterItemDataView
    ) {
        chosenCategoryFilter = categoryL3QuickFilterDataView.filter.options[2]
    }

    fun `test apply filter from category chooser`() {
        val requestParamsSlot = mutableListOf<RequestParams>()
        val requestParams by lazy { requestParamsSlot.last() }

        `Given view setup from created until open category chooser`()

        `When view apply filter from category chooser`()

        callback.`Then assert first page use case is called twice`(requestParamsSlot)
        `Then verify query params is updated from category filter`(requestParams)
        `Then verify category chooser is dismissed`()
    }

    private fun `When view apply filter from category chooser`() {
        baseViewModel.onViewApplyFilterFromCategoryChooser(chosenCategoryFilter)
    }

    private fun `Then verify query params is updated from category filter`(requestParams: RequestParams) {
        val queryParams = getTokonowQueryParam(requestParams)
        val reason = "Query param key \"${chosenCategoryFilter.key}\" value is incorrect"

        assertThat(
                reason,
                queryParams[chosenCategoryFilter.key].toString(),
                shouldBe(chosenCategoryFilter.value)
        )
    }

    private fun `Then verify category chooser is dismissed`() {
        assertThat(baseViewModel.isL3FilterPageOpenLiveData.value, nullValue())
    }

    fun `test dismiss category chooser`() {
        `Given view setup from created until open category chooser`()

        `When view dismiss L3 filter page`()

        `Then verify category chooser is dismissed`()
    }

    private fun `When view dismiss L3 filter page`() {
        baseViewModel.onViewDismissL3FilterPage()
    }

    interface Callback {
        fun `Given first page use case will be successful`()
        fun `Then assert first page use case is called twice`(
                requestParamsSlot: MutableList<RequestParams>
        )
    }
}