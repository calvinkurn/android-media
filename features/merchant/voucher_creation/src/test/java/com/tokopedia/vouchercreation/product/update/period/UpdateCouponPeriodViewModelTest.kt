package com.tokopedia.vouchercreation.product.update.period

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.vouchercreation.product.create.domain.entity.*
import com.tokopedia.vouchercreation.product.create.domain.usecase.GetCouponFacadeUseCase
import com.tokopedia.vouchercreation.product.create.domain.usecase.update.UpdateCouponFacadeUseCase
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.util.*

class UpdateCouponPeriodViewModelTest {

    @RelaxedMockK
    lateinit var updateCouponUseCase: UpdateCouponFacadeUseCase

    @RelaxedMockK
    lateinit var getCouponDetailUseCase: GetCouponFacadeUseCase

    @RelaxedMockK
    lateinit var updateCouponResultObserver: Observer<in Result<Boolean>>

    @RelaxedMockK
    lateinit var couponDetailObserver: Observer<in Result<CouponWithMetadata>>

    @RelaxedMockK
    lateinit var startDateObserver: Observer<in Date>

    @RelaxedMockK
    lateinit var endDateObserver: Observer<in Pair<Date, Date>>

    @get:Rule
    val rule = InstantTaskExecutorRule()


    private val viewModel by lazy {
        UpdateCouponPeriodViewModel(
            CoroutineTestDispatchersProvider,
            updateCouponUseCase,
            getCouponDetailUseCase
        )
    }

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        viewModel.updateCouponResult.observeForever(updateCouponResultObserver)
        viewModel.couponDetail.observeForever(couponDetailObserver)
        viewModel.startDate.observeForever(startDateObserver)
        viewModel.endDate.observeForever(endDateObserver)
    }

    @After
    fun tearDown() {
        viewModel.updateCouponResult.removeObserver(updateCouponResultObserver)
        viewModel.couponDetail.removeObserver(couponDetailObserver)
        viewModel.startDate.removeObserver(startDateObserver)
        viewModel.endDate.removeObserver(endDateObserver)
    }


    @Test
    fun `When update coupon success, observer should receive success value`() = runBlocking {
        //Given
        val isUpdateSuccess = true
        val coupon = buildDummyCoupon()
        val parentProductIds = listOf<Long>()

        coEvery {
            updateCouponUseCase.execute(
                coupon.id,
                coupon.information,
                coupon.settings,
                coupon.products,
                parentProductIds
            )
        } returns isUpdateSuccess


        //When
        viewModel.updateCoupon(coupon, parentProductIds)

        //Then
        coVerify {
            updateCouponResultObserver.onChanged(Success(isUpdateSuccess))
        }
    }

    @Test
    fun `When update coupon get error, observer should receive success value`() = runBlocking {
        //Given
        val isUpdateSuccess = false
        val coupon = buildDummyCoupon()
        val parentProductIds = listOf<Long>()

        coEvery {
            updateCouponUseCase.execute(
                coupon.id,
                coupon.information,
                coupon.settings,
                coupon.products,
                parentProductIds
            )
        } returns isUpdateSuccess


        //When
        viewModel.updateCoupon(coupon, parentProductIds)

        //Then
        coVerify {
            updateCouponResultObserver.onChanged(Success(isUpdateSuccess))
        }
    }

    @Test
    fun `When update coupon got exception, observer should receive error value`() = runBlocking {
        //Given
        val error = MessageErrorException()
        val coupon = buildDummyCoupon()
        val parentProductIds = listOf<Long>()

        coEvery {
            updateCouponUseCase.execute(
                coupon.id,
                coupon.information,
                coupon.settings,
                coupon.products,
                parentProductIds
            )
        } throws error


        //When
        viewModel.updateCoupon(coupon, parentProductIds)

        //Then
        coVerify {
            updateCouponResultObserver.onChanged(Fail(error))
        }
    }


    @Test
    fun `When get coupon detail success, observer should receive success value`() = runBlocking {
        //Given
        val createNewCoupon = false
        val couponId : Long = 3434455
        val coupon = mockk<CouponWithMetadata>()

        coEvery {
            getCouponDetailUseCase.execute(couponId, createNewCoupon)
        } returns coupon


        //When
        viewModel.getCouponDetail(couponId)

        //Then
        coVerify {
            couponDetailObserver.onChanged(Success(coupon))
        }
    }

    @Test
    fun `When get coupon detail error, observer should receive error value`() = runBlocking {
        //Given
        val createNewCoupon = false
        val error = MessageErrorException()
        val couponId : Long = 3434455

        coEvery {
            getCouponDetailUseCase.execute(couponId, createNewCoupon)
        } throws error


        //When
        viewModel.getCouponDetail(couponId)

        //Then
        coVerify {
            couponDetailObserver.onChanged(Fail(error))
        }
    }

    @Test
    fun `When get coupon, should return correct coupon value`() = runBlocking {
        //Given
        val coupon = buildDummyCoupon()

        //When
        viewModel.setCouponData(coupon)
        val actual = viewModel.getCoupon()

        //Then
        assertEquals(coupon, actual)
    }

    @Test
    fun `When get coupon start date, should return correct coupon start date value`() = runBlocking {
        //Given
        val startDate = Date()

        //When
        viewModel.setCurrentlySelectedStartDate(startDate)
        val actual = viewModel.getSelectedStartDate()

        //Then
        assertEquals(startDate, actual)
    }

    @Test
    fun `When get coupon end date, should return correct coupon end date value`() = runBlocking {
        //Given
        val endDate = Date()

        //When
        viewModel.setCurrentlySelectedEndDate(endDate)
        val actual = viewModel.getSelectedEndDate()

        //Then
        assertEquals(endDate, actual)
    }

    @Test
    fun `When open start date time picker, observer should receive previously stored start date`() = runBlocking {
        //Given
        val currentStartDate = Date()
        viewModel.setCurrentlySelectedStartDate(currentStartDate)
        val expected = viewModel.getSelectedStartDate()

        //When
        viewModel.openStartDateTimePicker()


        //Then
        coVerify {
            startDateObserver.onChanged(expected)
        }
    }

    @Test
    fun `When open end date time picker, observer should receive previously stored start date and end date`() = runBlocking {
        //Given
        val currentStartDate = Date()
        viewModel.setCurrentlySelectedStartDate(currentStartDate)

        val currentEndDate = Date()
        viewModel.setCurrentlySelectedEndDate(currentEndDate)

        val expected = Pair(viewModel.getSelectedStartDate(), viewModel.getSelectedEndDate())

        //When
        viewModel.openEndDateTimePicker()

        //Then
        coVerify {
            endDateObserver.onChanged(expected)
        }
    }

    private fun buildDummyCoupon() : Coupon {
        return Coupon(
            100,
            CouponInformation(
                CouponInformation.Target.PUBLIC,
                "coupon-name",
                "coupon-code",
                CouponInformation.Period(Date(), Date())
            ),
            CouponSettings(CouponType.CASHBACK, DiscountType.PERCENTAGE, MinimumPurchaseType.NOMINAL, 0, 50, 25000, 10, 50000, 500000),
            emptyList(),
            emptyList()
        )
    }
}