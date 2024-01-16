package com.tokopedia.tokopoints.view.validatePin

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.tokopedia.tokopoints.view.model.CouponSwipeUpdate
import com.tokopedia.tokopoints.view.model.CouponSwipeUpdateOuter
import com.tokopedia.tokopoints.view.model.ResultStatusEntity
import com.tokopedia.tokopoints.view.util.ErrorMessage
import com.tokopedia.tokopoints.view.util.Resources
import com.tokopedia.tokopoints.view.util.Success
import com.tokopedia.unit.test.ext.verifyValueEquals
import io.mockk.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

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
        val code = "bcdd62ef"
        val pin = "185991"
        val response = CouponSwipeUpdateOuter(
            swipeCoupon = CouponSwipeUpdate(
                resultStatus = ResultStatusEntity(code = 200)
            )
        )

        coEvery { useCase.execute(code, pin) } returns response

        viewModel.swipeMyCoupon(code, pin)

        viewModel.swipeCouponLiveData
            .verifyValueEquals(Success(response.swipeCoupon))
    }

    @Test
    fun `swipe my coupon error case`(){
        val observer = mockk<Observer<Resources<CouponSwipeUpdate>>>()
        val msgString = ArrayList<String>()
        msgString.add("error message")
        val dummyResponse = ResultStatusEntity(code = 1, messages = msgString)
        val data = mockk<CouponSwipeUpdate>{
            every {  resultStatus } returns
               dummyResponse
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

    @Test
    fun `given status code NOT 200 and error message list empty when swipeMyCoupon should NOT update swipeCouponLiveData`(){
        val statusCode = 404
        val messageList = emptyList<String>()
        val statusResponse = ResultStatusEntity(code = statusCode, messages = messageList)

        coEvery{ useCase.execute("","")} returns CouponSwipeUpdateOuter(
            swipeCoupon = CouponSwipeUpdate(resultStatus = statusResponse)
        )

        viewModel.swipeMyCoupon("","")

        viewModel.swipeCouponLiveData
            .verifyValueEquals(null)
    }

    @Test
    fun `given status code NOT 200 and error message null when swipeMyCoupon should NOT update swipeCouponLiveData`(){
        val statusCode = 404
        val messageList = null
        val statusResponse = ResultStatusEntity(code = statusCode, messages = messageList)

        coEvery{ useCase.execute("","")} returns CouponSwipeUpdateOuter(
            swipeCoupon = CouponSwipeUpdate(resultStatus = statusResponse)
        )

        viewModel.swipeMyCoupon("","")

        viewModel.swipeCouponLiveData
            .verifyValueEquals(null)
    }
}
