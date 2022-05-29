package com.tokopedia.affiliate.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.affiliate.adapter.AffiliateAdapterTypeFactory
import com.tokopedia.affiliate.model.request.OnboardAffiliateRequest
import com.tokopedia.affiliate.model.response.AffiliateValidateUserData
import com.tokopedia.affiliate.usecase.AffiliateValidateUserStatusUseCase
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
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class AffiliateRegistrationSharedViewModelTest {
    private val userSessionInterface: UserSessionInterface = mockk()
    private val affiliateValidateUserStatus: AffiliateValidateUserStatusUseCase = mockk()
    private var affiliateRegistrationSharedViewModel = spyk(AffiliateRegistrationSharedViewModel(
        userSessionInterface,
        affiliateValidateUserStatus))

    @get:Rule
    var rule = InstantTaskExecutorRule()

    @Before
    @Throws(Exception::class)
    fun setUp() {

        coEvery { userSessionInterface.userId } returns ""
        coEvery { userSessionInterface.email } returns ""

        MockKAnnotations.init(this)
        Dispatchers.setMain(TestCoroutineDispatcher())
    }

    @After
    @Throws(Exception::class)
    fun tearDown() {
        Dispatchers.resetMain()
    }

    /**************************** getAffiliateValidateUser() *******************************************/
    @Test
    fun getAffiliateValidateUserSignupStateTest() {
        val affiliateValidateUserData =
            AffiliateValidateUserData(AffiliateValidateUserData.ValidateAffiliateUserStatus(
                AffiliateValidateUserData.ValidateAffiliateUserStatus.Data(
                    null, isEligible = true, isRegistered = false, null)))
        coEvery { affiliateValidateUserStatus.validateUserStatus(any()) } returns affiliateValidateUserData

        affiliateRegistrationSharedViewModel.getAffiliateValidateUser()

        assertEquals(affiliateRegistrationSharedViewModel.getUserAction().value,
            AffiliateRegistrationSharedViewModel.UserAction.SignUpAction)
    }

    @Test
    fun getAffiliateValidateUserFraudStateTest() {
        val affiliateValidateUserData =
            AffiliateValidateUserData(AffiliateValidateUserData.ValidateAffiliateUserStatus(
                AffiliateValidateUserData.ValidateAffiliateUserStatus.Data(
                    null, isEligible = false, isRegistered = false, null)))
        coEvery { affiliateValidateUserStatus.validateUserStatus(any()) } returns affiliateValidateUserData

        affiliateRegistrationSharedViewModel.getAffiliateValidateUser()

        assertEquals(affiliateRegistrationSharedViewModel.getLoginScreenAction().value,
            AffiliateRegistrationSharedViewModel.UserAction.FraudAction)
    }

    @Test
    fun getAffiliateValidateUserRegisteredStateTest() {
        val affiliateValidateUserData =
            AffiliateValidateUserData(AffiliateValidateUserData.ValidateAffiliateUserStatus(
                AffiliateValidateUserData.ValidateAffiliateUserStatus.Data(
                    null, isEligible = true, isRegistered = true, null)))
        coEvery { affiliateValidateUserStatus.validateUserStatus(any()) } returns affiliateValidateUserData

        affiliateRegistrationSharedViewModel.getAffiliateValidateUser()

        assertEquals(affiliateRegistrationSharedViewModel.getLoginScreenAction().value,
            AffiliateRegistrationSharedViewModel.UserAction.RegisteredAction)
    }

    @Test
    fun getAffiliateValidateUserProgressBarTest() {
        val affiliateValidateUserData =
            AffiliateValidateUserData(AffiliateValidateUserData.ValidateAffiliateUserStatus(
                AffiliateValidateUserData.ValidateAffiliateUserStatus.Data(
                    null, isEligible = true, isRegistered = true, null)))
        coEvery { affiliateValidateUserStatus.validateUserStatus(any()) } returns affiliateValidateUserData

        affiliateRegistrationSharedViewModel.getAffiliateValidateUser()

        assertEquals(affiliateRegistrationSharedViewModel.getProgressBar().value, false)
    }

    @Test
    fun getValidateUserException() {
        val throwable = Throwable("Validate Data Exception")
        coEvery { affiliateValidateUserStatus.validateUserStatus(any()) } throws throwable

        affiliateRegistrationSharedViewModel.getAffiliateValidateUser()

        assertEquals(affiliateRegistrationSharedViewModel.getErrorMessage().value, throwable)
    }

    /**************************** getUserSessionTest() *******************************************/
    @Test
    fun userSessionNameTest() {
        coEvery { userSessionInterface.name } returns "toko"
        assertEquals(affiliateRegistrationSharedViewModel.getUserName(), "toko")
    }

    @Test
    fun userSessionEmailTest() {
        assertEquals(affiliateRegistrationSharedViewModel.getUserEmail(), "")

    }

    @Test
    fun userSessionIsLoggedInTest() {
        val isLoggedIn = true
        coEvery { userSessionInterface.isLoggedIn } returns isLoggedIn
        assertEquals(affiliateRegistrationSharedViewModel.isUserLoggedIn(), isLoggedIn)

    }

    @Test
    fun userSessionProfileUrlTest() {
        val profile = "www.tokopedia.com"
        coEvery { userSessionInterface.profilePicture } returns profile
        assertEquals(affiliateRegistrationSharedViewModel.getUserProfilePicture(), profile)

    }
    /**************************** navigationFunctionTest() *******************************************/
    @Test
    fun navToPortfolioTest() {
        affiliateRegistrationSharedViewModel.navigateToPortFolio()
        assertEquals(affiliateRegistrationSharedViewModel.getUserAction().value,
            AffiliateRegistrationSharedViewModel.UserAction.NaigateToPortFolio)
    }
    @Test
    fun navToTermsAndConditionTest() {
        affiliateRegistrationSharedViewModel.navigateToTermsFragment(arrayListOf())
        assertEquals(affiliateRegistrationSharedViewModel.getUserAction().value,
            AffiliateRegistrationSharedViewModel.UserAction.NaigateToTermsAndFragment)
    }
    /**************************** viewModelVariableTest() *******************************************/
    @Test
    fun affiliatePortfolioDataTest() {
        affiliateRegistrationSharedViewModel.affiliatePortfolioData.value = arrayListOf()
        assertEquals(affiliateRegistrationSharedViewModel.affiliatePortfolioData.value,
            arrayListOf<Visitable<AffiliateAdapterTypeFactory>>())
    }

    @Test
    fun isFieldErrorTest() {
        affiliateRegistrationSharedViewModel.isFieldError.value = false
        assertEquals(affiliateRegistrationSharedViewModel.isFieldError.value,
            false)
    }
    @Test
    fun listOfChannelTest() {
        affiliateRegistrationSharedViewModel.navigateToTermsFragment(arrayListOf())
        assertEquals(affiliateRegistrationSharedViewModel.listOfChannels,
            arrayListOf<OnboardAffiliateRequest.OnboardAffiliateChannelRequest>())
    }


}