package com.tokopedia.search.result.presentation.viewmodel

import com.tokopedia.discovery.common.constants.SearchConstant.SearchTabPosition
import com.tokopedia.search.result.SearchState
import com.tokopedia.search.result.SearchViewModel
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import org.hamcrest.core.Is.`is`
import org.junit.Assert.assertThat
import org.junit.Test

internal class SearchViewModelTest {

    private fun SearchViewModel(
        searchState: SearchState = SearchState(),
    ) = SearchViewModel(
        searchState,
        CoroutineTestDispatchersProvider
    )

    private val SearchViewModel.stateValue
        get() = stateFlow.value

    @Test
    fun `handle show auto complete view`() {
        val searchViewModel = SearchViewModel()

        searchViewModel.showAutoCompleteView()

        assertThat(
            searchViewModel.stateValue.isOpeningAutoComplete,
            `is`(true)
        )
    }

    @Test
    fun `show auto complete handled`() {
        val searchStateOpenAutoComplete = SearchState().openAutoComplete()
        val searchViewModel = SearchViewModel(searchStateOpenAutoComplete)

        searchViewModel.showAutoCompleteHandled()

        assertThat(
            searchViewModel.stateValue.isOpeningAutoComplete,
            `is`(false)
        )
    }

    @Test
    fun `set active tab`() {
        val searchViewModel = SearchViewModel()

        searchViewModel.setActiveTab(SearchTabPosition.TAB_SECOND_POSITION)

        assertThat(
            searchViewModel.stateValue.activeTabPosition,
            `is`(SearchTabPosition.TAB_SECOND_POSITION)
        )
    }
}
