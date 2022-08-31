package com.tokopedia.autocompletecomponent.universal.presenter.viewmodel

import androidx.lifecycle.LiveData
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.autocompletecomponent.shouldBeInstanceOf
import com.tokopedia.autocompletecomponent.universal.presentation.widget.errorstate.ErrorStateDataView
import com.tokopedia.discovery.common.State
import org.junit.Test

internal class LoadUniversalSearchErrorTest: UniversalSearchDataViewTestFixtures() {

    private lateinit var universalSearchState: LiveData<State<List<Visitable<*>>>>

    @Test
    fun `Universal Search Error`() {
        `Given universal search API will be fail`()

        `When universal search view created`()

        `Then assert universal search is failed`()
        `Then assert visitable data is correct`()
    }

    private fun `When universal search view created`() {
        universalSearchViewModel.onViewCreated()
    }

    private fun `Then assert universal search is failed`() {
        universalSearchState = universalSearchViewModel.getUniversalSearchState()

        universalSearchState.value.shouldBeInstanceOf<State.Error<*>>()
    }

    private fun `Then assert visitable data is correct`() {
        assert(universalSearchState.value?.data?.size == 1)
        universalSearchState.value?.data?.assertVisitableList()
    }

    private fun List<Visitable<*>>.assertVisitableList() {
        get(0).shouldBeInstanceOf<ErrorStateDataView>()
    }
}