package com.tokopedia.payment.setting.list.view.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.tokopedia.payment.setting.list.domain.GetCreditCardListUseCase
import com.tokopedia.payment.setting.list.model.CreditCardData
import com.tokopedia.payment.setting.list.model.PaymentQueryResponse
import com.tokopedia.payment.setting.list.model.PaymentSignature
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import org.junit.Before
import org.junit.Test

import org.junit.Assert.*
import org.junit.Rule

@ExperimentalCoroutinesApi
class SettingsListViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val getCreditCardListUseCase = mockk<GetCreditCardListUseCase>(relaxed = true)
    private val userSession = mockk<UserSessionInterface>(relaxed = true)
    private val dispatcher = TestCoroutineDispatcher()
    private lateinit var viewModel: SettingsListViewModel

    @Before
    fun setUp() {
        viewModel = SettingsListViewModel(getCreditCardListUseCase,
                userSession,
                dispatcher)
    }

    @Test
    fun `getCreditCardList fail`() {
        val mockThrowable = mockk<Throwable>("fail")
        coEvery { getCreditCardListUseCase.getCreditCardList(any(), any()) } answers {
            secondArg<(Throwable) -> Unit>().invoke(mockThrowable)
        }
        viewModel.getCreditCardList()
        assert(viewModel.paymentQueryResultLiveData.value is Fail)
    }

    @Test
    fun `getCreditCardList success`() {
        val signature = mockk<PaymentSignature>()
        val cardData = mockk<CreditCardData>()
        val data = PaymentQueryResponse(signature, cardData)
        coEvery { getCreditCardListUseCase.getCreditCardList(any(), any()) } answers {
            firstArg<(PaymentQueryResponse) -> Unit>().invoke(data)
        }
        viewModel.getCreditCardList()
        assert(viewModel.paymentQueryResultLiveData.value is Success)
    }

    @Test
    fun `checkVerificationPhone true`() {
        every { userSession.isMsisdnVerified } answers { true }
        viewModel.checkVerificationPhone()
        assert(viewModel.phoneVerificationStatusLiveData.value == true)
    }

    @Test
    fun `checkVerificationPhone false`() {
        every { userSession.isMsisdnVerified } answers { false }
        viewModel.checkVerificationPhone()
        assert(viewModel.phoneVerificationStatusLiveData.value == false)
    }
}