package com.tokopedia.tokopoints.view.coupondetail

import android.os.Bundle
import com.tokopedia.tokopoints.view.util.CommonConstant
import io.mockk.every
import io.mockk.mockk
import org.junit.Test

import org.junit.Assert.*
import org.junit.Before

class CouponDetailViewModelTest {

    val bundle = mockk<Bundle>{
        every { getString(CommonConstant.EXTRA_COUPON_CODE) } returns "couponcode"
    }
    val repository = mockk<CouponDetailRepository>()

    lateinit var viewModel: CouponDetailViewModel

    @Before
    fun setup() {
        viewModel = CouponDetailViewModel(bundle, repository)
    }

    @Test
    fun onSwipeComplete() {
    }

    @Test
    fun onErrorButtonClick() {
    }

    @Test
    fun reFetchRealCode() {
    }

    @Test
    fun redeemCoupon() {
    }
}