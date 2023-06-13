package com.tokopedia.privacycenter.main.section.accountlinking

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.privacycenter.common.PrivacyCenterStateResult
import com.tokopedia.privacycenter.domain.AccountLinkingStatus
import com.tokopedia.privacycenter.domain.AccountLinkingUseCase
import com.tokopedia.privacycenter.ui.main.section.accountlinking.AccountLinkingViewModel
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.unit.test.ext.getOrAwaitValue
import io.mockk.coEvery
import io.mockk.mockk
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class AccountLinkingViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val dispatcher = CoroutineTestDispatchersProvider
    private lateinit var viewModel: AccountLinkingViewModel

    private val accountLinkingUseCase = mockk<AccountLinkingUseCase>(relaxed = true)

    @Before
    fun setup() {
        viewModel = AccountLinkingViewModel(
            accountLinkingUseCase,
            dispatcher
        )
    }

    @Test
    fun `when get status account then success`() {
        val expected = PrivacyCenterStateResult.Success(
            AccountLinkingStatus(true, "081234567", "25 Nov 2022")
        )
        val type = AccountLinkingUseCase.ACCOUNT_LINKING_TYPE

        coEvery { accountLinkingUseCase(type) } returns expected
        viewModel.getAccountLinkingStatus()

        val result = viewModel.accountLinkingState.getOrAwaitValue()
        assertTrue(result is PrivacyCenterStateResult.Success)
        assertEquals(expected, result)
    }

    @Test
    fun `when get status account then failed`() {
        val expected = PrivacyCenterStateResult.Fail<AccountLinkingStatus>(Throwable())
        val type = AccountLinkingUseCase.ACCOUNT_LINKING_TYPE

        coEvery { accountLinkingUseCase(type) } returns expected
        viewModel.getAccountLinkingStatus()

        val result = viewModel.accountLinkingState.getOrAwaitValue()
        assertTrue(result is PrivacyCenterStateResult.Fail)
        assertEquals(expected, result)
    }

    @Test
    fun `when get status account then throwable`() {
        val throwable = Throwable()
        val expected = PrivacyCenterStateResult.Fail<AccountLinkingStatus>(throwable)
        val type = AccountLinkingUseCase.ACCOUNT_LINKING_TYPE

        coEvery { accountLinkingUseCase(type) } throws throwable
        viewModel.getAccountLinkingStatus()

        val result = viewModel.accountLinkingState.getOrAwaitValue()
        assertTrue(result is PrivacyCenterStateResult.Fail)
        assertEquals(expected.error, result.error)
    }
}
