package com.tokopedia.tokopoints.view.validatePin

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.tokopedia.tokopoints.view.model.CouponSwipeUpdate
import com.tokopedia.tokopoints.view.util.CommonConstant
import com.tokopedia.tokopoints.view.util.ErrorMessage
import com.tokopedia.tokopoints.view.util.Resources
import com.tokopedia.tokopoints.view.util.Success
import io.mockk.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

import org.junit.Assert.*
import org.junit.Rule
import kotlin.reflect.KClass

class ValidateMerchantPinViewModelTest {

    lateinit var viewModel : ValidateMerchantPinViewModel
    val useCase = mockk<SwipeCouponUseCase>()
    @get:Rule
    var rule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        Dispatchers.setMain(TestCoroutineDispatcher())
        viewModel = ValidateMerchantPinViewModel(useCase)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `swipeMyCoupon success case`() {
        val observer = mockk<Observer<Resources<CouponSwipeUpdate>>>()
        val data = mockk<CouponSwipeUpdate>{
            every {  resultStatus } returns mockk{
                every {  code } returns CommonConstant.CouponRedemptionCode.SUCCESS
             }
        }
        coEvery{ useCase.execute("","")} returns mockk{
            every { swipeCoupon } returns data
        }
        viewModel.swipeCouponLiveData.observeForever(observer)
        viewModel.swipeMyCoupon("","")

        verify(ordering = Ordering.ORDERED) {
            observer.onChanged(ofType(Success::class as KClass<Success<CouponSwipeUpdate>>))
        }
        val result = viewModel.swipeCouponLiveData.value as Success
        assert(result.data == data)
    }

    @Test
    fun `swipe my coupon error case`(){
        val observer = mockk<Observer<Resources<CouponSwipeUpdate>>>()
        val data = mockk<CouponSwipeUpdate>{
            every {  resultStatus } returns mockk{
                every {  code } returns 1
                every { messages[0] } returns "error message"
                every { messages.size } returns 1
            }
        }
        coEvery{ useCase.execute("","")} returns mockk{
            every { swipeCoupon } returns data
        }
        viewModel.swipeCouponLiveData.observeForever(observer)
        viewModel.swipeMyCoupon("","")

        verify(ordering = Ordering.ORDERED) {
            observer.onChanged(ofType(ErrorMessage::class as KClass<ErrorMessage<CouponSwipeUpdate>>))
        }
    }
}