package com.tokopedia.tokopoints.view.coupondetail

import android.os.Bundle
import android.util.Log
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.tokopoints.view.model.*
import com.tokopedia.tokopoints.view.util.*
import io.mockk.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Test

import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule

class CouponDetailViewModelTest {

    val bundle = mockk<Bundle> {
        every { getString(CommonConstant.EXTRA_COUPON_CODE) } returns "couponcode"
    }
    val repository = mockk<CouponDetailRepository>()


    lateinit var viewModel: CouponDetailViewModel
    @get:Rule
    var rule = InstantTaskExecutorRule()
    val detailObserver = mockk<Observer<Resources<CouponValueEntity>>>() {
        every { onChanged(any()) } just Runs
    }

    @Before
    fun setup() {
        val dispacter = TestCoroutineDispatcher()
        Dispatchers.setMain(dispacter)
        viewModel = CouponDetailViewModel(bundle, repository)
        viewModel.detailLiveData.observeForever(detailObserver)
        verify { detailObserver.onChanged(any<ErrorMessage<CouponValueEntity>>()) }
    }

    @Test
    fun onSwipeComplete() {

        runBlockingTest {
            val code = "realCode"
            val texts = "text"
            viewModel.data = mockk {
                every { swipe } returns mockk {
                    every { isNeedSwipe } returns true
                    every { pin } returns mockk {
                        every { isPinRequire } returns true
                        every { text } returns texts
                    }
                    every { realCode } returns code
                }
            }
            viewModel.onSwipeComplete()

            assert(viewModel.pinPageData.value is PinPageData)
            assert(viewModel.pinPageData.value!!.code == code && viewModel.pinPageData.value!!.pinText == texts)

        }
    }

    @Test
    fun `rehit coupon which conyain swipe data`() {
        runBlockingTest {
            val swipeObserver = mockk<Observer<CouponSwipeDetail>>() {
                every { onChanged(any()) } just Runs
            }


            val data = mockk<GraphqlResponse>(relaxed = true)
            val swipe1 = mockk<CouponSwipeDetail> {
                every { isNeedSwipe } returns true
            }

            val response = mockk<CouponValueEntity> {
                every { swipe } returns swipe1
            }

            coEvery { repository.getCouponDetail(any()) } returns data
            every { data.getError(CouponDetailOuter::class.java) } returns null
            every { data.getData<CouponDetailOuter>(CouponDetailOuter::class.java) } returns mockk(relaxed = true) {
                every { detail } returns response
            }
            viewModel.swipeDetail.observeForever(swipeObserver)

            viewModel.onErrorButtonClick()

            verify { detailObserver.onChanged(any<Loading<CouponValueEntity>>()) }
            verify { detailObserver.onChanged(any<Success<CouponValueEntity>>()) }
            verify { swipeObserver.onChanged(any()) }

            val result = viewModel.detailLiveData.value as Success
            assert(viewModel.swipeDetail.value == swipe1)
            assert(result.data == response)

        }

    }

    @Test
    fun `rehit coupon with swipe not available case 1`() {
        runBlockingTest {
            val swipeObserver = mockk<Observer<CouponSwipeDetail>>() {
                every { onChanged(any()) } just Runs
            }


            val data = mockk<GraphqlResponse>(relaxed = true)


            val response = mockk<CouponValueEntity>(relaxed = true)

            coEvery { repository.getCouponDetail(any()) } returns data
            every { data.getError(CouponDetailOuter::class.java) } returns null
            every { data.getData<CouponDetailOuter>(CouponDetailOuter::class.java) } returns mockk(relaxed = true) {
                every { detail } returns response
            }
            viewModel.swipeDetail.observeForever(swipeObserver)

            viewModel.onErrorButtonClick()

            verify { detailObserver.onChanged(any<Loading<CouponValueEntity>>()) }
            verify { detailObserver.onChanged(any<Success<CouponValueEntity>>()) }
            verify(exactly = 0) { swipeObserver.onChanged(any()) }

            assert(viewModel.swipeDetail.value !is CouponSwipeDetail)

        }


    }

