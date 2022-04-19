package com.tokopedia.search.result.presentation.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.search.shouldBe
import org.junit.Rule
import org.junit.Test

internal class SearchViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val searchViewModel = SearchViewModel(CoroutineTestDispatchersProvider)

    @Test
    fun `handle show auto complete view`() {
        `When show auto complete view`()

        `Then validate show auto complete view event`()
    }

    private fun `When show auto complete view`() {
        searchViewModel.showAutoCompleteView()
    }

    private fun `Then validate show auto complete view event`() {
        val autoCompleteEvent = searchViewModel.getShowAutoCompleteViewEventLiveData().value

        autoCompleteEvent?.getContentIfNotHandled() shouldBe true
    }

    @Test
    fun `handle hide search page loading`() {
        `When hide search page loading`()

        `Then validate hide search page loading event`()
    }

    private fun `When hide search page loading`() {
        searchViewModel.hideSearchPageLoading()
    }

    private fun `Then validate hide search page loading event`() {
        val hideLoadingEvent = searchViewModel.getHideLoadingEventLiveData().value

        hideLoadingEvent?.getContentIfNotHandled() shouldBe true
    }

    @Test
    fun `Change bottom navigation visibility to visible`() {
        `When change bottom navigation visibility`(true)
        `Then verify bottom navigation visibility live data`(true)
    }

    private fun `When change bottom navigation visibility`(isVisible: Boolean) {
        searchViewModel.changeBottomNavigationVisibility(isVisible)
    }

    private fun `Then verify bottom navigation visibility live data`(expectedIsBottomNavigationVisible: Boolean) {
        val bottomNavigationVisibilityLiveData = searchViewModel.getBottomNavigationVisibilityLiveData()
        bottomNavigationVisibilityLiveData.value shouldBe expectedIsBottomNavigationVisible
    }

    @Test
    fun `Change bottom navigation visibility to hidden`() {
        `When change bottom navigation visibility`(false)
        `Then verify bottom navigation visibility live data`(false)
    }
}