package com.tokopedia.search.result.mps

import com.tokopedia.discovery.common.constants.SearchApiConst
import com.tokopedia.filter.common.data.DynamicFilterModel
import com.tokopedia.filter.common.data.Option
import com.tokopedia.search.jsonToObject
import com.tokopedia.search.result.isExecuted
import com.tokopedia.search.result.mps.domain.model.MPSModel
import com.tokopedia.search.result.stubExecute
import io.mockk.verify
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

private const val dynamicFilterModelJSON = "mps/filter/dynamic-filter-model.json"

class MultiProductSearchBottomSheetFilterTest: MultiProductSearchTestFixtures() {

    private val parameters = mapOf(
        SearchApiConst.Q1 to "samsung",
        SearchApiConst.Q2 to "xiaomi",
        SearchApiConst.Q3 to "iphone"
    )

    @Test
    fun `open bottomsheet filter`() {
        val mpsViewModel = mpsViewModel(
            MPSState(parameters).success(MPSSuccessJSON.jsonToObject())
        )

        `Given get dynamic filter use case is successful`()

        `When open bottomsheet filter`(mpsViewModel)

        `Then assert get dynamic filter request params`(parameters)
        `Then assert bottomsheet filter is opened`(mpsViewModel)
    }

    private fun `Given get dynamic filter use case is successful`(
        dynamicFilterModel: DynamicFilterModel = DynamicFilterModel()
    ) {
        getDynamicFilterUseCase.stubExecute(requestParamsSlot) returns dynamicFilterModel
    }

    private fun `When open bottomsheet filter`(mpsViewModel: MPSViewModel) {
        mpsViewModel.openBottomSheetFilter()
    }

    private fun `Then assert get dynamic filter request params`(parameters: Map<String, String>) {
        parameters.forEach { (key, value) ->
            assertEquals(value, requestParamParameters[key])
        }

        assertEquals(
            SearchApiConst.MPS,
            requestParamParameters[SearchApiConst.SOURCE]
        )
    }

    private fun `Then assert bottomsheet filter is opened`(mpsViewModel: MPSViewModel) {
        assertTrue(mpsViewModel.stateValue.isBottomSheetFilterOpen)
    }

    @Test
    fun `open bottomsheet filter success`() {
        val mpsViewModel = mpsViewModel(
            MPSState(parameters).success(MPSSuccessJSON.jsonToObject())
        )

        val dynamicFilterModel = dynamicFilterModelJSON.jsonToObject<DynamicFilterModel>()

        `Given get dynamic filter use case is successful`(dynamicFilterModel)

        `When open bottomsheet filter`(mpsViewModel)

        `Then assert filter model in state`(dynamicFilterModel, mpsViewModel)
    }

    private fun `Then assert filter model in state`(
        dynamicFilterModel: DynamicFilterModel,
        mpsViewModel: MPSViewModel
    ) {
        assertEquals(
            dynamicFilterModel,
            mpsViewModel.stateValue.bottomSheetFilterModel
        )
    }

    @Test
    fun `close bottomsheet filter`() {
        val mpsViewModel = mpsViewModel(
            MPSState(parameters)
                .success(MPSSuccessJSON.jsonToObject())
                .openBottomSheetFilter()
        )

        `When close bottomsheet filter`(mpsViewModel)

        `Then assert bottomsheet filter is closed`(mpsViewModel)
    }

    private fun `When close bottomsheet filter`(mpsViewModel: MPSViewModel) {
        mpsViewModel.closeBottomSheetFilter()
    }

    private fun `Then assert bottomsheet filter is closed`(mpsViewModel: MPSViewModel) {
        assertFalse(mpsViewModel.stateValue.isBottomSheetFilterOpen)
    }

    @Test
    fun `open bottomsheet filter will not call use case after successful`() {
        val mpsViewModel = mpsViewModel(
            MPSState(parameters)
                .success(MPSSuccessJSON.jsonToObject())
                .setBottomSheetFilterModel(dynamicFilterModelJSON.jsonToObject<DynamicFilterModel>())
        )

        `When open bottomsheet filter`(mpsViewModel)

        `Then verify get dynamic filter use case is not called`()
    }

    private fun `Then verify get dynamic filter use case is not called`() {
        verify(exactly = 0) { getDynamicFilterUseCase.execute(any(), any(), any()) }
    }

    @Test
    fun `apply bottomsheet filter will change parameter and filter state`() {
        val dynamicFilterModel = dynamicFilterModelJSON.jsonToObject<DynamicFilterModel>()
        val mpsModel = MPSSuccessJSON.jsonToObject<MPSModel>()

        val mpsViewModel = mpsViewModel(
            MPSState(parameters)
                .success(mpsModel)
                .setBottomSheetFilterModel(dynamicFilterModel)
        )

        `Given MPS Use Case success`(mpsModel, requestParamsSlot)

        val appliedOption = dynamicFilterModel.data.filter.first().options.first()
        val parameterWithFilter = parameters + mapOf(appliedOption.key to appliedOption.value)
        `When apply filter from bottomsheet`(mpsViewModel, parameterWithFilter)

        `Then assert parameter is replaced with parameter from filter`(
            mpsViewModel,
            parameterWithFilter
        )
        `Then assert page is reloaded`()
        `Then assert filter params is applied in filter state`(mpsViewModel, appliedOption)
    }

    private fun `When apply filter from bottomsheet`(
        mpsViewModel: MPSViewModel,
        parameterWithFilter: Map<String, String>
    ) {
        mpsViewModel.applyFilter(parameterWithFilter)
    }

    private fun `Then assert parameter is replaced with parameter from filter`(
        mpsViewModel: MPSViewModel,
        parameterWithFilter: Map<String, String>
    ) {
        assertEquals(mpsViewModel.stateValue.parameter, parameterWithFilter)
    }

    private fun `Then assert page is reloaded`() {
        mpsFirstPageUseCase.isExecuted()
    }

    private fun `Then assert filter params is applied in filter state`(
        mpsViewModel: MPSViewModel,
        appliedOption: Option,
    ) {
        assertEquals(
            appliedOption.value,
            mpsViewModel.stateValue.filterState.activeFilterMap[appliedOption.key],
        )
    }
}
