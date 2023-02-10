package com.tokopedia.privacycenter.main.section.consentwithdrawal

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.privacycenter.common.PrivacyCenterStateResult
import com.tokopedia.privacycenter.data.ConsentGroupListDataModel
import com.tokopedia.privacycenter.data.GetConsentGroupListDataModel
import com.tokopedia.privacycenter.domain.GetConsentGroupListUseCase
import com.tokopedia.privacycenter.ui.main.section.consentwithdrawal.ConsentWithdrawalSectionViewModel
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.unit.test.ext.getOrAwaitValue
import io.mockk.coEvery
import io.mockk.mockk
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class ConsentWithdrawalSectionViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val dispatcher = CoroutineTestDispatchersProvider
    private lateinit var viewModel: ConsentWithdrawalSectionViewModel

    private val useCase = mockk<GetConsentGroupListUseCase>(relaxed = true)

    @Before
    fun setup() {
        viewModel = ConsentWithdrawalSectionViewModel(
            useCase,
            dispatcher
        )
    }

    @Test
    fun `get consent group then success`() {
        val expected = PrivacyCenterStateResult.Success(GetConsentGroupListDataModel().consentGroupList)

        coEvery { useCase(Unit) } returns expected
        viewModel.getConsentGroupList()

        val result = viewModel.getConsentGroupList.getOrAwaitValue()
        assertTrue(result is PrivacyCenterStateResult.Success)
        assertEquals(expected, result)
    }

    @Test
    fun `get consent group then failed`() {
        val expected = PrivacyCenterStateResult.Fail<ConsentGroupListDataModel>(Throwable())

        coEvery { useCase(Unit) } returns expected
        viewModel.getConsentGroupList()

        val result = viewModel.getConsentGroupList.getOrAwaitValue()
        assertTrue(result is PrivacyCenterStateResult.Fail)
        assertEquals(expected, result)
    }

    @Test
    fun `get consent group then throwable`() {
        val throwable = Throwable()
        val expected = PrivacyCenterStateResult.Fail<ConsentGroupListDataModel>(throwable)

        coEvery { useCase(Unit) } throws throwable
        viewModel.getConsentGroupList()

        val result = viewModel.getConsentGroupList.getOrAwaitValue()
        assertTrue(result is PrivacyCenterStateResult.Fail)
        assertEquals(expected.error, result.error)
    }
}