    @Test
    fun `rehit coupon with swipe not available case 2`() {
        runBlockingTest {
            val swipeObserver = mockk<Observer<CouponSwipeDetail>>() {
                every { onChanged(any()) } just Runs
            }


            val data = mockk<GraphqlResponse>(relaxed = true)


            val response = mockk<CouponValueEntity>(relaxed = true)

            coEvery { repository.getCouponDetail(any()) } returns data
            every { data.getError(CouponDetailOuter::class.java) } returns null
            every { data.getData<CouponDetailOuter>(CouponDetailOuter::class.java) } returns mockk(relaxed = true) {
                every { detail } returns response
            }
            viewModel.swipeDetail.observeForever(swipeObserver)
            every { response.swipe } returns mockk()

            viewModel.onErrorButtonClick()

            verify { detailObserver.onChanged(any<Loading<CouponValueEntity>>()) }
            verify { detailObserver.onChanged(any<Success<CouponValueEntity>>()) }
            verify(exactly = 0) { swipeObserver.onChanged(any()) }

            assert(viewModel.swipeDetail.value !is CouponSwipeDetail)
        }
    }
    @Test
    fun `rehit coupon with swipe not available case 3`() {
        runBlockingTest {
            val swipeObserver = mockk<Observer<CouponSwipeDetail>>() {
                every { onChanged(any()) } just Runs
            }


            val data = mockk<GraphqlResponse>(relaxed = true)


            val response = mockk<CouponValueEntity>(relaxed = true)

            coEvery { repository.getCouponDetail(any()) } returns data
            every { data.getError(CouponDetailOuter::class.java) } returns null
            every { data.getData<CouponDetailOuter>(CouponDetailOuter::class.java) } returns mockk(relaxed = true) {
                every { detail } returns response
            }
            viewModel.swipeDetail.observeForever(swipeObserver)
            every { response.swipe } returns mockk {
                every { isNeedSwipe } returns false
            }

            viewModel.onErrorButtonClick()

            verify { detailObserver.onChanged(any<Loading<CouponValueEntity>>()) }
            verify { detailObserver.onChanged(any<Success<CouponValueEntity>>()) }
            verify(exactly = 0) { swipeObserver.onChanged(any()) }

            assert(viewModel.swipeDetail.value !is CouponSwipeDetail)
        }
    }


    @Test
    fun reFetchRealCode() {
        runBlockingTest {
            val refetchObserver = mockk<Observer<Resources<String>>>() {
                every { onChanged(any()) } just Runs
            }
            viewModel.onReFetch.observeForever(refetchObserver)
            viewModel.reFetchRealCode()
            verify { refetchObserver.onChanged(any<ErrorMessage<String>>()) }

            val realcode = "code"
            coEvery { repository.reFetchRealCode(any()) } returns mockk {
                every { getError(any()) } returns null
                every { getData<CouponDetailOuter>(CouponDetailOuter::class.java) } returns mockk {
                    every { detail } returns mockk {
                        every { realCode } returns realcode
                    }
                }
            }
            viewModel.reFetchRealCode()

            verify { refetchObserver.onChanged(any()) }
            val result = viewModel.onReFetch.value as Success<String>
            assert(result.data == realcode)
        }
    }

    @Test
    fun redeemCoupon() {

        runBlockingTest {
            val redeemCouponObserver = mockk<Observer<Resources<String>>>()
            viewModel.onRedeemCoupon.observeForever(redeemCouponObserver)
            viewModel.redeemCoupon("code", "cta")
            verify { redeemCouponObserver.onChanged(any<ErrorMessage<String>>()) }


            coEvery { repository.redeemCoupon(any()) } returns mockk() {
                every { getError(ApplyCouponBaseEntity::class.java) } returns null
                every { getData<ApplyCouponBaseEntity>(ApplyCouponBaseEntity::class.java) } returns mockk()
            }
            viewModel.redeemCoupon("code", "cta")
            verify { redeemCouponObserver.onChanged(any<Success<String>>()) }
        }


    }

    @Test
    fun onInit() {
        val bundle = mockk<Bundle>()
        coEvery { bundle.getString(any()) } returns "12345"
        viewModel = CouponDetailViewModel(Bundle(), repository)
        assert(viewModel.finish.value is Unit)

        viewModel = CouponDetailViewModel(bundle, repository)
        assertEquals("12345", viewModel.couponCode)

    }

    @After
    fun onComplete() {
        Dispatchers.resetMain()
    }
}