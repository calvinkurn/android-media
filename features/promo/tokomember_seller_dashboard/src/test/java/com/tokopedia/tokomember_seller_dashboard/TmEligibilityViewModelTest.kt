package com.tokopedia.tokomember_seller_dashboard

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.tokomember_seller_dashboard.domain.TmOnBoardingCheckUsecase
import com.tokopedia.tokomember_seller_dashboard.domain.TokomemberAuthenticatedUsecase
import com.tokopedia.tokomember_seller_dashboard.domain.TokomemberEligibilityUsecase
import com.tokopedia.tokomember_seller_dashboard.model.CheckEligibility
import com.tokopedia.tokomember_seller_dashboard.model.MembershipData
import com.tokopedia.tokomember_seller_dashboard.model.SellerData
import com.tokopedia.tokomember_seller_dashboard.view.viewmodel.TmEligibilityViewModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class TmEligibilityViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val mockThrowable = Throwable(message = "exception")
    private val dispatcher = TestCoroutineDispatcher()
    private lateinit var viewModel: TmEligibilityViewModel
    private val tokomemberEligibilityUsecase = mockk<TokomemberEligibilityUsecase>(relaxed = true)
    private val tokomemberAuthenticatedUsecase = mockk<TokomemberAuthenticatedUsecase>(relaxed = true)
    private val tmOnBoardingCheckUsecase = mockk<TmOnBoardingCheckUsecase>(relaxed = true)


    @Before
    fun setUp() {
        viewModel = TmEligibilityViewModel(
            tokomemberEligibilityUsecase,
            tokomemberAuthenticatedUsecase,
            tmOnBoardingCheckUsecase,
           dispatcher
        )
    }

    @Test
    fun successEligibility() {
        val data = mockk<CheckEligibility>(relaxed = true)
        coEvery {
            tokomemberEligibilityUsecase.checkEligibility(any(), any(), 0,false)
        } coAnswers {
            firstArg<(CheckEligibility) -> Unit>().invoke(data)
        }
        viewModel.checkEligibility(0,false)

        Assert.assertEquals(
            (viewModel.eligibilityCheckResultLiveData.value as Success).data,
            data
        )
    }

    @Test
    fun failEligibility() {
        coEvery {
            tokomemberEligibilityUsecase.checkEligibility(any(), any(), 0,false)
        } coAnswers {
            secondArg<(Throwable) -> Unit>().invoke(mockThrowable)
        }
        viewModel.checkEligibility(0,false)
        Assert.assertEquals(
            (viewModel.eligibilityCheckResultLiveData.value as Fail).throwable,
            mockThrowable
        )
    }

    @Test
    fun successSellerInfo(){
        val data = mockk<SellerData>(relaxed = true)
        coEvery {
            tokomemberAuthenticatedUsecase.getSellerInfo(any(), any())
        } coAnswers {
            firstArg<(SellerData) -> Unit>().invoke(data)
        }
        viewModel.getSellerInfo()

        Assert.assertEquals(
            (viewModel.sellerInfoResultLiveData.value as Success).data,
            data
        )
    }

    @Test
    fun failProgramForm() {
        coEvery {
            tokomemberAuthenticatedUsecase.getSellerInfo(any(), any())
        } coAnswers {
            secondArg<(Throwable) -> Unit>().invoke(mockThrowable)
        }
        viewModel.getSellerInfo()
        Assert.assertEquals(
            (viewModel.sellerInfoResultLiveData.value as Fail).throwable,
            mockThrowable
        )
    }

    @Test
    fun getOnboardingInfoSuccess(){
       val data = mockk<MembershipData>()
        every {
            tmOnBoardingCheckUsecase.getMemberOnboardingInfo(any(),any(),any())
        } answers {
            firstArg<(MembershipData) -> Unit>().invoke(data)
        }
        viewModel.getOnboardingInfo(0)
        Assert.assertEquals(
            (viewModel.tokomemberOnboardingResultLiveData.value as Success).data,
            data
        )
    }

    @Test
    fun getOnboardingInfoFailure(){
        every {
            tmOnBoardingCheckUsecase.getMemberOnboardingInfo(any(),any(),any())
        } answers {
            secondArg<(Throwable) -> Unit>().invoke(mockThrowable)
        }
        viewModel.getOnboardingInfo(0)
        Assert.assertEquals(
            (viewModel.tokomemberOnboardingResultLiveData.value as Fail).throwable,
            mockThrowable
        )
    }

}
