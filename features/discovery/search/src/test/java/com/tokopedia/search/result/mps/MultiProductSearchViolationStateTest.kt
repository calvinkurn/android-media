package com.tokopedia.search.result.mps

import com.tokopedia.discovery.common.constants.SearchApiConst
import com.tokopedia.discovery.common.constants.SearchConstant
import com.tokopedia.search.jsonToObject
import com.tokopedia.search.result.mps.domain.model.MPSModel
import com.tokopedia.search.result.mps.violationstate.ViolationStateDataView
import com.tokopedia.search.result.mps.violationstate.ViolationType
import com.tokopedia.search.result.mps.violationstate.ViolationType.*
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.Is.`is`
import org.hamcrest.core.IsInstanceOf
import org.junit.Assert
import org.junit.Test

class MultiProductSearchViolationStateTest: MultiProductSearchTestFixtures() {

    private val parameter = mapOf(
        SearchApiConst.ACTIVE_TAB to SearchConstant.ActiveTab.MPS,
        SearchApiConst.Q1 to "samsung",
        SearchApiConst.Q2 to "xiaomi",
        SearchApiConst.Q3 to "iphone",
    )

    private fun `When view created`(mpsViewModel: MPSViewModel) {
        mpsViewModel.onViewCreated()
    }

    @Test
    fun `MPS will show violation banned listed state by keyword`() {
        val mpsModel = "mps/mps-banned-violationstate.json".jsonToObject<MPSModel>()
        val activeFilterOption = mpsModel.quickFilterList.first().options.first()
        val activeFilterPair = activeFilterOption.run { key to value }
        val parameterWithFilter = parameter + activeFilterPair
        val mpsState = MPSState(parameterWithFilter)
        val mpsViewModel = mpsViewModel(mpsState)

        `Given MPS Use Case success`(mpsModel)

        `When view created`(mpsViewModel)

        mpsViewModel.apply {
            `assert is violation state`()
            `assert violation state is`(BANNED)
        }
    }

    @Test
    fun `MPS will show violation black listed state by keyword`() {
        val mpsModel = "mps/mps-blacklisted-violationstate.json".jsonToObject<MPSModel>()
        val activeFilterOption = mpsModel.quickFilterList.first().options.first()
        val activeFilterPair = activeFilterOption.run { key to value }
        val parameterWithFilter = parameter + activeFilterPair
        val mpsState = MPSState(parameterWithFilter)
        val mpsViewModel = mpsViewModel(mpsState)

        `Given MPS Use Case success`(mpsModel)

        `When view created`(mpsViewModel)

        mpsViewModel.apply {
            `assert is violation state`()
            `assert violation state is`(BLACKLISTED)
        }
    }

    private fun MPSViewModel.`assert is violation state`() {
        assertThat(this.visitableList.first(), `is`(IsInstanceOf.instanceOf(ViolationStateDataView::class.java)))
    }

    private fun MPSViewModel.`assert violation state is`(type: ViolationType) {
        Assert.assertEquals(type, (this.visitableList.first() as ViolationStateDataView).violationType)
    }
}
