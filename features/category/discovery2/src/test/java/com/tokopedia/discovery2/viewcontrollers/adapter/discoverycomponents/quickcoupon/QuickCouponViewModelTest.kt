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
    }

    @After
    @Throws(Exception::class)
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `test for useCase`() {
        val viewModel: QuickCouponViewModel =
                spyk(QuickCouponViewModel(application, componentsItem, 99))

        val quickCouponUseCase = mockk<QuickCouponUseCase>()
        viewModel.quickCouponUseCase = quickCouponUseCase
        assert(viewModel.quickCouponUseCase === quickCouponUseCase)
    }


    @Test
    fun `test for fetchCouponDetailData`(){
        viewModel.quickCouponUseCase = quickCouponUseCase
        runBlocking {
            coEvery {
                quickCouponUseCase.getCouponDetail(componentsItem.pagePath)} throws Exception("Error")
            viewModel.onAttachToViewHolder()
            coVerify { quickCouponUseCase.getCouponDetail(componentsItem.pagePath)}


            val quickCouponDetailResponse: QuickCouponDetailResponse = mockk(relaxed = true)
            coEvery {
                quickCouponUseCase.getCouponDetail(componentsItem.pagePath)} returns quickCouponDetailResponse
            viewModel.onAttachToViewHolder()
            TestCase.assertEquals(viewModel.getCouponVisibilityStatus().value != null, true)
        }
    }


    @Test
    fun `test for checkMobileVerificationStatus`(){
        viewModel.quickCouponUseCase = quickCouponUseCase
        runBlocking {
            coEvery {
                quickCouponUseCase.getMobileVerificationStatus()} throws Exception("Error")
            viewModel.checkMobileVerificationStatus()
            coVerify { quickCouponUseCase.getMobileVerificationStatus()}


            val phoneVerificationResponse: PhoneVerificationResponse = mockk(relaxed = true)
            coEvery {
                quickCouponUseCase.getMobileVerificationStatus()} returns phoneVerificationResponse
            viewModel.checkMobileVerificationStatus()
            TestCase.assertEquals(viewModel.getPhoneVerificationStatus().value != null, true)
        }
    }

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

    @Test
    fun `isUser Logged in`(){
        mockkConstructor(UserSession::class)
        every { application.applicationContext } returns context
        every { constructedWith<UserSession>(OfTypeMatcher<Context>(Context::class)).isLoggedIn } returns false
        viewModel.loggedInStatus()
        TestCase.assertEquals(viewModel.getLoggedInStatusLiveData().value,false)
        every { constructedWith<UserSession>(OfTypeMatcher<Context>(Context::class)).isLoggedIn } returns true
        viewModel.loggedInStatus()
        TestCase.assertEquals(viewModel.getLoggedInStatusLiveData().value,true)
        unmockkConstructor(UserSession::class)
    }

    @Test
    fun `test for position passed`(){
        viewModel.onAttachToViewHolder()
        assert(viewModel.getComponentPosition().value == 99)
    }

}