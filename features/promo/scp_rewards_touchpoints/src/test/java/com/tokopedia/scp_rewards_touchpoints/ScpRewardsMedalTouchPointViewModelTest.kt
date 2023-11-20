package com.tokopedia.scp_rewards_touchpoints

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.scp_rewards_touchpoints.common.Error
import com.tokopedia.scp_rewards_touchpoints.common.Success
import com.tokopedia.scp_rewards_touchpoints.touchpoints.data.response.ScpRewardsMedalTouchPointResponse
import com.tokopedia.scp_rewards_touchpoints.touchpoints.domain.ScpRewardsMedalTouchPointUseCase
import com.tokopedia.scp_rewards_touchpoints.touchpoints.viewmodel.ScpRewardsMedalTouchPointViewModel
import com.tokopedia.unit.test.ext.verifyValueEquals
import com.tokopedia.unit.test.rule.CoroutineTestRule
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ScpRewardsMedalTouchPointViewModelTest {

    @get:Rule
    val rule = CoroutineTestRule()

    @get:Rule
    val rule2 = InstantTaskExecutorRule()

    private var touchpointViewModel: ScpRewardsMedalTouchPointViewModel? = null
    private val touchpointUseCase = mockk<ScpRewardsMedalTouchPointUseCase>()

    private val successCode = "200"
    private val slug = "UNILEVER_CLUB"
    private val orderId = 100L
    private val retryDelay = 10L

    @Before
    fun setup() {
        touchpointViewModel = ScpRewardsMedalTouchPointViewModel(
            touchpointUseCase,
            rule.dispatchers
        )
    }

    @Test
    fun `get touchpoint success without delay`() {
        val response = getTouchpointSuccessResponse()
        val actualtouchPointResult = getTouchPointActualSuccessResult(result = response)
        coEvery { touchpointUseCase.getTouchPoint(orderId, any(), any()) } returns response
        touchpointViewModel?.getMedalTouchPoint(orderId, "", "")
        touchpointViewModel?.medalTouchPointData?.verifyValueEquals(actualtouchPointResult)
    }

    @Test
    fun `get touchpoint success but retry true with delay`() {
        val retryResponse = getTouchpointSuccessResponse(retry = true, delay = retryDelay)
        val successResponse = getTouchpointSuccessResponse()
        val actualtouchPointResult = getTouchPointActualSuccessResult(result = successResponse)
        var invocationCount = 0
        coEvery { touchpointUseCase.getTouchPoint(orderId, any(), any()) } answers {
            val response = if (invocationCount == 0) retryResponse else successResponse
            invocationCount++
            response
        }
        touchpointViewModel?.getMedalTouchPoint(orderId, "", "")
        rule.dispatchers.io.advanceUntilIdle()
        coVerify(exactly = 2) { touchpointUseCase.getTouchPoint(orderId, any(), any()) }
        touchpointViewModel?.medalTouchPointData?.verifyValueEquals(actualtouchPointResult)
    }

    @Test
    fun `get touchpoint failure`() {
        val error = Throwable()
        val actualtouchPointResult = getTouchPointActualFailureResult(error = error)
        coEvery { touchpointUseCase.getTouchPoint(orderId, any(), any()) } throws error
        touchpointViewModel?.getMedalTouchPoint(orderId, "", "")
        touchpointViewModel?.medalTouchPointData?.verifyValueEquals(actualtouchPointResult)
    }

    private fun getTouchpointSuccessResponse(retry: Boolean = false, delay: Long = 0L) = ScpRewardsMedalTouchPointResponse(
        scpRewardsMedaliTouchpointOrder = ScpRewardsMedalTouchPointResponse.ScpRewardsMedaliTouchpointOrder(
            resultStatus = ScpRewardsMedalTouchPointResponse.ScpRewardsMedaliTouchpointOrder.ResultStatus(
                code = successCode
            ),
            medaliTouchpointOrder = ScpRewardsMedalTouchPointResponse.ScpRewardsMedaliTouchpointOrder.MedaliTouchpointOrder(
                medaliSlug = slug,
                retryChecking = ScpRewardsMedalTouchPointResponse.ScpRewardsMedaliTouchpointOrder.MedaliTouchpointOrder.RetryChecking(
                    isRetryable = retry,
                    durationToRetry = delay
                )
            )
        )
    )

    private fun getTouchPointActualSuccessResult(initalLoad: Boolean = false, result: ScpRewardsMedalTouchPointResponse) = ScpRewardsMedalTouchPointViewModel.ScpTouchPointResult(
        initialLoad = false,
        result = Success(result)
    )

    private fun getTouchPointActualFailureResult(initalLoad: Boolean = false, error: Throwable) = ScpRewardsMedalTouchPointViewModel.ScpTouchPointResult(
        initialLoad = false,
        result = Error(error)
    )
}
