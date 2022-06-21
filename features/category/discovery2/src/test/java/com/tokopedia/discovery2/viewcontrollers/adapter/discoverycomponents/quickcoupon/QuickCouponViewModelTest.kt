package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.quickcoupon

import android.app.Application
import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.data.quickcouponresponse.ClickCouponData
import com.tokopedia.discovery2.data.quickcouponresponse.PhoneVerificationResponse
import com.tokopedia.discovery2.data.quickcouponresponse.QuickCouponDetailResponse
import com.tokopedia.discovery2.usecase.quickcouponusecase.QuickCouponUseCase
import com.tokopedia.user.session.UserSession
import io.mockk.*
import junit.framework.TestCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class QuickCouponViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()
    private val componentsItem: ComponentsItem = mockk(relaxed = true)
    private val application: Application = mockk()
    private var context: Context = mockk()
    private var viewModel: QuickCouponViewModel =
        spyk(QuickCouponViewModel(application, componentsItem, 99))

    private val quickCouponUseCase: QuickCouponUseCase by lazy {
        mockk()
    }

    @Before
    @Throws(Exception::class)
    fun setup() {
        MockKAnnotations.init(this)
        Dispatchers.setMain(TestCoroutineDispatcher())

        mockkConstructor(UserSession::class)
        every { application.applicationContext } returns context
    }

    @After
    @Throws(Exception::class)
    fun tearDown() {
        Dispatchers.resetMain()
        unmockkConstructor(UserSession::class)
    }

    @Test
    fun `test for useCase`() {
        val viewModel: QuickCouponViewModel =
                spyk(QuickCouponViewModel(application, componentsItem, 99))

        val quickCouponUseCase = mockk<QuickCouponUseCase>()

        viewModel.quickCouponUseCase = quickCouponUseCase

        assert(viewModel.quickCouponUseCase === quickCouponUseCase)
    }

    /**************************** test for fetchCouponDetailData() *******************************************/
    @Test
    fun `test for fetchCouponDetailData when getCouponDetail returns error`() {
        viewModel.quickCouponUseCase = quickCouponUseCase
        coEvery {
            quickCouponUseCase.getCouponDetail(componentsItem.pagePath)
        } throws Exception("Error")

        viewModel.onAttachToViewHolder()

        coVerify { quickCouponUseCase.getCouponDetail(componentsItem.pagePath) }

    }

    @Test
    fun `test for fetchCouponDetailData when getCouponDetail returns quickCouponDetailResponse`() {
        viewModel.quickCouponUseCase = quickCouponUseCase
        val quickCouponDetailResponse: QuickCouponDetailResponse = mockk(relaxed = true)
        every { quickCouponDetailResponse.clickCouponData?.isApplicable } returns true
        coEvery {
            quickCouponUseCase.getCouponDetail(componentsItem.pagePath)
        } returns quickCouponDetailResponse

        viewModel.onAttachToViewHolder()

        TestCase.assertEquals(viewModel.getCouponVisibilityStatus().value != null, true)
    }
    /**************************** end of fetchCouponDetailData() *******************************************/

    /**************************** test for checkMobileVerificationStatus() *******************************************/
    @Test
    fun `test for checkMobileVerificationStatus when getMobileVerificationStatus returns error`() {
        viewModel.quickCouponUseCase = quickCouponUseCase
        coEvery {
            quickCouponUseCase.getMobileVerificationStatus()
        } throws Exception("Error")

        viewModel.checkMobileVerificationStatus()

        coVerify { quickCouponUseCase.getMobileVerificationStatus() }
    }

    @Test
    fun `test for checkMobileVerificationStatus when getMobileVerificationStatus returns phoneVerificationResponse`() {
        viewModel.quickCouponUseCase = quickCouponUseCase
            val phoneVerificationResponse: PhoneVerificationResponse = mockk(relaxed = true)
            coEvery {
                quickCouponUseCase.getMobileVerificationStatus()} returns phoneVerificationResponse

            viewModel.checkMobileVerificationStatus()

            TestCase.assertEquals(viewModel.getPhoneVerificationStatus().value != null, true)
    }
    /**************************** end of checkMobileVerificationStatus() *******************************************/

    @Test
    fun `test for getCouponDetail`(){
        val clickCouponData: ClickCouponData = mockk(relaxed = true)

        viewModel.getCouponDetail()

        assert(clickCouponData.componentID == componentsItem.id)
    }

    @Test
    fun `test for getCouponAppliedStatus`(){
        val clickCouponData: ClickCouponData = mockk(relaxed = true)

        viewModel.onAttachToViewHolder()
        viewModel.getCouponAppliedStatus()

        assert(clickCouponData.couponApplied != null)
    }

    @Test
    fun `test for getCouponApplicableStatus`(){
        val clickCouponData: ClickCouponData = mockk(relaxed = true)

        viewModel.onAttachToViewHolder()
        viewModel.getCouponApplicableStatus()

        assert(clickCouponData.isApplicable != null)
    }

    /**************************** test for User Logged in *******************************************/
    @Test
    fun `isUser Logged in when isLoggedIn is false`() {
        every { constructedWith<UserSession>(OfTypeMatcher<Context>(Context::class)).isLoggedIn } returns false

        viewModel.loggedInStatus()

        TestCase.assertEquals(viewModel.getLoggedInStatusLiveData().value, false)
    }

    @Test
    fun `isUser Logged in when isLoggedIn is true`(){
        every { constructedWith<UserSession>(OfTypeMatcher<Context>(Context::class)).isLoggedIn } returns true

        viewModel.loggedInStatus()

        TestCase.assertEquals(viewModel.getLoggedInStatusLiveData().value,true)
    }
    /**************************** end of User Logged in *******************************************/

    @Test
    fun `test for position passed`(){
        viewModel.onAttachToViewHolder()
        assert(viewModel.getComponentPosition().value == 99)
    }

}