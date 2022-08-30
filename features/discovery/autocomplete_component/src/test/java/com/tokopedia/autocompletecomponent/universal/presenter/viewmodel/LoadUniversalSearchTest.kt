package com.tokopedia.autocompletecomponent.universal.presenter.viewmodel

import com.tokopedia.autocompletecomponent.shouldBeInstanceOf
import com.tokopedia.discovery.common.State
import org.junit.Test

internal class LoadUniversalSearchTest: UniversalSearchDataViewTestFixtures() {

    private val universalSearchState = universalSearchViewModel.getUniversalSearchState()
    private val expectedDataCount = 3

    @Test
    fun `Universal Search Successful`() {
        `Given universal search API will be successful`()

        `When universal search view created`()

        `Then assert universal search is successful`()
        `Then assert visitable data is correct`()
    }

    private fun `When universal search view created`() {
        universalSearchViewModel.onViewCreated()
    }

    private fun `Then assert universal search is successful`() {
        universalSearchState.value.shouldBeInstanceOf<State.Success<*>>()
    }

    private fun `Then assert visitable data is correct`() {
        assert(universalSearchState.value?.data?.size == expectedDataCount)
    }
}