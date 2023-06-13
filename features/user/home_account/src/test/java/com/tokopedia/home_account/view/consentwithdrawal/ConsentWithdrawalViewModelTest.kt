package com.tokopedia.home_account.view.consentwithdrawal

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.tokopedia.home_account.consentWithdrawal.common.TransactionType
import com.tokopedia.home_account.consentWithdrawal.data.ConsentPurposeGroupDataModel
import com.tokopedia.home_account.consentWithdrawal.data.GetConsentPurposeDataModel
import com.tokopedia.home_account.consentWithdrawal.data.SubmitConsentDataModel
import com.tokopedia.home_account.consentWithdrawal.data.SubmitConsentPreferenceDataModel
import com.tokopedia.home_account.consentWithdrawal.domain.GetConsentPurposeByGroupUseCase
import com.tokopedia.home_account.consentWithdrawal.domain.SubmitConsentPreferenceUseCase
import com.tokopedia.home_account.consentWithdrawal.viewmodel.ConsentWithdrawalViewModel
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import io.mockk.coEvery
import io.mockk.mockk
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class ConsentWithdrawalViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private var viewModelTest: ConsentWithdrawalViewModel? = null
    private val getConsentPurposeByGroupUseCase = mockk<GetConsentPurposeByGroupUseCase>(relaxed = true)
    private val submitConsentPurposeByGroupUseCase = mockk<SubmitConsentPreferenceUseCase>(relaxed = true)
    private val consentPurposeObserver = mockk<Observer<Result<ConsentPurposeGroupDataModel>>>(relaxed = true)
    private val submitConsentPreferenceObserver = mockk<Observer<Result<SubmitConsentDataModel>>>(relaxed = true)

    private val throwable = mockk<Throwable>(relaxed = true)

    @Before
    fun setup() {
        viewModelTest = ConsentWithdrawalViewModel(
            getConsentPurposeByGroupUseCase,
            submitConsentPurposeByGroupUseCase,
            CoroutineTestDispatchersProvider
        )
    }

    @After
    fun tearDown() {
        viewModelTest?.consentPurpose?.removeObserver(consentPurposeObserver)
        viewModelTest?.submitConsentPreference?.removeObserver(submitConsentPreferenceObserver)
    }

    @Test
    fun `get consent purpose by group id - success flow`() {
        val successResponse = ConsentPurposeGroupDataModel(
            isSuccess = true
        )
        val mockResponse = GetConsentPurposeDataModel(successResponse)

        coEvery {
            getConsentPurposeByGroupUseCase(any())
        } returns mockResponse

        viewModelTest?.getConsentPurposeByGroup(0)

        assert(viewModelTest?.consentPurpose?.value is Success)
        assert((viewModelTest?.consentPurpose?.value as Success).data.isSuccess)
    }

    @Test
    fun `get consent purpose by group id - failed flow - from BE`() {
        val failedResponse = ConsentPurposeGroupDataModel(
            isSuccess = false,
            errorMessages = listOf(
                "Opss!, Something wrong!"
            )
        )
        val mockResponse = GetConsentPurposeDataModel(failedResponse)

        coEvery {
            getConsentPurposeByGroupUseCase(any())
        } returns mockResponse

        viewModelTest?.getConsentPurposeByGroup(0)

        assert(viewModelTest?.consentPurpose?.value is Fail)
        assert((viewModelTest?.consentPurpose?.value as Fail).throwable.message.toString() == failedResponse.errorMessages.toString())
    }

    @Test
    fun `get consent purpose by group id - from Exception`() {
        coEvery {
            getConsentPurposeByGroupUseCase(any())
        } throws throwable

        viewModelTest?.getConsentPurposeByGroup(0)

        assert(viewModelTest?.consentPurpose?.value is Fail)
        assert((viewModelTest?.consentPurpose?.value as Fail).throwable == throwable)
    }

    @Test
    fun `submit consent preference - success flow`() {
        val successResponse = SubmitConsentDataModel(
            isSuccess = true
        )
        val mockResponse = SubmitConsentPreferenceDataModel(successResponse)

        coEvery {
            submitConsentPurposeByGroupUseCase(any())
        } returns mockResponse

        viewModelTest?.submitConsentPreference(0, "", TransactionType.OPT_IN)

        assert(viewModelTest?.submitConsentPreference?.value is Success)
        assert((viewModelTest?.submitConsentPreference?.value as Success).data.isSuccess)
    }

    @Test
    fun `submit consent preference - failed flow - from BE`() {
        val failedResponse = SubmitConsentDataModel(
            isSuccess = false,
            errorMessages = listOf(
                "Opss!, Something wrong!"
            )
        )
        val mockResponse = SubmitConsentPreferenceDataModel(failedResponse)

        coEvery {
            submitConsentPurposeByGroupUseCase(any())
        } returns mockResponse

        viewModelTest?.submitConsentPreference(0, "", TransactionType.OPT_IN)

        assert(viewModelTest?.submitConsentPreference?.value is Fail)
        assert((viewModelTest?.submitConsentPreference?.value as Fail).throwable.message.toString() == failedResponse.errorMessages.toString())
    }

    @Test
    fun `submit consent preference - from Exception`() {
        coEvery {
            submitConsentPurposeByGroupUseCase(any())
        } throws throwable

        viewModelTest?.submitConsentPreference(0, "", TransactionType.OPT_IN)

        assert(viewModelTest?.submitConsentPreference?.value is Fail)
        assert((viewModelTest?.submitConsentPreference?.value as Fail).throwable == throwable)
    }
}
