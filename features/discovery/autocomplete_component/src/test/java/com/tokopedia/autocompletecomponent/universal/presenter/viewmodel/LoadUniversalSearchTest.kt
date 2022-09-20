package com.tokopedia.autocompletecomponent.universal.presenter.viewmodel

import androidx.lifecycle.LiveData
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.autocompletecomponent.shouldBeInstanceOf
import com.tokopedia.autocompletecomponent.universal.domain.model.UniversalSearchModel
import com.tokopedia.autocompletecomponent.universal.presentation.widget.carousel.CarouselDataView
import com.tokopedia.autocompletecomponent.universal.presentation.widget.doubleline.DoubleLineDataView
import com.tokopedia.autocompletecomponent.universal.presentation.widget.errorstate.ErrorStateDataView
import com.tokopedia.autocompletecomponent.universal.presentation.widget.listgrid.ListGridDataView
import com.tokopedia.autocompletecomponent.universal.presenter.viewmodel.testinstance.universalSearchItems
import com.tokopedia.discovery.common.State
import org.junit.Test

internal class LoadUniversalSearchTest: UniversalSearchDataViewTestFixtures() {

    private lateinit var universalSearchState: LiveData<State<List<Visitable<*>>>>
    private val expectedDataCount = 3

    @Test
    fun `Universal Search Successful`() {
        `Given universal search API will be successful`()

        `When universal search view created`()

        `Then assert universal search is successful`()
        `Then assert visitable data`()
    }

    private fun `When universal search view created`() {
        universalSearchViewModel.loadData()
    }

    private fun `Then assert universal search is successful`() {
        universalSearchState = universalSearchViewModel.getUniversalSearchState()

        universalSearchState.value.shouldBeInstanceOf<State.Success<*>>()
    }

    private fun `Then assert visitable data`() {
        assert(universalSearchState.value?.data?.size == expectedDataCount)
        universalSearchState.value?.data?.assertVisitableList()
    }

    private fun List<Visitable<*>>.assertVisitableList() {
        get(0).shouldBeInstanceOf<CarouselDataView>()
        get(1).shouldBeInstanceOf<DoubleLineDataView>()
        get(2).shouldBeInstanceOf<ListGridDataView>()

        assertVisitableListData(universalSearchItems, keyword, dimension90)
    }

    @Test
    fun `Universal Search Error`() {
        `Given universal search API will be fail`()

        `When universal search view created`()

        `Then assert universal search is failed`()
        `Then assert error visitable data`()
    }

    private fun `Then assert universal search is failed`() {
        universalSearchState = universalSearchViewModel.getUniversalSearchState()

        universalSearchState.value.shouldBeInstanceOf<State.Error<*>>()
    }

    private fun `Then assert error visitable data`() {
        assert(universalSearchState.value?.data?.size == 1)
        universalSearchState.value?.data?.get(0).shouldBeInstanceOf<ErrorStateDataView>()
    }
}