package com.tokopedia.tokopoints.view.coupondetail

import android.os.Bundle
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.tokopoints.view.model.*
import com.tokopedia.tokopoints.view.util.*
import com.tokopedia.unit.test.ext.verifyValueEquals
import io.mockk.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
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

        runTest {
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

    @ExperimentalCoroutinesApi
    @Test
    fun `onSwipeComplete pin not require success`() {
        runTest {
            val uniqueCode = "realCode"
            val texts = "text"

            viewModel.data = CouponValueEntity(
                swipe = CouponSwipeDetail(
                    isNeedSwipe = true,
                    pin = CouponSwipePin(isPinRequire = false, text = texts)
                ),
                realCode = uniqueCode
            )

            val swipeCouponResponse = CouponSwipeUpdate(
                resultStatus = ResultStatusEntity(code = 200)
            )

            coEvery { repository.swipeMyCoupon(any(), "") } returns mockk {
                every { getData<CouponSwipeUpdateOuter>(CouponSwipeUpdateOuter::class.java) } returns CouponSwipeUpdateOuter(
                    swipeCoupon = swipeCouponResponse
                )
                every { getError(CouponSwipeUpdateOuter::class.java) } returns null
            }

            viewModel.onSwipeComplete()

            val expectedCouponLiveData = Success(swipeCouponResponse)

            viewModel.onCouponSwipe
                .verifyValueEquals(expectedCouponLiveData)
        }
    }


    @ExperimentalCoroutinesApi
    @Test
    fun `onSwipeComplete pin not require error`() {
        runTest {
            val uniqueCode = "realCode"
            val texts = "text"
            viewModel.data = mockk {
                every { swipe } returns mockk {
                    every { isNeedSwipe } returns true
                    every { pin } returns mockk {
                        every { isPinRequire } returns false
                        every { text } returns texts
                    }
                    every { realCode } returns uniqueCode
                }
            }

            val observer = mockk<Observer<Resources<CouponSwipeUpdate>>>()
            val response = mockk<CouponSwipeUpdate> {
                every {
                    resultStatus
                } returns mockk {
                    every { messages } returns listOf("nvjnv","nvjdsnvj")
                    every { code } returns  403
                }
            }
            coEvery { repository.swipeMyCoupon(any(), "") } returns mockk {
                every { getData<CouponSwipeUpdateOuter>(CouponSwipeUpdateOuter::class.java) } returns mockk(relaxed = true) {
                    every { swipeCoupon } returns response
                }
                every { getError(CouponSwipeUpdateOuter::class.java) } returns null
            }
            viewModel.onCouponSwipe.observeForever(observer)
            val method = viewModel::class.java.getDeclaredMethod("swipeMyCoupon", CouponValueEntity::class.java)
            method.isAccessible = true
            val parameters = arrayOfNulls<Any>(1)
            parameters[0] = viewModel.data
            method.invoke(viewModel, *parameters)
            verify { observer.onChanged(any<ErrorMessage<CouponSwipeUpdate>>()) }
        }
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `given isNeedSwipe false when call onSwipeComplete should not update onCouponSwipe live data`() {
        runTest {
            val isNeedSwipe = false

            viewModel.data = CouponValueEntity(
                swipe = CouponSwipeDetail(isNeedSwipe = isNeedSwipe)
            )

            viewModel.onSwipeComplete()

            viewModel.onCouponSwipe
                .verifyValueEquals(null)
        }
    }

    @Test
    fun `rehit coupon which conyain swipe data`() {
        runTest {
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
        runTest {
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
        runTest {
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
        runTest {
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
        runTest {
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
    fun `given reFetchRealCode throws exception when reFetchRealCode should set onReFetch live data with ErrorMessage`() {
        runTest {
            coEvery { repository.reFetchRealCode(any()) } throws Throwable()

            viewModel.reFetchRealCode()

            assertEquals(ErrorMessage<String>(""), viewModel.onReFetch.value)
        }
    }

    @Test
    fun redeemCoupon() {

        runTest {
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

    @ExperimentalCoroutinesApi
    @Test
    fun `redeem fail`(){
        runTest {
            coEvery { repository.redeemCoupon(any()) } throws NullPointerException()

            viewModel.redeemCoupon("code", "cta")

            viewModel.onRedeemCoupon
                .verifyValueEquals(ErrorMessage<String>("cta"))
        }
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `phone verification before coupon redeem`() {
        runTest {
            val phoneVerificationResponse =
                PhoneVerificationResponse(mfGetUserInfo = MfGetUserInfo(verifiedMsisdn = true))

            coEvery { repository.getUserPhoneVerificationInfo() } returns mockk {
                every {
                    getData<PhoneVerificationResponse>(PhoneVerificationResponse::class.java)
                } returns phoneVerificationResponse
                every { getError(PhoneVerificationResponse::class.java) } returns null
            }

            viewModel.isPhonerVerfied()

            viewModel.userInfo
                .verifyValueEquals(phoneVerificationResponse)
        }
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `given phone verification throws exception when call isPhonerVerfied should NOT crash`() {
        runTest {
            coEvery { repository.getUserPhoneVerificationInfo() } throws NullPointerException()

            viewModel.isPhonerVerfied()

            viewModel.userInfo
                .verifyValueEquals(null)
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

    @Test
    fun `given couponCode empty when init viewmodel should update detailLiveData with Loading`() {
        val couponCode = ""

        val bundle = mockk<Bundle>()
        coEvery { bundle.getString("coupon_code") } returns couponCode

        viewModel = CouponDetailViewModel(bundle, repository)

        assert(viewModel.detailLiveData.value is Loading)
        assert(viewModel.finish.value is Unit)
    }

    @Test
    fun `given getCouponDetail throws exception when init viewmodel should update detailLiveData with ErrorMessage`() {
        val exception = MessageErrorException(null)

        val couponCode = "5125"
        val bundle = mockk<Bundle>()

        coEvery { repository.getCouponDetail(couponCode) } throws exception
        coEvery { bundle.getString("coupon_code") } returns couponCode

        viewModel = CouponDetailViewModel(bundle, repository)

        viewModel.detailLiveData
            .verifyValueEquals(ErrorMessage<CouponValueEntity>(""))
    }

    @After
    fun onComplete() {
        Dispatchers.resetMain()
    }
}
