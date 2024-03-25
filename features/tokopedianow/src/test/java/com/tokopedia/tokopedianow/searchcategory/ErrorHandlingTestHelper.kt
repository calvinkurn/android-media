package com.tokopedia.tokopedianow.searchcategory

import com.tokopedia.tokopedianow.search.presentation.viewmodel.TokoNowSearchViewModel
import org.junit.Assert.assertThat
import org.hamcrest.CoreMatchers.`is` as shouldBe

class ErrorHandlingTestHelper(
    private val baseViewModel: TokoNowSearchViewModel,
    private val callback: Callback
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
        val actualStopPerformanceMonitoring = baseViewModel.stopPerformanceMonitoringLiveData.value

        assertThat(actualThrowable, shouldBe(throwable))
        assertThat(actualStopPerformanceMonitoring, shouldBe(Unit))
    }

    interface Callback {
        fun `Given first page will error`(throwable: Throwable)
    }
}
