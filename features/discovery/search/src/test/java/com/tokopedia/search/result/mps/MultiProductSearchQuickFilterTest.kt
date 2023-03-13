package com.tokopedia.search.result.mps

import com.tokopedia.discovery.common.constants.SearchApiConst
import com.tokopedia.discovery.common.constants.SearchConstant
import com.tokopedia.filter.common.data.DataValue
import com.tokopedia.search.jsonToObject
import com.tokopedia.search.result.mps.domain.model.MPSModel
import com.tokopedia.search.result.mps.filter.quickfilter.QuickFilterDataView
import io.mockk.coVerify
import org.junit.Assert.assertEquals
import org.junit.Test

class MultiProductSearchQuickFilterTest: MultiProductSearchTestFixtures() {

    private val mpsViewModel = mpsViewModel(
        MPSState(
            mapOf(
                SearchApiConst.ACTIVE_TAB to SearchConstant.ActiveTab.MPS,
                SearchApiConst.Q1 to "samsung",
                SearchApiConst.Q2 to "xiaomi",
                SearchApiConst.Q3 to "iphone",
            )
        ),
    )

    @Test
    fun `on view created will show list of quick filter`() {
        val mpsModel = MPSSuccessJSON.jsonToObject<MPSModel>()
        `Given MPS Use Case success`(mpsModel)

        `When view created`()

        `Then assert quick filter data view list`(mpsModel.quickFilterModel)
    }

    private fun `When view created`() {
        mpsViewModel.onViewCreated()
    }

    private fun `Then assert quick filter data view list`(quickFilterModel: DataValue) {
        val quickFilterDataViewList = mpsViewModel.stateValue.quickFilterDataViewList
        val expectedQuickFilterList = quickFilterModel.filter

        assertEquals(expectedQuickFilterList.size, quickFilterDataViewList.size)

        quickFilterDataViewList.forEachIndexed { index, quickFilterDataView ->
            assertEquals(expectedQuickFilterList[index], quickFilterDataView.filter)
        }
    }

    @Test
    fun `selecting un-applied quick filter will apply key and value to parameter via filterState`() {
        val mpsModel = MPSSuccessJSON.jsonToObject<MPSModel>()
        val currentMPSState = MPSState().success(mpsModel)
        val mpsViewModel = mpsViewModel(currentMPSState)

        val selectedQuickFilter = currentMPSState.quickFilterDataViewList.first()
        `When select quick filter`(mpsViewModel, selectedQuickFilter)

        val expectedParameter =
            currentMPSState
                .filterState
                .setFilter(selectedQuickFilter.firstOption!!, true)
                .parameter

        `Then assert state is updated with new parameter`(mpsViewModel, expectedParameter)
        `Then assert use case is executed with new parameter`(expectedParameter)
    }

    private fun `When select quick filter`(
        mpsViewModel: MPSViewModel,
        selectedQuickFilter: QuickFilterDataView,
    ) {
        mpsViewModel.onQuickFilterSelected(selectedQuickFilter)
    }

    private fun `Then assert state is updated with new parameter`(
        mpsViewModel: MPSViewModel,
        expectedParameter: Map<String, String>,
    ) {
        assertEquals(expectedParameter, mpsViewModel.stateValue.parameter)
    }

    private fun `Then assert use case is executed with new parameter`(
        expectedParameter: Map<String, String>,
    ) {
        coVerify(exactly = 1) {
            mpsUseCase.execute(any(), any(), capture(requestParamsSlot))
        }

        assertEquals(expectedParameter, requestParamParameters)
    }

    @Test
    fun `selecting applied quick filter will un-apply key and value to parameter via filterState`() {
        val mpsModel = MPSSuccessJSON.jsonToObject<MPSModel>()
        val mpsStateSuccess = MPSState().success(mpsModel)
        val selectedQuickFilter = mpsStateSuccess.quickFilterDataViewList.first()
        val mpsStateApplyQuickFilter = mpsStateSuccess.applyQuickFilter(selectedQuickFilter)
        val mpsViewModel = mpsViewModel(mpsStateApplyQuickFilter)

        `When select quick filter`(mpsViewModel, selectedQuickFilter)

        val expectedParameter =
            mpsStateApplyQuickFilter
                .filterState
                .setFilter(selectedQuickFilter.firstOption!!, false)
                .parameter

        `Then assert state is updated with new parameter`(mpsViewModel, expectedParameter)
        `Then assert use case is executed with new parameter`(expectedParameter)
    }
}
