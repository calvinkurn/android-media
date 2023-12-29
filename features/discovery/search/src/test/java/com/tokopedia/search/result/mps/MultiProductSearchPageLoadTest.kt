package com.tokopedia.search.result.mps

import com.tokopedia.discovery.common.State.Error
import com.tokopedia.discovery.common.State.Success
import com.tokopedia.discovery.common.constants.SearchApiConst
import com.tokopedia.discovery.common.constants.SearchApiConst.Companion.DEFAULT_VALUE_OF_PARAMETER_ROWS
import com.tokopedia.discovery.common.constants.SearchApiConst.Companion.ROWS
import com.tokopedia.discovery.common.constants.SearchApiConst.Companion.START
import com.tokopedia.discovery.common.constants.SearchConstant
import com.tokopedia.search.TestException
import com.tokopedia.search.jsonToObject
import com.tokopedia.search.result.isExecuted
import com.tokopedia.search.result.isNeverExecuted
import com.tokopedia.search.result.mps.domain.model.MPSModel
import com.tokopedia.search.result.stubExecute
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.Is.`is`
import org.hamcrest.core.IsInstanceOf.instanceOf
import org.junit.Assert.assertEquals
import org.junit.Test

class MultiProductSearchPageLoadTest: MultiProductSearchTestFixtures() {

    private val parameter = mapOf(
        SearchApiConst.ACTIVE_TAB to SearchConstant.ActiveTab.MPS,
        SearchApiConst.Q1 to "samsung",
        SearchApiConst.Q2 to "xiaomi",
        SearchApiConst.Q3 to "iphone",
    )

    private val mpsViewModel = mpsViewModel(MPSState(parameter))

    @Test
    fun `multi product search first page success`() {
        val mpsModel = MPSSuccessJSON.jsonToObject<MPSModel>()
        `Given MPS Use Case success`(mpsModel, requestParamsSlot)

        `When view created`()

        val mpsState = mpsViewModel.stateValue
        `Then assert use case is executed with correct parameters`()
        `Then assert state is success`(mpsState)
        `Then assert pagination state`(mpsViewModel, 8, mpsModel.totalData)
    }

    private fun `When view created`() {
        mpsViewModel.onViewCreated()
    }

    private fun `Then assert use case is executed with correct parameters`() {
        mpsFirstPageUseCase.isExecuted()

        parameter.forEach { (key, value) ->
            assertEquals(value, requestParamParameters[key])
        }

        assertEquals("0", requestParamParameters[START])
        assertEquals(DEFAULT_VALUE_OF_PARAMETER_ROWS, requestParamParameters[ROWS])
    }

    private fun `Then assert state is success`(mpsState: MPSState) {
        assertThat(mpsState.result, `is`(instanceOf(Success::class.java)))
    }

    private fun `Then assert pagination state`(
        mpsViewModel: MPSViewModel,
        expectedStartFrom: Long,
        expectedTotalData: Long,
    ) {
        assertEquals(expectedStartFrom, mpsViewModel.stateValue.paginationState.startFrom)
        assertEquals(expectedTotalData, mpsViewModel.stateValue.paginationState.totalData)
    }

    @Test
    fun `multi product search first page failed`() {
        val exception = TestException("test exception")
        `Given MPS Use Case failed`(exception)

        `When view created`()

        `Then assert state is error`(mpsViewModel.stateValue, exception)
    }

    private fun `Given MPS Use Case failed`(throwable: Throwable) {
        mpsFirstPageUseCase.stubExecute() throws throwable
    }

    private fun `Then assert state is error`(mpsState: MPSState, throwable: Throwable) {
        assertThat(mpsState.result, `is`(instanceOf(Error::class.java)))
        assertEquals(throwable, (mpsState.result as Error).throwable)
    }

    @Test
    fun `multi product search load more page success`() {
        val mpsModel = MPSSuccessJSON.jsonToObject<MPSModel>()
        val mpsStateAfterFirstPage = MPSState(parameter).success(mpsModel)
        val mpsViewModel = mpsViewModel(mpsStateAfterFirstPage)

        val mpsModelLoadMore = MPSLoadMoreSuccessJSON.jsonToObject<MPSModel>()
        `Given MPS load more use case success`(mpsModelLoadMore, requestParamsSlot)

        `When load more`(mpsViewModel)

        `Then assert load more use case is executed with correct parameters`()
        `Then assert pagination state`(mpsViewModel, 16, mpsModel.totalData)
    }

    private fun `When load more`(mpsViewModel: MPSViewModel) {
        mpsViewModel.onViewLoadMore()
    }

    private fun `Then assert load more use case is executed with correct parameters`() {
        mpsLoadMoreUseCase.isExecuted()

        parameter.forEach { (key, value) ->
            assertEquals(value, requestParamParameters[key])
        }

        assertEquals("8", requestParamParameters[START])
        assertEquals(DEFAULT_VALUE_OF_PARAMETER_ROWS, requestParamParameters[ROWS])
    }

    @Test
    fun `multi product search load more page failed`() {
        val mpsModel = MPSSuccessJSON.jsonToObject<MPSModel>()
        val mpsStateAfterFirstPage = MPSState(parameter).success(mpsModel)
        val mpsViewModel = mpsViewModel(mpsStateAfterFirstPage)

        val exception = TestException("test exception")
        `Given MPS load more use case failed`(exception)

        `When load more`(mpsViewModel)

        `Then assert state load more error`(mpsViewModel.stateValue, exception)
    }

    private fun `Given MPS load more use case failed`(throwable: Throwable) {
        mpsLoadMoreUseCase.stubExecute() throws throwable
    }

    private fun `Then assert state load more error`(
        mpsState: MPSState,
        throwable: Throwable?,
    ) {
        assertEquals(throwable, mpsState.loadMoreThrowable)
    }

    @Test
    fun `multi product search will not load more if does not have next page`() {
        val mpsModel = "mps/mps-no-next-page.json".jsonToObject<MPSModel>()
        val mpsStateAfterFirstPage = MPSState(parameter).success(mpsModel)
        val mpsViewModel = mpsViewModel(mpsStateAfterFirstPage)

        mpsLoadMoreUseCase.stubExecute() returns MPSModel()

        `When load more`(mpsViewModel)

        mpsLoadMoreUseCase.isNeverExecuted()
    }

    @Test
    fun `multi product search reload page will reset state and call first page use case`() {
        val mpsModel = MPSSuccessJSON.jsonToObject<MPSModel>()
        val mpsStateAfterFirstPage = MPSState(parameter).success(mpsModel)
        val mpsViewModel = mpsViewModel(mpsStateAfterFirstPage)

        `Given MPS Use Case success`(mpsModel, requestParamsSlot)

        `When view reload data`(mpsViewModel)

        val mpsState = mpsViewModel.stateValue
        `Then assert use case is executed with correct parameters`()
        `Then assert state is success`(mpsState)
        `Then assert pagination state`(mpsViewModel, 8, mpsModel.totalData)
    }

    private fun `When view reload data`(mpsViewModel: MPSViewModel) {
        mpsViewModel.onViewReloadData()
    }
}
