package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.claimcoupon

import android.app.Application
import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.discovery2.Constant.ClaimCouponConstant.DOUBLE_COLUMNS
import com.tokopedia.discovery2.Constant.ClaimCouponConstant.HABIS
import com.tokopedia.discovery2.Constant.ClaimCouponConstant.NOT_LOGGEDIN
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.data.claim_coupon.CatalogWithCouponList
import com.tokopedia.discovery2.data.claimcoupon.RedeemCouponResponse
import com.tokopedia.discovery2.usecase.ClaimCouponClickUseCase
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.*
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class ClaimCouponItemViewModelTest {

    @get:Rule
    var rule = InstantTaskExecutorRule()

    private val componentsItem: ComponentsItem = mockk()
    private val application: Application = mockk()
    var context: Context = mockk(relaxed = true)
    private val viewModel: ClaimCouponItemViewModel by lazy {
        spyk(ClaimCouponItemViewModel(application, componentsItem, 99))
    }

    private val claimCouponClickUseCase: ClaimCouponClickUseCase by lazy {
        mockk()
    }

    @RelaxedMockK
    lateinit var userSession: UserSessionInterface

    @Before
    @Throws(Exception::class)
    fun setUp() {
        MockKAnnotations.init(this)
        Dispatchers.setMain(TestCoroutineDispatcher())
        every { componentsItem.data } returns null
        coEvery { application.applicationContext } returns context
    }

    @Test
    fun `position test`() {
        assert(viewModel.position == 99)
    }

    @Test
    fun `test for application`() {
        assert(viewModel.application === application)
    }
    @Test
    fun `test for componentData`() {
        assert(viewModel.getComponentData().value === componentsItem)
    }

    @Test
    fun `test for claimCouponClickUseCase useCase`() {
        val viewModel: ClaimCouponItemViewModel =
            spyk(ClaimCouponItemViewModel(application, componentsItem, 99))

        val claimCouponClickUseCase = mockk<ClaimCouponClickUseCase>()
        viewModel.claimCouponClickUseCase = claimCouponClickUseCase

        assert(viewModel.claimCouponClickUseCase === claimCouponClickUseCase)
    }

    @Test
    fun `test for userSession useCase`() {
        val viewModel: ClaimCouponItemViewModel =
            spyk(ClaimCouponItemViewModel(application, componentsItem, 99))

        val userSession = mockk<UserSessionInterface>()
        viewModel.userSession = userSession

        assert(viewModel.userSession === userSession)
    }

    /**************************** getClaimCouponData() *******************************************/

    @Test
    fun getClaimCouponData() {
        val claimString = "claimString"
        val data = arrayListOf(CatalogWithCouponList(buttonStr = claimString))
        every { componentsItem.claimCouponList } returns data

        assertEquals(viewModel.getClaimCouponData(), data.firstOrNull())
    }

    @Test
    fun `getComponentData when dataItem is null`() {
        every { componentsItem.claimCouponList } returns null

        assertEquals(viewModel.getClaimCouponData(), null)
    }

    @Test
    fun `getComponentData when claimButtonStr is null`() {
        val data = arrayListOf(CatalogWithCouponList())
        every { componentsItem.claimCouponList } returns data

        assertEquals(viewModel.getClaimCouponData()?.status, HABIS)
    }

    /**************************** getComponentData() *******************************************/

    /**************************** getIsDouble() *******************************************/

    @Test
    fun `getIsDouble when columns returns DOUBLE_COLUMNS`() {
        every { componentsItem.properties?.columns } returns DOUBLE_COLUMNS

        assertEquals(viewModel.getIsDouble(), true)
    }

    @Test
    fun `getIsDouble when columns returs HABIS`() {
        every { componentsItem.properties?.columns } returns HABIS

        assertEquals(viewModel.getIsDouble(), false)
    }

    /**************************** getIsDouble() *******************************************/

    /**************************** redeemCoupon() *******************************************/

    @Test
    fun `redeemCoupon when isLoggedIn is false`() {
        viewModel.userSession = userSession
        viewModel.claimCouponClickUseCase = claimCouponClickUseCase
        coEvery { viewModel.userSession?.isLoggedIn } returns false

        viewModel.redeemCoupon { }

        assertEquals(viewModel.getRedeemCouponCode().value, NOT_LOGGEDIN)
    }

    @Test
    fun `redeemCoupon when isLoggedIn is true and id is empty`() {
        viewModel.userSession = userSession
        viewModel.claimCouponClickUseCase = claimCouponClickUseCase
        val applink = "applink"
        val code = "code"
        val data = RedeemCouponResponse(
            hachikoRedeem =
            RedeemCouponResponse.HachikoRedeem(
                coupons =
                arrayListOf(
                    RedeemCouponResponse.HachikoRedeem.Coupon(
                        appLink = applink,
                        code = code
                    )
                )
            )
        )
        val list = ArrayList<CatalogWithCouponList>()
        val dataItem = CatalogWithCouponList(id = 12)
        list.add(dataItem)
        every { componentsItem.claimCouponList } returns list
        coEvery { viewModel.userSession?.isLoggedIn } returns true
        coEvery { viewModel.claimCouponClickUseCase?.redeemCoupon(any()) } returns data

        viewModel.redeemCoupon { }

        assertEquals(viewModel.getRedeemCouponCode().value, code)
    }

    @Test
    fun `redeemCoupon when isLoggedIn is true`() {
        viewModel.userSession = userSession
        viewModel.claimCouponClickUseCase = claimCouponClickUseCase
        val applink = "applink"
        val code = "code"
        val data = RedeemCouponResponse(
            hachikoRedeem =
            RedeemCouponResponse.HachikoRedeem(
                coupons =
                arrayListOf(
                    RedeemCouponResponse.HachikoRedeem.Coupon(
                        appLink = applink,
                        code = code
                    )
                )
            )
        )
        every { componentsItem.claimCouponList } returns null
        coEvery { viewModel.userSession?.isLoggedIn } returns true
        coEvery { viewModel.claimCouponClickUseCase?.redeemCoupon(any()) } returns data

        viewModel.redeemCoupon { }

        assertEquals(viewModel.getRedeemCouponCode().value, code)
    }

    @Test
    fun `redeemCoupon when isLoggedIn is true and claimCouponList is empty`() {
        viewModel.userSession = userSession
        viewModel.claimCouponClickUseCase = claimCouponClickUseCase
        val applink = "applink"
        val code = "code"
        val data = RedeemCouponResponse(
            hachikoRedeem =
            RedeemCouponResponse.HachikoRedeem(
                coupons =
                arrayListOf(
                    RedeemCouponResponse.HachikoRedeem.Coupon(
                        appLink = applink,
                        code = code
                    )
                )
            )
        )
        every { componentsItem.claimCouponList } returns arrayListOf()
        coEvery { viewModel.userSession?.isLoggedIn } returns true
        coEvery { viewModel.claimCouponClickUseCase?.redeemCoupon(any()) } returns data

        viewModel.redeemCoupon { }

        assertEquals(viewModel.getRedeemCouponCode().value, code)
    }

    @Test
    fun `redeemCoupon when isLoggedIn is true and HachikoRedeem is null`() {
        viewModel.userSession = userSession
        viewModel.claimCouponClickUseCase = claimCouponClickUseCase
        val data = RedeemCouponResponse()
        coEvery { userSession.isLoggedIn } returns true
        coEvery { claimCouponClickUseCase.redeemCoupon(any()) } returns data

        viewModel.redeemCoupon { }

        assertEquals(viewModel.getRedeemCouponCode().value, null)
    }

    @Test
    fun `redeemCoupon when isLoggedIn is true and coupons list empty`() {
        viewModel.userSession = userSession
        viewModel.claimCouponClickUseCase = claimCouponClickUseCase
        val data = RedeemCouponResponse(
            hachikoRedeem =
            RedeemCouponResponse.HachikoRedeem(coupons = arrayListOf())
        )
        coEvery { userSession.isLoggedIn } returns true
        coEvery { claimCouponClickUseCase.redeemCoupon(any()) } returns data

        viewModel.redeemCoupon { }

        assertEquals(viewModel.getRedeemCouponCode().value, null)
    }

    @Test
    fun `redeemCoupon when claimCouponClickUseCase redeemCoupon throws MessageErrorException`() {
        viewModel.userSession = userSession
        viewModel.claimCouponClickUseCase = claimCouponClickUseCase
        coEvery { userSession.isLoggedIn } returns true
        coEvery { claimCouponClickUseCase.redeemCoupon(any()) } throws MessageErrorException("error")

        viewModel.redeemCoupon { }

        assertEquals(viewModel.getRedeemCouponCode().value, null)
    }

    @Test
    fun `redeemCoupon when claimCouponClickUseCase redeemCoupon throws Exception`() {
        viewModel.userSession = userSession
        viewModel.claimCouponClickUseCase = claimCouponClickUseCase
        coEvery { userSession.isLoggedIn } returns true
        coEvery { claimCouponClickUseCase.redeemCoupon(any()) } throws Exception("error")

        viewModel.redeemCoupon { }

        assertEquals(viewModel.getRedeemCouponCode().value, null)
    }

    /**************************** redeemCoupon() *******************************************/

    /**************************** setClick() *******************************************/

    @Test
    fun setClick() {
        val applink = "tokopedia://discovery/deals"
        val data = arrayListOf(CatalogWithCouponList(appLink = applink))
        every { componentsItem.claimCouponList } returns data
        every { viewModel.navigate(any(), any()) } just runs

        viewModel.setClick(mockk(), "")

        verify(exactly = 1) { viewModel.navigate(any(), any()) }
    }

    /**************************** setClick() *******************************************/

    /**************************** getCouponAppLink() *******************************************/

    @Test
    fun `getCouponAppLink when applinks is not null`() {
        val applink = "tokopedia://discovery/deals"
        val data = arrayListOf(CatalogWithCouponList(appLink = applink))
        every { componentsItem.claimCouponList } returns data

        assertEquals(viewModel.getCouponAppLink(), applink)
    }

    @Test
    fun `getCouponAppLink when dataItem is empty`() {
        val data = arrayListOf(CatalogWithCouponList())
        every { componentsItem.claimCouponList } returns data

        assertEquals(viewModel.getCouponAppLink(), "")
    }

    @Test
    fun `getCouponAppLink when dataItem is null`() {
        every { componentsItem.claimCouponList } returns null

        assertEquals(viewModel.getCouponAppLink(), "")
    }

    /**************************** getCouponAppLink() *******************************************/

    @After
    fun shutDown() {
        Dispatchers.resetMain()
    }
}
