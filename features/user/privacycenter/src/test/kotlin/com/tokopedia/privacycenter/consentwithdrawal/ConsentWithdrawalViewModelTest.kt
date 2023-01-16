package com.tokopedia.privacycenter.consentwithdrawal

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.privacycenter.common.PrivacyCenterStateResult
import com.tokopedia.privacycenter.data.ConsentPurposeGroupDataModel
import com.tokopedia.privacycenter.data.SubmitConsentDataModel
import com.tokopedia.privacycenter.domain.GetConsentPurposeByGroupUseCase
import com.tokopedia.privacycenter.domain.SubmitConsentPreferenceUseCase
import com.tokopedia.privacycenter.ui.consentwithdrawal.ConsentWithdrawalConst
import com.tokopedia.privacycenter.ui.consentwithdrawal.ConsentWithdrawalViewModel
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.unit.test.ext.getOrAwaitValue
import io.mockk.coEvery
import io.mockk.mockk
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class ConsentWithdrawalViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var viewModelTest: ConsentWithdrawalViewModel
    private val getConsentPurposeByGroupUseCase = mockk<GetConsentPurposeByGroupUseCase>(relaxed = true)
    private val submitConsentPurposeByGroupUseCase = mockk<SubmitConsentPreferenceUseCase>(relaxed = true)

    @Before
    fun setup() {
        viewModelTest = ConsentWithdrawalViewModel(
            getConsentPurposeByGroupUseCase,
            submitConsentPurposeByGroupUseCase,
            CoroutineTestDispatchersProvider
        )
    }

    @Test
    fun `get consent purpose by group id then success flow`() {
        val successResponse = ConsentPurposeGroupDataModel(
            isSuccess = true
        )
        val mockResponse = PrivacyCenterStateResult.Success(successResponse)

        coEvery {
            getConsentPurposeByGroupUseCase(any())
        } returns mockResponse
        viewModelTest.getConsentPurposeByGroup(0)

        val result = viewModelTest.consentPurpose.getOrAwaitValue()
        assertTrue(result is PrivacyCenterStateResult.Success)
        assertTrue(result.data.isSuccess)
    }

    @Test
    fun `get consent purpose by group id - failed flow - from BE`() {
        val failedResponse = ConsentPurposeGroupDataModel(
            isSuccess = false,
            errorMessages = listOf(
                "Opss!, Something wrong!"
            )
        )
        val mockResponse = PrivacyCenterStateResult.Fail<ConsentPurposeGroupDataModel>(
            Throwable(failedResponse.errorMessages.toString())
        )

        coEvery {
            getConsentPurposeByGroupUseCase(any())
        } returns mockResponse
        viewModelTest.getConsentPurposeByGroup(0)

        val result = viewModelTest.consentPurpose.getOrAwaitValue()
        assertTrue(result is PrivacyCenterStateResult.Fail)
        assertEquals(failedResponse.errorMessages.toString(), result.error.message.toString())
    }

    @Test
    fun `get consent purpose by group id - from Exception`() {
        val throwable = Throwable()

        coEvery {
            getConsentPurposeByGroupUseCase(any())
        } throws throwable
        viewModelTest.getConsentPurposeByGroup(0)

        val result = viewModelTest.consentPurpose.getOrAwaitValue()
        assertTrue(result is PrivacyCenterStateResult.Fail)
        assertEquals(throwable, result.error)
    }

    @Test
    fun `submit consent preference - success flow`() {
        val purposeId = "0"
        val transactionType = ConsentWithdrawalConst.OPT_OUT
        val successResponse = SubmitConsentDataModel(
            isSuccess = true
        )
        val mockResponse = PrivacyCenterStateResult.Success(successResponse)

        coEvery {
            submitConsentPurposeByGroupUseCase(any())
        } returns mockResponse
        viewModelTest.submitConsentPreference(purposeId, transactionType)

        val result = viewModelTest.submitConsentPreference.getOrAwaitValue()
        assertTrue(result is PrivacyCenterStateResult.Success)
        assertTrue(result.data.isSuccess)
    }

    @Test
    fun `submit consent preference - failed flow - from BE`() {
        val purposeId = "0"
        val transactionType = ConsentWithdrawalConst.OPT_OUT
        val failedResponse = SubmitConsentDataModel(
            isSuccess = false,
            errorMessages = listOf(
                "Opss!, Something wrong!"
            )
        )
        val throwable = Throwable(failedResponse.errorMessages.toString())
        val mockResponse = PrivacyCenterStateResult.Fail<SubmitConsentDataModel>(throwable)

        coEvery {
            submitConsentPurposeByGroupUseCase(any())
        } returns mockResponse
        viewModelTest.submitConsentPreference(purposeId, transactionType)

        val result = viewModelTest.submitConsentPreference.getOrAwaitValue()
        assertTrue(result is PrivacyCenterStateResult.Fail)
        assertEquals(throwable.message, result.error.message)
    }

    @Test
    fun `submit consent preference - from Exception`() {
        val throwable = Throwable()
        val purposeId = "0"
        val transactionType = ConsentWithdrawalConst.OPT_OUT

        coEvery {
            submitConsentPurposeByGroupUseCase(any())
        } throws throwable
        viewModelTest.submitConsentPreference(purposeId, transactionType)

        val result = viewModelTest.submitConsentPreference.getOrAwaitValue()
        assertTrue(result is PrivacyCenterStateResult.Fail)
        assertEquals(throwable, result.error)
    }
}
