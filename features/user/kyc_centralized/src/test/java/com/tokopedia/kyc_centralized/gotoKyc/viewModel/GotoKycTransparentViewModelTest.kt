package com.tokopedia.kyc_centralized.gotoKyc.viewModel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.kyc_centralized.ui.gotoKyc.domain.AccountLinkingStatusResult
import com.tokopedia.kyc_centralized.ui.gotoKyc.domain.AccountLinkingStatusUseCase
import com.tokopedia.kyc_centralized.ui.gotoKyc.domain.CheckEligibilityResult
import com.tokopedia.kyc_centralized.ui.gotoKyc.domain.CheckEligibilityUseCase
import com.tokopedia.kyc_centralized.ui.gotoKyc.domain.ProjectInfoResult
import com.tokopedia.kyc_centralized.ui.gotoKyc.domain.ProjectInfoUseCase
import com.tokopedia.kyc_centralized.ui.gotoKyc.transparent.GotoKycTransparentViewModel
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import io.mockk.coEvery
import io.mockk.mockk
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import util.getOrAwaitValue
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class GotoKycTransparentViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val dispatcher = CoroutineTestDispatchersProvider
    private lateinit var viewModel: GotoKycTransparentViewModel

    private val accountLinkingStatusUseCase = mockk<AccountLinkingStatusUseCase>(relaxed = true)
    private val projectInfoUseCase = mockk<ProjectInfoUseCase>(relaxed = true)
    private val checkEligibilityUseCase = mockk<CheckEligibilityUseCase>(relaxed = true)

    @Before
    fun setup() {
        viewModel = GotoKycTransparentViewModel(projectInfoUseCase, checkEligibilityUseCase, accountLinkingStatusUseCase, dispatcher)
    }

    @Test
    fun `when set projectId then get projectId then return correct projectId`() {
        val expectedProjectId = "7"

        viewModel.setProjectId(expectedProjectId)

        val result = viewModel.projectId
        assertEquals(expectedProjectId, result)
    }

    @Test
    fun `when set source then get source then return correct projectId`() {
        val expectedSource = "Power Merchant"

        viewModel.setSource(expectedSource)

        val result = viewModel.source
        assertEquals(expectedSource, result)
    }

    @Test
    fun `when get user info then return toko kyc`() {
        val projectId = 7
        val expected = ProjectInfoResult.TokoKyc

        coEvery { projectInfoUseCase(any()) } returns expected
        viewModel.getProjectInfo(projectId)

        val result = viewModel.projectInfo.getOrAwaitValue()
        assertTrue(result is ProjectInfoResult.TokoKyc)
    }

    @Test
    fun `when get user info then return status submission`() {
        val projectId = 7
        val status = "0"
        val rejectionReason = "Terjadi Kesalahan"
        val waitMessage = "Tunggu 24 Jam ya!"
        val expected = ProjectInfoResult.StatusSubmission(
            status, rejectionReason, waitMessage
        )

        coEvery { projectInfoUseCase(any()) } returns expected
        viewModel.getProjectInfo(projectId)

        val result = viewModel.projectInfo.getOrAwaitValue()
        assertTrue(result is ProjectInfoResult.StatusSubmission)
        assertEquals(status, result.status)
        assertEquals(rejectionReason, result.rejectionReason)
        assertEquals(waitMessage, result.waitMessage)
    }

    @Test
    fun `when get user info then return not verified and account linked`() {
        val projectId = 7
        val isAccountLinked = true
        val expected = ProjectInfoResult.NotVerified(isAccountLinked)

        coEvery { projectInfoUseCase(any()) } returns expected
        viewModel.getProjectInfo(projectId)

        val result = viewModel.projectInfo.getOrAwaitValue()
        val resultIsAccountLinked = viewModel.isAccountLinked
        assertTrue(result is ProjectInfoResult.NotVerified)
        assertEquals(isAccountLinked, result.isAccountLinked)
        assertEquals(isAccountLinked, resultIsAccountLinked)
    }

    @Test
    fun `when get user info then return not verified and account not linked`() {
        val projectId = 7
        val isAccountLinked = false
        val expected = ProjectInfoResult.NotVerified(isAccountLinked)

        coEvery { projectInfoUseCase(any()) } returns expected
        viewModel.getProjectInfo(projectId)

        val result = viewModel.projectInfo.getOrAwaitValue()
        assertTrue(result is ProjectInfoResult.NotVerified)
        assertEquals(isAccountLinked, result.isAccountLinked)
    }

    @Test
    fun `when get user info then return failed`() {
        val projectId = 7
        val throwable = Throwable()
        coEvery { projectInfoUseCase(any()) } throws throwable
        viewModel.getProjectInfo(projectId)

        val result = viewModel.projectInfo.getOrAwaitValue()
        assertTrue(result is ProjectInfoResult.Failed)
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
    fun `when get status account linking then return linked`() {
        val projectId = 7
        val expected = AccountLinkingStatusResult.Linked

        coEvery { accountLinkingStatusUseCase(projectId) } returns expected
        viewModel.accountLinkingStatus(projectId)

        val result = viewModel.accountLinkingStatus.getOrAwaitValue()
        assertTrue(result is AccountLinkingStatusResult.Linked)
        assertEquals(expected, result)
    }

    @Test
    fun `when get status account linking then return not linked`() {
        val projectId = 7
        val expected = AccountLinkingStatusResult.NotLinked

        coEvery { accountLinkingStatusUseCase(projectId) } returns expected
        viewModel.accountLinkingStatus(projectId)

        val result = viewModel.accountLinkingStatus.getOrAwaitValue()
        assertTrue(result is AccountLinkingStatusResult.NotLinked)
        assertEquals(expected, result)
    }


    @Test
    fun `when get status account linking then return failed`() {
        val projectId = 7
        val throwable = Throwable()

        coEvery { accountLinkingStatusUseCase(projectId) } throws throwable
        viewModel.accountLinkingStatus(projectId)

        val result = viewModel.accountLinkingStatus.getOrAwaitValue()
        assertTrue(result is AccountLinkingStatusResult.Failed)
        assertEquals(throwable, result.throwable)
    }
}
