package com.tokopedia.kyc_centralized.gotoKyc.viewModel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.kyc_centralized.ui.gotoKyc.bottomSheet.OnboardProgressiveViewModel
import com.tokopedia.kyc_centralized.ui.gotoKyc.domain.RegisterProgressiveResult
import com.tokopedia.kyc_centralized.ui.gotoKyc.domain.RegisterProgressiveUseCase
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import io.mockk.coEvery
import io.mockk.mockk
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import util.getOrAwaitValue
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class OnboardProgressiveViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val dispatcher = CoroutineTestDispatchersProvider
    private lateinit var viewModel: OnboardProgressiveViewModel

    private val registerProgressiveUseCase = mockk<RegisterProgressiveUseCase>(relaxed = true)

    @Before
    fun setup() {
        viewModel = OnboardProgressiveViewModel(registerProgressiveUseCase, dispatcher)
    }

    @Test
    fun `when register progressive then return risky user`() {
        val challengeId = "AsDew-SdJdw5F"
        val projectId = "7"
        val expected = RegisterProgressiveResult.RiskyUser(challengeId)

        coEvery { registerProgressiveUseCase(any()) } returns expected
        viewModel.registerProgressive(projectId)

        val result = viewModel.registerProgressive.getOrAwaitValue()
        assertTrue(result is RegisterProgressiveResult.RiskyUser)
        assertEquals(challengeId, result.challengeId)
    }

    @Test
    fun `when register progressive then return not risky user`() {
        val rejectionReason = "Terjadi kesalahan!"
        val status = -1
        val projectId = "7"
        val expected = RegisterProgressiveResult.NotRiskyUser(status, rejectionReason)

        coEvery { registerProgressiveUseCase(any()) } returns expected
        viewModel.registerProgressive(projectId)

        val result = viewModel.registerProgressive.getOrAwaitValue()
        assertTrue(result is RegisterProgressiveResult.NotRiskyUser)
        assertEquals(rejectionReason, result.rejectionReason)
        assertEquals(status, result.status)
    }

    @Test
    fun `when register progressive then return exhausted`() {
        val projectId = "7"
        val cooldownTimeInSeconds = "3600"
        val maximumAttemptsAllowed = "3"
        val expected = RegisterProgressiveResult.Exhausted(cooldownTimeInSeconds, maximumAttemptsAllowed)

        coEvery { registerProgressiveUseCase(any()) } returns expected
        viewModel.registerProgressive(projectId)

        val result = viewModel.registerProgressive.getOrAwaitValue()
        assertTrue(result is RegisterProgressiveResult.Exhausted)
        assertEquals(cooldownTimeInSeconds, result.cooldownTimeInSeconds)
        assertEquals(maximumAttemptsAllowed, result.maximumAttemptsAllowed)
    }

    @Test
    fun `when register progressive then failed`() {
        val throwable = Throwable()
        val projectId = "7"

        coEvery { registerProgressiveUseCase(any()) } throws throwable
        viewModel.registerProgressive(projectId)

        val result = viewModel.registerProgressive.getOrAwaitValue()
        assertTrue(result is RegisterProgressiveResult.Failed)
        assertEquals(throwable, result.throwable)
    }

}
