package com.tokopedia.kyc_centralized.gotoKyc.viewModel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.kyc_centralized.ui.gotoKyc.domain.ProjectInfoResult
import com.tokopedia.kyc_centralized.ui.gotoKyc.domain.ProjectInfoUseCase
import com.tokopedia.kyc_centralized.ui.gotoKyc.domain.RegisterProgressiveResult
import com.tokopedia.kyc_centralized.ui.gotoKyc.domain.RegisterProgressiveUseCase
import com.tokopedia.kyc_centralized.ui.gotoKyc.main.submit.FinalLoaderViewModel
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import io.mockk.coEvery
import io.mockk.mockk
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import util.getOrAwaitValue
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class FinalLoaderViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val dispatcher = CoroutineTestDispatchersProvider
    private lateinit var viewModel: FinalLoaderViewModel

    private val registerProgressiveUseCase = mockk<RegisterProgressiveUseCase>(relaxed = true)
    private val projectInfoUseCase = mockk<ProjectInfoUseCase>(relaxed = true)

    @Before
    fun setup() {
        viewModel = FinalLoaderViewModel(registerProgressiveUseCase, projectInfoUseCase, dispatcher)
    }

    @Test
    fun `when register progressive then return risky user`() {
        val challengeId = "AsDew-SdJdw5F"
        val projectId = "7"
        val expected = RegisterProgressiveResult.RiskyUser(challengeId)

        coEvery { registerProgressiveUseCase(any()) } returns expected
        viewModel.registerProgressive(projectId, challengeId)

        val result = viewModel.registerProgressive.getOrAwaitValue()
        assertTrue(result is RegisterProgressiveResult.RiskyUser)
        assertEquals(challengeId, result.challengeId)
    }

    @Test
    fun `when register progressive then return not risky user`() {
        val rejectionReason = "Terjadi kesalahan!"
        val status = -1
        val projectId = "7"
        val challengeId = "AsDew-SdJdw5F"
        val expected = RegisterProgressiveResult.NotRiskyUser(status, rejectionReason)

        coEvery { registerProgressiveUseCase(any()) } returns expected
        viewModel.registerProgressive(projectId, challengeId)

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
        val challengeId = "AsDew-SdJdw5F"
        val expected = RegisterProgressiveResult.Exhausted(cooldownTimeInSeconds, maximumAttemptsAllowed)

        coEvery { registerProgressiveUseCase(any()) } returns expected
        viewModel.registerProgressive(projectId, challengeId)

        val result = viewModel.registerProgressive.getOrAwaitValue()
        assertTrue(result is RegisterProgressiveResult.Exhausted)
        assertEquals(cooldownTimeInSeconds, result.cooldownTimeInSeconds)
        assertEquals(maximumAttemptsAllowed, result. maximumAttemptsAllowed)
    }

    @Test
    fun `when register progressive then failed`() {
        val throwable = Throwable()
        val projectId = "7"
        val challengeId = "AsDew-SdJdw5F"

        coEvery { registerProgressiveUseCase(any()) } throws throwable
        viewModel.registerProgressive(projectId, challengeId)

        val result = viewModel.registerProgressive.getOrAwaitValue()
        assertTrue(result is RegisterProgressiveResult.Failed)
        assertEquals(throwable, result.throwable)
    }

    @Test
    fun `when get status then return toko kyc`() {
        val projectId = "7"
        val expected = ProjectInfoResult.TokoKyc

        coEvery { projectInfoUseCase(any()) } returns expected
        viewModel.kycStatus(projectId)

        val result = viewModel.kycStatus.getOrAwaitValue()
        assertTrue(result is ProjectInfoResult.TokoKyc)
    }

    @Test
    fun `when get status then return status submission`() {
        val projectId = "7"
        val status = "0"
        val rejectionReason = "Terjadi Kesalahan"
        val waitMessage = "Tunggu 24 Jam ya!"
        val expected = ProjectInfoResult.StatusSubmission(
            status, rejectionReason, waitMessage
        )

        coEvery { projectInfoUseCase(any()) } returns expected
        viewModel.kycStatus(projectId)

        val result = viewModel.kycStatus.getOrAwaitValue()
        assertTrue(result is ProjectInfoResult.StatusSubmission)
        assertEquals(status, result.status)
        assertEquals(rejectionReason, result.rejectionReason)
        assertEquals(waitMessage, result.waitMessage)
    }

    @Test
    fun `when get status then return not verified and account linked`() {
        val projectId = "7"
        val isAccountLinked = true
        val expected = ProjectInfoResult.NotVerified(isAccountLinked)

        coEvery { projectInfoUseCase(any()) } returns expected
        viewModel.kycStatus(projectId)

        val result = viewModel.kycStatus.getOrAwaitValue()
        assertTrue(result is ProjectInfoResult.NotVerified)
        assertEquals(isAccountLinked, result.isAccountLinked)
    }

    @Test
    fun `when get status then return not verified and account not linked`() {
        val projectId = "7"
        val isAccountLinked = false
        val expected = ProjectInfoResult.NotVerified(isAccountLinked)

        coEvery { projectInfoUseCase(any()) } returns expected
        viewModel.kycStatus(projectId)

        val result = viewModel.kycStatus.getOrAwaitValue()
        assertTrue(result is ProjectInfoResult.NotVerified)
        assertEquals(isAccountLinked, result.isAccountLinked)
    }

    @Test
    fun `when get status then return failed`() {
        val projectId = "7"
        val throwable = Throwable()
        coEvery { projectInfoUseCase(any()) } throws throwable
        viewModel.kycStatus(projectId)

        val result = viewModel.kycStatus.getOrAwaitValue()
        assertTrue(result is ProjectInfoResult.Failed)
        assertEquals(throwable, result.throwable)
    }

}
