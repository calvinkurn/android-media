package com.tokopedia.tokopoints.view.merchantcoupon

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.tokopedia.tokopoints.view.model.merchantcoupon.MerchantCouponResponse
import com.tokopedia.tokopoints.view.util.ErrorMessage
import com.tokopedia.tokopoints.view.util.Loading
import com.tokopedia.tokopoints.view.util.Resources
import com.tokopedia.tokopoints.view.util.Success
import io.mockk.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlin.reflect.KClass

class MerchantCouponViewmodelTest {

    lateinit var viewModel: MerchantCouponViewModel
    val repository = mockk<MerchantCouponRepository>()

    @get:Rule
    var rule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        Dispatchers.setMain(TestCoroutineDispatcher())
        viewModel = MerchantCouponViewModel(repository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `getMerchantCouponData for success data`() {
        val couponObserver = mockk<Observer<Resources<MerchantCouponData>>>() {
            every { onChanged(any()) } just Runs
        }
        val merchantCouponResponseData = mockk<MerchantCouponResponse> {
            every { productlist } returns mockk {
                every { productCategoriesFilter } returns mockk()
                every { tokopointsPaging } returns mockk()
                every { catalogMVCWithProductsList } returns mockk()
            }
        }
        coEvery { repository.getProductData(1, "0") } returns mockk {
            every { getData<MerchantCouponResponse>(MerchantCouponResponse::class.java) } returns merchantCouponResponseData
            every { getError(MerchantCouponResponse::class.java) } returns null

        }
        viewModel.couponData.observeForever(couponObserver)
        viewModel.setCategoryRootId("0")
        viewModel.merchantCouponData(1)

        verify(ordering = Ordering.ORDERED) {
            couponObserver.onChanged(ofType(Loading::class as KClass<Loading<MerchantCouponData>>))
            couponObserver.onChanged(ofType(Success::class as KClass<Success<MerchantCouponData>>))
        }
        val result = viewModel.couponData.value as Success
        assert(result.data.merchantCouponResponse == merchantCouponResponseData)
    }


    @Test
    fun `getMerchantCouponData for error data`() {
        val couponObserver = mockk<Observer<Resources<MerchantCouponData>>>() {
            every { onChanged(any()) } just Runs
        }
        viewModel.couponData.observeForever(couponObserver)
        viewModel.merchantCouponData(0)

        verify(ordering = Ordering.ORDERED) {
            couponObserver.onChanged(ofType(ErrorMessage::class as KClass<ErrorMessage<MerchantCouponData>>))
        }
    }
}