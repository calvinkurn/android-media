package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.claimcoupon

import android.app.Application
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.discovery2.Constant.ClaimCouponConstant.DOUBLE_COLUMNS
import com.tokopedia.discovery2.Constant.ClaimCouponConstant.NOT_LOGGEDIN
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.data.DataItem
import com.tokopedia.discovery2.data.claimcoupon.RedeemCouponResponse
import io.mockk.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
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
    private val viewModel: ClaimCouponItemViewModel by lazy {
        spyk(ClaimCouponItemViewModel(application, componentsItem, 99))
    }

    @Before
    @Throws(Exception::class)
    fun setUp() {
        MockKAnnotations.init(this)
        Dispatchers.setMain(TestCoroutineDispatcher())
        every { componentsItem.data } returns null
    }

    @Test
    fun `position test`() {
        assert(viewModel.position == 99)
    }

    /**************************** getComponentData() *******************************************/

    @Test
    fun getComponentData() {
        val claimString = "claimString"
        val data = arrayListOf(DataItem(claimButtonStr = claimString))
        every { componentsItem.data } returns data

        viewModel.onAttachToViewHolder()

        assertEquals(viewModel.getComponentData().value?.status, claimString)
    }

    /**************************** getComponentData() *******************************************/

    /**************************** getIsDouble() *******************************************/

    @Test
    fun getIsDouble() {
        every { componentsItem.properties?.columns } returns DOUBLE_COLUMNS

        assertEquals(viewModel.getIsDouble(), true)
    }

    /**************************** getIsDouble() *******************************************/


    /**************************** redeemCoupon() *******************************************/

    @Test
    fun `redeemCoupon when isLoggedIn is false`() {
        coEvery { viewModel.userSession.isLoggedIn } returns false

        viewModel.redeemCoupon { }

        assertEquals(viewModel.getRedeemCouponCode().value, NOT_LOGGEDIN)
    }

    @Test
    fun `redeemCoupon when isLoggedIn is true`() {
            val applink = "applink"
            val code = "code"
            val data = RedeemCouponResponse(hachikoRedeem =
                RedeemCouponResponse.HachikoRedeem(coupons =
                    arrayListOf(RedeemCouponResponse.HachikoRedeem.Coupon(
                        appLink = applink, code = code
            ))))
            coEvery { viewModel.userSession.isLoggedIn } returns true
            coEvery { viewModel.claimCouponClickUseCase.redeemCoupon(any()) } returns data

            viewModel.redeemCoupon { }

            assertEquals(viewModel.getRedeemCouponCode().value, code)
    }

    /**************************** redeemCoupon() *******************************************/

    /**************************** setClick() *******************************************/

    @Test
    fun setClick() {
        viewModel.setClick(mockk(), "")

        verify(exactly = 1) { viewModel.navigate(any(),any()) }
    }

    /**************************** setClick() *******************************************/


    @After
    fun shutDown() {
        Dispatchers.resetMain()
    }
}