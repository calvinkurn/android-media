package com.tokopedia.tokomart.searchcategory

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.filter.common.data.Option
import com.tokopedia.tokomart.searchcategory.presentation.model.EmptyProductDataView
import com.tokopedia.tokomart.searchcategory.presentation.model.QuickFilterDataView
import com.tokopedia.tokomart.searchcategory.presentation.model.SortFilterItemDataView
import com.tokopedia.tokomart.searchcategory.presentation.viewmodel.BaseSearchCategoryViewModel
import org.hamcrest.CoreMatchers.instanceOf
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

    interface Callback {
        fun `Given first page product list is empty`()
        fun `Given first page product list will be successful`()
    }
}