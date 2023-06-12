package com.tokopedia.search.result.mps

import com.tokopedia.discovery.common.constants.SearchApiConst
import com.tokopedia.discovery.common.constants.SearchConstant
import com.tokopedia.search.jsonToObject
import com.tokopedia.search.result.mps.domain.model.MPSModel
import com.tokopedia.search.result.mps.emptystate.MPSEmptyStateFilterDataView
import com.tokopedia.search.result.mps.emptystate.MPSEmptyStateKeywordDataView
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.Is.`is`
import org.junit.Assert.assertEquals
import org.junit.Test

class MultiProductSearchEmptyStateTest: MultiProductSearchTestFixtures() {

    private val parameter = mapOf(
        SearchApiConst.ACTIVE_TAB to SearchConstant.ActiveTab.MPS,
        SearchApiConst.Q1 to "samsung",
        SearchApiConst.Q2 to "xiaomi",
        SearchApiConst.Q3 to "iphone",
    )

    @Test
    fun `shop list empty will show empty state by keyword`() {
        val mpsModel = "mps/mps-emptystate.json".jsonToObject<MPSModel>()
        val mpsViewModel = mpsViewModel(MPSState(parameter))

        `Given MPS Use Case success`(mpsModel)

        `When view created`(mpsViewModel)

        `Then assert empty state by keyword`(mpsViewModel)
    }

    private fun `When view created`(mpsViewModel: MPSViewModel) {
        mpsViewModel.onViewCreated()
    }

    private fun `Then assert empty state by keyword`(mpsViewModel: MPSViewModel) {
        assertThat(mpsViewModel.visitableList, `is`(listOf(MPSEmptyStateKeywordDataView)))
    }

    @Test
    fun `shop list empty with filter active will show empty state by filter`() {
        val mpsModel = "mps/mps-emptystate.json".jsonToObject<MPSModel>()
        val activeFilterOption = mpsModel.quickFilterList.first().options.first()
        val activeFilterPair = activeFilterOption.run { key to value }
        val parameterWithFilter = parameter + activeFilterPair
        val mpsState = MPSState(parameterWithFilter)
        val mpsViewModel = mpsViewModel(mpsState)

        `Given MPS Use Case success`(mpsModel)

        `When view created`(mpsViewModel)

        `Then assert empty state by filter`(mpsViewModel)
    }

    private fun `Then assert empty state by filter`(mpsViewModel: MPSViewModel) {
        assertEquals(
            mpsViewModel.visitableList,
            listOf(MPSEmptyStateFilterDataView)
        )
    }
}
