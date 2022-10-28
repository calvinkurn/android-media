package com.tokopedia.vouchercreation.product.create.view.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.unit.test.ext.getOrAwaitValue
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.vouchercreation.product.create.domain.entity.CouponInformation
import com.tokopedia.vouchercreation.product.create.view.uimodel.CouponTargetEnum
import com.tokopedia.vouchercreation.shop.create.domain.usecase.validation.PeriodValidationUseCase
import com.tokopedia.vouchercreation.shop.create.domain.usecase.validation.VoucherTargetValidationUseCase
import com.tokopedia.vouchercreation.shop.create.view.uimodel.validation.PeriodValidation
import com.tokopedia.vouchercreation.shop.create.view.uimodel.validation.VoucherTargetValidation
import io.mockk.MockKAnnotations
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.spyk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.resetMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.util.*

@ExperimentalCoroutinesApi
class CreateCouponDetailViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @RelaxedMockK
    lateinit var voucherTargetValidationUseCase: VoucherTargetValidationUseCase

    @RelaxedMockK
    lateinit var periodValidationUseCase: PeriodValidationUseCase

    val viewModel: CreateCouponDetailViewModel by lazy {
        spyk(CreateCouponDetailViewModel(
            CoroutineTestDispatchersProvider,
            voucherTargetValidationUseCase,
            periodValidationUseCase))
    }

    @Before
    fun setup() {
        MockKAnnotations.init(this)
    }

    @After
    fun cleanup() {
        clearAllMocks()
        Dispatchers.resetMain()
    }

    @Test
    fun `When validateCouponTarget success Expect couponValidationResult and allInputValid value invoked`() {
        coEvery {
            voucherTargetValidationUseCase.executeOnBackground()
        } returns VoucherTargetValidation()

        viewModel.validateCouponTarget("", "")
        val result = viewModel.couponValidationResult.getOrAwaitValue()
        assert(result is Success)
        assertFalse(viewModel.allInputValid.getOrAwaitValue())
    }

    @Test
    fun `When validateCouponTarget error Expect couponValidationResult Fail invoked`() {
        coEvery {
            voucherTargetValidationUseCase.executeOnBackground()
        } throws MessageErrorException()

        viewModel.validateCouponTarget("", "")
        val result = viewModel.couponValidationResult.getOrAwaitValue()
        assert(result is Fail)
    }

    @Test
    fun `When validateCouponPeriod success Expect periodValidationLiveData and allInputValid value invoked`() {
        coEvery {
            periodValidationUseCase.executeOnBackground()
        } returns PeriodValidation()

        viewModel.validateCouponPeriod()
        val result = viewModel.periodValidationLiveData.getOrAwaitValue()
        assert(result is Success)
        assertFalse(viewModel.allInputValid.getOrAwaitValue())
    }

    @Test
    fun `When validateCouponPeriod error Expect periodValidationLiveData Fail invoked`() {
        coEvery {
            periodValidationUseCase.executeOnBackground()
        } throws MessageErrorException()

        viewModel.validateCouponPeriod()
        val couponlistResult = viewModel.periodValidationLiveData.getOrAwaitValue()
        assert(couponlistResult is Fail)
    }

    @Test
    fun `When setCouponInformation Expect local variables invoked`() {
        val dummyCalendar = GregorianCalendar()
        viewModel.setCouponInformation(CouponInformation(
            target = CouponInformation.Target.NOT_SELECTED,
            name = "",
            code = "",
            period = CouponInformation.Period(dummyCalendar.time, dummyCalendar.time)
        ))
        viewModel.populateCouponTarget()
        viewModel.populateCouponTarget(CouponTargetEnum.PUBLIC)
        viewModel.populateCouponTarget(CouponTargetEnum.PRIVATE)

        assertEquals(2, viewModel.couponTargetList.getOrAwaitValue().size)
        assertEquals(CouponTargetEnum.NOT_SELECTED, viewModel.selectedCouponTarget.getOrAwaitValue())
        assertEquals(CouponTargetEnum.NOT_SELECTED, viewModel.selectedCouponTargetValue)
        assertEquals(dummyCalendar, viewModel.startDateCalendarLiveData.getOrAwaitValue())
        assertEquals(dummyCalendar, viewModel.endDateCalendarLiveData.getOrAwaitValue())
    }

    @Test
    fun `When validateMinCharCoupon Expect return correct data`() {
        assertFalse(viewModel.validateMinCharCouponCode("12345"))
        assert(viewModel.validateMinCharCouponCode("123456"))
        assertFalse(viewModel.validateMinCharCouponName("12345"))
        assert(viewModel.validateMinCharCouponName("123456"))
    }

    @Test
    fun `When validateAllInput expect allInputValid invoked correct data`() {
        runValidateAllInputTest("", "", true)
        runValidateAllInputTest("", "error", false)
        runValidateAllInputTest("error", "", false)
        runValidateAllInputTest("error", "error", false)
    }

    private fun runValidateAllInputTest(periodError: String, targetError: String, expected: Boolean) {
        coEvery { periodValidationUseCase.executeOnBackground() } returns PeriodValidation(periodError)
        coEvery { voucherTargetValidationUseCase.executeOnBackground() } returns VoucherTargetValidation(targetError)

        viewModel.validateCouponTarget("", "")
        viewModel.validateCouponPeriod()

        // await all data invoked
        viewModel.couponValidationResult.getOrAwaitValue()
        viewModel.periodValidationLiveData.getOrAwaitValue()

        // run check
        assertEquals(expected, viewModel.allInputValid.getOrAwaitValue())
    }
}
