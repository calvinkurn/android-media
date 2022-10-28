package com.tokopedia.vouchercreation.product.update.quota

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.unit.test.ext.getOrAwaitValue
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.vouchercreation.common.consts.VoucherStatusConst
import com.tokopedia.vouchercreation.shop.voucherlist.domain.usecase.UpdateQuotaUseCase
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class UpdateCouponQuotaViewModelTest {

    @RelaxedMockK
    lateinit var updateQuotaUseCase: UpdateQuotaUseCase

    @RelaxedMockK
    lateinit var validInputObserver: Observer<in UpdateCouponQuotaViewModel.QuotaState>

    @RelaxedMockK
    lateinit var updateQuotaResultObserver: Observer<in Result<Boolean>>

    @RelaxedMockK
    lateinit var maxExpenseEstimationObserver: Observer<in Long>

    @get:Rule
    val rule = InstantTaskExecutorRule()


    private val viewModel by lazy {
        UpdateCouponQuotaViewModel(
            CoroutineTestDispatchersProvider,
            updateQuotaUseCase
        )
    }

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        viewModel.validInput.observeForever(validInputObserver)
        viewModel.updateQuotaResult.observeForever(updateQuotaResultObserver)
        viewModel.maxExpenseEstimation.observeForever(maxExpenseEstimationObserver)
    }

    @After
    fun tearDown() {
        viewModel.validInput.removeObserver(validInputObserver)
        viewModel.updateQuotaResult.removeObserver(updateQuotaResultObserver)
        viewModel.maxExpenseEstimation.removeObserver(maxExpenseEstimationObserver)
    }

    @Test
    fun `When update coupon quota success, should emit success to observer`() = runBlocking {
        //Given
        val couponId = 92382
        val quota = 20
        val updateQuotaSuccess = true

        coEvery { updateQuotaUseCase.executeOnBackground() } returns updateQuotaSuccess

        //When
        viewModel.updateQuota(couponId, quota)

        //Then
        coVerify { updateQuotaResultObserver.onChanged(Success(updateQuotaSuccess)) }
    }


    @Test
    fun `When update coupon error, should emit error to observer`() = runBlocking {
        //Given
        val couponId = 92382
        val quota = 20
        val updateQuotaSuccess = false

        coEvery { updateQuotaUseCase.executeOnBackground() } returns updateQuotaSuccess

        //When
        viewModel.updateQuota(couponId, quota)

        //Then
        coVerify { updateQuotaResultObserver.onChanged(Success(updateQuotaSuccess)) }
    }

    @Test
    fun `When update coupon got exception, should emit error to observer`() = runBlocking {
        //Given
        val error = MessageErrorException()
        val couponId = 92382
        val quota = 20

        coEvery { updateQuotaUseCase.executeOnBackground() } throws error

        //When
        viewModel.updateQuota(couponId, quota)

        //Then
        coVerify { updateQuotaResultObserver.onChanged(Fail(error)) }
    }


    @Test
    fun `When new quota is zero, observer should receive quota is zero error`() = runBlocking {
        //Given
        val newQuota = 0
        val currentQuota = 20
        val expected = UpdateCouponQuotaViewModel.QuotaState.QuotaIsZero


        //When
        viewModel.validateInput(newQuota, currentQuota, VoucherStatusConst.ONGOING)

        //Then
        coVerify { validInputObserver.onChanged(expected) }
    }

    @Test
    fun `When coupon is ongoing coupon and new quota is less than current quota, observer should receive below min quota error`() = runBlocking {
        //Given
        val newQuota = 25
        val currentQuota = 50
        val expected = UpdateCouponQuotaViewModel.QuotaState.BelowMinQuota(currentQuota)


        //When
        viewModel.validateInput(newQuota, currentQuota, VoucherStatusConst.ONGOING)

        //Then
        coVerify { validInputObserver.onChanged(expected) }
        val actual = viewModel.validInput.getOrAwaitValue()
        assertEquals(currentQuota, (actual as UpdateCouponQuotaViewModel.QuotaState.BelowMinQuota).minQuota)
    }

    @Test
    fun `When coupon is ongoing coupon and new quota is bigger than current quota, observer should receive valid input`() = runBlocking {
        //Given
        val newQuota = 50
        val currentQuota = 25
        val expected = UpdateCouponQuotaViewModel.QuotaState.Valid


        //When
        viewModel.validateInput(newQuota, currentQuota, VoucherStatusConst.ONGOING)

        //Then
        coVerify { validInputObserver.onChanged(expected) }
    }

    @Test
    fun `When coupon is not an ongoing coupon and new quota is bigger than current quota, observer should receive valid input`() = runBlocking {
        //Given
        val newQuota = 50
        val currentQuota = 25
        val expected = UpdateCouponQuotaViewModel.QuotaState.Valid


        //When
        viewModel.validateInput(newQuota, currentQuota, VoucherStatusConst.DELETED)

        //Then
        coVerify { validInputObserver.onChanged(expected) }
    }

    @Test
    fun `When coupon is not an ongoing coupon and new quota is less than current quota, observer should receive valid input`() = runBlocking {
        //Given
        val newQuota = 25
        val currentQuota = 50
        val expected = UpdateCouponQuotaViewModel.QuotaState.Valid


        //When
        viewModel.validateInput(newQuota, currentQuota, VoucherStatusConst.DELETED)

        //Then
        coVerify { validInputObserver.onChanged(expected) }
    }


    @Test
    fun `When new quota is bigger than max allowed quota, observer should receive exceed max allowed quota error`() = runBlocking {
        //Given
        val newQuota = 1200
        val currentQuota = 25
        val maxQuota = 999

        val expected = UpdateCouponQuotaViewModel.QuotaState.ExceedMaxAllowedQuota(maxQuota)


        //When
        viewModel.validateInput(newQuota, currentQuota, VoucherStatusConst.ONGOING)

        //Then
        coVerify { validInputObserver.onChanged(expected) }
        val actual = viewModel.validInput.getOrAwaitValue()
        assertEquals(maxQuota, (actual as UpdateCouponQuotaViewModel.QuotaState.ExceedMaxAllowedQuota).maxQuota)
    }


    @Test
    fun `When calculate max expense, observer should receive correct value from the multiplication`() = runBlocking {
        //Given
        val discountAmount = 25_000
        val quota = 2
        val expected : Long = 50_000


        //When
        viewModel.calculateMaxExpenseEstimation(discountAmount, quota)

        //Then
        coVerify { maxExpenseEstimationObserver.onChanged(expected) }
    }

    @Test
    fun `When new quota is less than current quota, should return true`() = runBlocking {
        //Given
        val newQuota = 25
        val currentQuota = 30
        val expected = true


        //When
        val actual = viewModel.isQuotaDecreased(currentQuota, newQuota)

        //Then
        assertEquals(expected, actual)
    }

    @Test
    fun `When new quota is bigger than current quota, should return false`() = runBlocking {
        //Given
        val newQuota = 50
        val currentQuota = 30
        val expected = false


        //When
        val actual = viewModel.isQuotaDecreased(currentQuota, newQuota)

        //Then
        assertEquals(expected, actual)
    }

    @Test
    fun `When coupon status id is ongoing, should return true`() = runBlocking {
        //Given
        val expected = true


        //When
        val actual = viewModel.isOngoingCoupon(VoucherStatusConst.ONGOING)

        //Then
        assertEquals(expected, actual)
    }

    @Test
    fun `When coupon status id is not ongoing, should return false`() = runBlocking {
        //Given
        val expected = false

        //When
        val actual = viewModel.isOngoingCoupon(VoucherStatusConst.NOT_STARTED)

        //Then
        assertEquals(expected, actual)
    }

}