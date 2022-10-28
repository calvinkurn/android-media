package com.tokopedia.tokomember_seller_dashboard

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.tokomember_seller_dashboard.domain.TmCouponDetailUsecase
import com.tokopedia.tokomember_seller_dashboard.model.TmCouponDetailResponseData
import com.tokopedia.tokomember_seller_dashboard.util.TokoLiveDataResult
import com.tokopedia.tokomember_seller_dashboard.view.viewmodel.TmCouponDetailViewModel
import io.mockk.every
import io.mockk.mockk
import io.mockk.spyk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertEquals

@ExperimentalCoroutinesApi
class TmCouponDetailViewModelTest {
    @get:Rule
    val rule = InstantTaskExecutorRule()

    lateinit var viewModel : TmCouponDetailViewModel
    lateinit var couponDetailUsecase: TmCouponDetailUsecase
    lateinit var dispatcher : TestCoroutineDispatcher
    lateinit var throwable: Throwable

    @Before
    fun setup(){
        couponDetailUsecase = mockk()
        dispatcher = TestCoroutineDispatcher()
        viewModel = spyk(TmCouponDetailViewModel(couponDetailUsecase,dispatcher))
        throwable = Throwable("Exception")
    }

    @Test
    fun `get coupon details success`(){
        val couponDetailResponse = mockk<TmCouponDetailResponseData>()
        every {
            couponDetailUsecase.getCouponDetail(any(),any(),any())
        } answers {
            firstArg<(TmCouponDetailResponseData) -> Unit>().invoke(couponDetailResponse)
        }

        viewModel.getCouponDetails(0)
        assertEquals(viewModel.couponDetailResult.value?.status,TokoLiveDataResult.STATUS.SUCCESS)
    }

    @Test
    fun `get coupon details failure`(){
        every {
            couponDetailUsecase.getCouponDetail(any(),any(),any())
        } answers {
            secondArg<(Throwable) -> Unit>().invoke(throwable)
        }

        viewModel.getCouponDetails(0)
        assertEquals(viewModel.couponDetailResult.value?.status,TokoLiveDataResult.STATUS.ERROR)
    }
}
