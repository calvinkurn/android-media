package com.tokopedia.search.result.mps

import com.tokopedia.search.jsonToObject
import com.tokopedia.search.result.mps.domain.model.MPSModel
import com.tokopedia.search.result.mps.emptystate.MPSEmptyStateKeywordDataView
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.Is.`is`
import org.junit.Test

class MultiProductSearchEmptyState: MultiProductSearchTestFixtures() {

    @Test
    fun `no data will show empty state`() {
        val mpsModel = "mps/mps-emptystate.json".jsonToObject<MPSModel>()
        val mpsViewModel = mpsViewModel()

        `Given MPS Use Case success`(mpsModel)

        `When view created`(mpsViewModel)

        `Then assert empty state`(mpsViewModel)
    }

    private fun `When view created`(mpsViewModel: MPSViewModel) {
        mpsViewModel.onViewCreated()
    }

    private fun `Then assert empty state`(mpsViewModel: MPSViewModel) {
        assertThat(mpsViewModel.stateData, `is`(listOf(MPSEmptyStateKeywordDataView)))
    }
}
