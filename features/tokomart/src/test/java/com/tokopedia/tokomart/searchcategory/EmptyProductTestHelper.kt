package com.tokopedia.tokomart.searchcategory

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.tokomart.searchcategory.presentation.model.EmptyProductDataView
import com.tokopedia.tokomart.searchcategory.presentation.viewmodel.BaseSearchCategoryViewModel
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.instanceOf
import org.junit.Assert.assertThat

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
        assertThat(baseViewModel.isHeaderBackgroundVisibleLiveData.value, `is`(false))
    }

    interface Callback {
        fun `Given first page product list is empty`()
    }
}