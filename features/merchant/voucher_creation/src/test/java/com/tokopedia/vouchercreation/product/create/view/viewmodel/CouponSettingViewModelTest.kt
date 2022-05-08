package com.tokopedia.vouchercreation.product.create.view.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.unit.test.ext.getOrAwaitValue
import com.tokopedia.vouchercreation.common.utils.ResourceProvider
import com.tokopedia.vouchercreation.product.create.domain.entity.CouponType
import com.tokopedia.vouchercreation.product.create.domain.entity.MinimumPurchaseType
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class CouponSettingViewModelTest {

    @RelaxedMockK
    lateinit var resourceProvider: ResourceProvider

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private val viewModel by lazy { CouponSettingViewModel(resourceProvider) }

    @Before
    fun setup() {
        MockKAnnotations.init(this)
    }


    @Test
    fun `When cashback amount less than minimum, should return false`() = runBlocking {
        //Given
        val cashbackDiscountAmount = 4000

        //When
        val actual = viewModel.isValidCashbackDiscountAmount(cashbackDiscountAmount)

        //Then
        assertEquals(false, actual)
    }

    @Test
    fun `When cashback amount bigger than minimum, should return true`() = runBlocking {
        //Given
        val cashbackDiscountAmount = 100_000_000

        //When
        val actual = viewModel.isValidCashbackDiscountAmount(cashbackDiscountAmount)

        //Then
        assertEquals(true, actual)
    }

    @Test
    fun `When minimum purchase type is none,  should return false`() = runBlocking {
        //Given
        val minimumPurchase = 0
        val cashbackDiscountAmount = 0

        //When
        val actual = viewModel.isValidCashbackMinimumPurchase(
            minimumPurchase,
            cashbackDiscountAmount,
            MinimumPurchaseType.NONE
        )

        //Then
        assertEquals(false, actual)
    }


    @Test
    fun `When minimum purchase type is nominal and minimum purchase bigger than discount amount, should return true`() = runBlocking {
        //Given
        val minimumPurchase = 500_000
        val cashbackDiscountAmount = 250_000

        //When
        val actual = viewModel.isValidCashbackMinimumPurchase(
            minimumPurchase,
            cashbackDiscountAmount,
            MinimumPurchaseType.NOMINAL
        )

        //Then
        assertEquals(true, actual)
    }

    @Test
    fun `When minimum purchase type is nominal and minimum purchase less than discount amount, should return false`() = runBlocking {
        //Given
        val minimumPurchase = 250_000
        val cashbackDiscountAmount = 500_000

        //When
        val actual = viewModel.isValidCashbackMinimumPurchase(
            minimumPurchase,
            cashbackDiscountAmount,
            MinimumPurchaseType.NOMINAL
        )

        //Then
        assertEquals(false, actual)
    }


    @Test
    fun `When minimum purchase type is quantity and minimum purchase is zero, should return false`() = runBlocking {
        //Given
        val minimumPurchase = 0
        val cashbackDiscountAmount = 0

        //When
        val actual = viewModel.isValidCashbackMinimumPurchase(
            minimumPurchase,
            cashbackDiscountAmount,
            MinimumPurchaseType.QUANTITY
        )

        //Then
        assertEquals(false, actual)
    }

    @Test
    fun `When minimum purchase type is quantity and minimum purchase is greater than zero, should return true`() = runBlocking {
        //Given
        val minimumPurchase = 500_000
        val cashbackDiscountAmount = 0

        //When
        val actual = viewModel.isValidCashbackMinimumPurchase(
            minimumPurchase,
            cashbackDiscountAmount,
            MinimumPurchaseType.QUANTITY
        )

        //Then
        assertEquals(true, actual)
    }

    @Test
    fun `When minimum purchase type is nothing, should return true`() = runBlocking {
        //Given
        val minimumPurchase = 0
        val cashbackDiscountAmount = 0

        //When
        val actual = viewModel.isValidCashbackMinimumPurchase(
            minimumPurchase,
            cashbackDiscountAmount,
            MinimumPurchaseType.NOTHING
        )

        //Then
        assertEquals(true, actual)
    }

    @Test
    fun `When free shipping discount amount less than minimum, should return false`() = runBlocking {
        //Given
        val freeShippingDiscountAmount = 4000

        //When
        val actual = viewModel.isValidFreeShippingDiscountAmount(freeShippingDiscountAmount)

        //Then
        assertEquals(false, actual)
    }

    @Test
    fun `When free shipping discount amount bigger than minimum, should return true`() = runBlocking {
        //Given
        val freeShippingDiscountAmount = 100_000

        //When
        val actual = viewModel.isValidFreeShippingDiscountAmount(freeShippingDiscountAmount)

        //Then
        assertEquals(true, actual)
    }

    @Test
    fun `When cashback amount less than minimum, should return BelowAllowedMinimumPercentage`() = runBlocking {
        //Given
        val cashbackPercentage = 1

        //When
        val actual = viewModel.isValidCashbackPercentage(cashbackPercentage)

        //Then
        assertEquals(CouponSettingViewModel.CashbackPercentageState.BelowAllowedMinimumPercentage, actual)
    }

    @Test
    fun `When cashback amount bigger than maximum, should return ExceedAllowedMaximumPercentage`() = runBlocking {
        //Given
        val cashbackPercentage = 101

        //When
        val actual = viewModel.isValidCashbackPercentage(cashbackPercentage)

        //Then
        assertEquals(CouponSettingViewModel.CashbackPercentageState.ExceedAllowedMaximumPercentage, actual)
    }

    @Test
    fun `When cashback amount bigger than maximum, should return ValidPercentage`() = runBlocking {
        //Given
        val cashbackPercentage = 50

        //When
        val actual = viewModel.isValidCashbackPercentage(cashbackPercentage)

        //Then
        assertEquals(CouponSettingViewModel.CashbackPercentageState.ValidPercentage, actual)
    }

    @Test
    fun `When cashback amount less than minimum, should return BelowAllowedMinimumAmount`() = runBlocking {
        //Given
        val cashbackDiscountAmount = 4_000

        //When
        val actual = viewModel.isValidMaximumCashbackAmount(cashbackDiscountAmount)

        //Then
        assertEquals(CouponSettingViewModel.CashbackAmountState.BelowAllowedMinimumAmount, actual)
    }

    @Test
    fun `When cashback amount bigger than maximum, should return ExceedAllowedMinimumAmount`() = runBlocking {
        //Given
        val cashbackDiscountAmount = 100_000_000

        //When
        val actual = viewModel.isValidMaximumCashbackAmount(cashbackDiscountAmount)

        //Then
        assertEquals(CouponSettingViewModel.CashbackAmountState.ExceedAllowedMinimumAmount, actual)
    }

    @Test
    fun `When cashback amount is in valid range, should return ValidAmount`() = runBlocking {
        //Given
        val cashbackDiscountAmount = 100_000

        //When
        val actual = viewModel.isValidMaximumCashbackAmount(cashbackDiscountAmount)

        //Then
        assertEquals(CouponSettingViewModel.CashbackAmountState.ValidAmount, actual)
    }

    @Test
    fun `When free shipping minimum purchase bigger than discount amount, should return true`() = runBlocking {
        //Given
        val freeShippingMinimumPurchase = 500_000
        val freeShippingDiscountAmount = 100_000

        //When
        val actual = viewModel.isValidFreeShippingMinimumPurchase(
            freeShippingMinimumPurchase,
            freeShippingDiscountAmount
        )

        //Then
        assertEquals(true, actual)
    }

    @Test
    fun `When free shipping minimum purchase less than discount amount, should return false`() = runBlocking {
        //Given
        val freeShippingMinimumPurchase = 50_000
        val freeShippingDiscountAmount = 100_000

        //When
        val actual = viewModel.isValidFreeShippingMinimumPurchase(
            freeShippingMinimumPurchase,
            freeShippingDiscountAmount
        )

        //Then
        assertEquals(false, actual)
    }

    @Test
    fun `When quota quantity less than minimum, should return BelowAllowedQuotaAmount`() = runBlocking {
        //Given
        val quotaQuantity = 0

        //When
        val actual = viewModel.isValidQuota(quotaQuantity)

        //Then
        assertEquals(CouponSettingViewModel.QuotaState.BelowAllowedQuotaAmount, actual)
    }

    @Test
    fun `When quota quantity bigger than maximum, should return ExceedAllowedQuotaAmount`() = runBlocking {
        //Given
        val quotaQuantity = 1_000

        //When
        val actual = viewModel.isValidQuota(quotaQuantity)

        //Then
        assertEquals(CouponSettingViewModel.QuotaState.ExceedAllowedQuotaAmount, actual)
    }

    @Test
    fun `When quota quantity is in valid range, should return ValidQuota`() = runBlocking {
        //Given
        val quotaQuantity = 20

        //When
        val actual = viewModel.isValidQuota(quotaQuantity)

        //Then
        assertEquals(CouponSettingViewModel.QuotaState.ValidQuota, actual)
    }

    @Test
    fun `When coupon type changed, coupon type should be updated and previous input validation should be false`() = runBlocking {
        //Given
        val couponType = CouponType.FREE_SHIPPING

        //When
        viewModel.couponTypeChanged(couponType)

        //Then
        assertEquals(viewModel.couponType.getOrAwaitValue(), couponType)
        assertEquals(viewModel.areInputValid.getOrAwaitValue(), false)
    }
}