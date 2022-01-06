package com.tokopedia.affiliate.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.affiliate.model.response.AffiliateOnBoardingData
import com.tokopedia.affiliate.usecase.AffiliateOnBoardingUseCase
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.mockk
import io.mockk.spyk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.util.ArrayList

@ExperimentalCoroutinesApi
class AffiliateTermsAndConditionViewModelTest{
    private val userSessionInterface: UserSessionInterface = mockk()
    private val affiliateOnBoardingUseCase: AffiliateOnBoardingUseCase = mockk()
    private val affiliateTermViewModel = spyk(AffiliateTermsAndConditionViewModel (userSessionInterface,affiliateOnBoardingUseCase))
    @get:Rule
    var rule = InstantTaskExecutorRule()

    @Before
    @Throws(Exception::class)
    fun setUp() {

        MockKAnnotations.init(this)
        Dispatchers.setMain(TestCoroutineDispatcher())
    }

    @After
    @Throws(Exception::class)
    fun tearDown() {
        Dispatchers.resetMain()
    }

    /**************************** affiliateOnBoarding() *******************************************/
    @Test
    fun `Affiliate Onboarding`() {
        val affiliateOnboarding : AffiliateOnBoardingData.OnBoardAffiliate? = mockk(relaxed = true)
        coEvery { affiliateOnBoardingUseCase.affiliateOnBoarding(any()) } returns affiliateOnboarding

        affiliateTermViewModel.affiliateOnBoarding(ArrayList())

        assertEquals(affiliateTermViewModel.getOnBoardingData().value , affiliateOnboarding)
        assertEquals(affiliateTermViewModel.progressBar().value,false)

    }

    @Test
    fun `Affiliate Onboarding Exception`() {
        val exception = java.lang.Exception("Validate Data Exception")
        coEvery { affiliateOnBoardingUseCase.affiliateOnBoarding(any()) } throws exception

        affiliateTermViewModel.affiliateOnBoarding(ArrayList())

        assertEquals(affiliateTermViewModel.getErrorMessage().value, exception)
        assertEquals(affiliateTermViewModel.progressBar().value,false)
    }
}