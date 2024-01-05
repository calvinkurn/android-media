package com.tokopedia.kyc_centralized.gotoKyc.viewModel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.kyc_centralized.ui.gotoKyc.domain.AccountLinkingStatusResult
import com.tokopedia.kyc_centralized.ui.gotoKyc.domain.AccountLinkingStatusUseCase
import com.tokopedia.kyc_centralized.ui.gotoKyc.domain.CheckEligibilityResult
import com.tokopedia.kyc_centralized.ui.gotoKyc.domain.CheckEligibilityUseCase
import com.tokopedia.kyc_centralized.ui.gotoKyc.domain.RegisterProgressiveResult
import com.tokopedia.kyc_centralized.ui.gotoKyc.domain.RegisterProgressiveUseCase
import com.tokopedia.kyc_centralized.ui.gotoKyc.main.bridging.BridgingAccountLinkingViewModel
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import io.mockk.coEvery
import io.mockk.mockk
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import util.getOrAwaitValue
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class BridgingAccountLinkingViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val dispatcher = CoroutineTestDispatchersProvider
    private lateinit var viewModel: BridgingAccountLinkingViewModel

    private val accountLinkingStatusUseCase = mockk<AccountLinkingStatusUseCase>(relaxed = true)
    private val checkEligibilityUseCase = mockk<CheckEligibilityUseCase>(relaxed = true)
    private val registerProgressiveUseCase = mockk<RegisterProgressiveUseCase>(relaxed = true)

    @Before
    fun setup() {
        viewModel = BridgingAccountLinkingViewModel(
            accountLinkingStatusUseCase,
            checkEligibilityUseCase,
            registerProgressiveUseCase,
            dispatcher
        )
    }

    @Test
    fun `when get status account linking then return linked`() {
        val projectId = 7
        val expected = AccountLinkingStatusResult.Linked

        coEvery { accountLinkingStatusUseCase(projectId) } returns expected
        viewModel.checkAccountLinkingStatus(projectId)

        val result = viewModel.accountLinkingStatus.getOrAwaitValue()
        assertTrue(result is AccountLinkingStatusResult.Linked)
        assertEquals(expected, result)
    }

    @Test
    fun `when get status account linking then return not linked`() {
        val projectId = 7
        val expected = AccountLinkingStatusResult.NotLinked

        coEvery { accountLinkingStatusUseCase(projectId) } returns expected
        viewModel.checkAccountLinkingStatus(projectId)

        val result = viewModel.accountLinkingStatus.getOrAwaitValue()
        assertTrue(result is AccountLinkingStatusResult.NotLinked)
        assertEquals(expected, result)
    }


    @Test
    fun `when get status account linking then return failed`() {
        val projectId = 7
        val throwable = Throwable()

        coEvery { accountLinkingStatusUseCase(projectId) } throws throwable
        viewModel.checkAccountLinkingStatus(projectId)

        val result = viewModel.accountLinkingStatus.getOrAwaitValue()
        assertTrue(result is AccountLinkingStatusResult.Failed)
        assertEquals(throwable, result.throwable)
    }

    @Test
    fun `when get eligibility then return progressive`() {
        val encryptedName = "H*****"
        val expected = CheckEligibilityResult.Progressive(encryptedName)

        coEvery { checkEligibilityUseCase.invoke() } returns expected
        viewModel.checkEligibility()

        val result = viewModel.checkEligibility.getOrAwaitValue()
        assertTrue(result is CheckEligibilityResult.Progressive)
        assertEquals(encryptedName, result.encryptedName)
    }

    @Test
    fun `when get eligibility then return non progressive`() {
        val expected = CheckEligibilityResult.NonProgressive

        coEvery { checkEligibilityUseCase.invoke() } returns expected
        viewModel.checkEligibility()

        val result = viewModel.checkEligibility.getOrAwaitValue()
        assertTrue(result is CheckEligibilityResult.NonProgressive)
    }

    @Test
    fun `when get eligibility then return awaiting approval gopay`() {
        val expected = CheckEligibilityResult.AwaitingApprovalGopay

        coEvery { checkEligibilityUseCase.invoke() } returns expected
        viewModel.checkEligibility()

        val result = viewModel.checkEligibility.getOrAwaitValue()
        assertTrue(result is CheckEligibilityResult.AwaitingApprovalGopay)
    }

    @Test
    fun `when get eligibility then failed`() {
        val throwable = Throwable()

        coEvery { checkEligibilityUseCase.invoke() } throws throwable
        viewModel.checkEligibility()

        val result = viewModel.checkEligibility.getOrAwaitValue()
        assertTrue(result is CheckEligibilityResult.Failed)
        assertEquals(throwable, result.throwable)
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
