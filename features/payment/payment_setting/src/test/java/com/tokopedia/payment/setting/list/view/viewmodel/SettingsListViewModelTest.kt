package com.tokopedia.payment.setting.list.view.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.tokopedia.payment.setting.list.domain.GetCreditCardListUseCase
import com.tokopedia.payment.setting.list.domain.GetSettingBannerUseCase
import com.tokopedia.payment.setting.list.model.CreditCardData
import com.tokopedia.payment.setting.list.model.PaymentQueryResponse
import com.tokopedia.payment.setting.list.model.PaymentSignature
import com.tokopedia.payment.setting.list.model.SettingBannerModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.*
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import org.assertj.core.api.Assertions
import org.hamcrest.core.IsInstanceOf
import org.junit.After
import org.junit.Before
import org.junit.Test

import org.junit.Assert.*
import org.junit.Rule

@ExperimentalCoroutinesApi
class SettingsListViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val getCreditCardListUseCase = mockk<GetCreditCardListUseCase>(relaxed = true)
    private val getSettingBannerUseCase = mockk<GetSettingBannerUseCase>(relaxed = true)
    private val userSession = mockk<UserSessionInterface>(relaxed = true)
    private val dispatcher = TestCoroutineDispatcher()
    private lateinit var viewModel: SettingsListViewModel

    private var bannerAndCardListResultObserver =
        mockk<Observer<Pair<Result<PaymentQueryResponse>?, Result<SettingBannerModel>?>>>(relaxed = true)

    @Before
    fun setUp() {
        viewModel = SettingsListViewModel(
            getCreditCardListUseCase,
            getSettingBannerUseCase,
            userSession,
            dispatcher,
        )

        viewModel.bannerAndCardListResultLiveData.observeForever(bannerAndCardListResultObserver)
    }

    @After
    fun cleanUp() {
        viewModel.bannerAndCardListResultLiveData.removeObserver(bannerAndCardListResultObserver)
    }

    @Test
    fun `getCreditCardList fail`() {
        val mockThrowable = mockk<Throwable>("fail")
        coEvery { getCreditCardListUseCase.getCreditCardList(any(), any()) } answers {
            secondArg<(Throwable) -> Unit>().invoke(mockThrowable)
        }

        viewModel.getCreditCardList()

        val cardLiveData = viewModel.bannerAndCardListResultLiveData.value?.first
        assert(cardLiveData is Fail)
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

        val cardLiveData = viewModel.bannerAndCardListResultLiveData.value?.first
        assert(cardLiveData is Success)
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

    @Test
    fun `getSettingBanner fail`() {
        val mockThrowable = mockk<Throwable>("fail")
        coEvery {
            getSettingBannerUseCase.getSettingBanner(any(), any())
        } answers {
            secondArg<(Throwable) -> Unit>().invoke(mockThrowable)
        }

        viewModel.getSettingBanner()

        val bannerLiveData = viewModel.bannerAndCardListResultLiveData.value?.second
        assert(bannerLiveData is Fail)
    }

    @Test
    fun `getSettingBanner success`() {
        val bannerModel = SettingBannerModel()
        coEvery {
            getSettingBannerUseCase.getSettingBanner(any(), any())
        } answers {
            firstArg<(SettingBannerModel) -> Unit>().invoke(bannerModel)
        }

        viewModel.getSettingBanner()

        val a = viewModel.bannerAndCardListResultLiveData.value

        val bannerLiveData = viewModel.bannerAndCardListResultLiveData.value?.second
        assert(bannerLiveData is Success)
        assert((bannerLiveData as Success<SettingBannerModel>)
            .data == bannerModel
        )
    }
}
