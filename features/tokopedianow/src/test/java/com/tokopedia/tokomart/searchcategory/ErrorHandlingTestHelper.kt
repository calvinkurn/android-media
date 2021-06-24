package com.tokopedia.tokomart.searchcategory

import com.tokopedia.tokomart.searchcategory.presentation.viewmodel.BaseSearchCategoryViewModel
import org.junit.Assert.assertThat
import org.hamcrest.CoreMatchers.`is` as shouldBe

class ErrorHandlingTestHelper(
        private val baseViewModel: BaseSearchCategoryViewModel,
        private val callback: Callback,
) {

    fun `Test first page error`() {
        val throwable = Throwable("error message")
        callback.`Given first page will error`(throwable)

        `When view created`()

        `Then assert page show error with exception data`(throwable)
    }

    private fun `When view created`() {
        baseViewModel.onViewCreated()
    }

    private fun `Then assert page show error with exception data`(throwable: Throwable) {
        val actualThrowable = baseViewModel.isShowErrorLiveData.value!!

        assertThat(actualThrowable, shouldBe(throwable))
    }

    interface Callback {
        fun `Given first page will error`(throwable: Throwable)
    }
}