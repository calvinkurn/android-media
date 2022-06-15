package com.tokopedia.vouchercreation.product.create.view.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.unit.test.ext.getOrAwaitValue
import com.tokopedia.vouchercreation.common.utils.ResourceProvider
import com.tokopedia.vouchercreation.product.create.domain.entity.CouponSettings
import com.tokopedia.vouchercreation.product.create.domain.entity.CouponType
import com.tokopedia.vouchercreation.product.create.domain.entity.DiscountType
import com.tokopedia.vouchercreation.product.create.domain.entity.MinimumPurchaseType
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.verify
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

    //region Coupon Type: None
    @Test
    fun `When validating input and coupon type is not selected, observer should receive false as a result`() {
        //Given

        val couponType = CouponType.NONE

        val discountType = DiscountType.NONE
        val minimumPurchaseType = MinimumPurchaseType.NONE
        val cashbackPercentage = 0
        val cashbackMaximumDiscountAmount = 0
        val cashbackDiscountAmount = 0
        val cashbackMinimumPurchase = 0
        val cashbackQuota = 0

        val freeShippingDiscountAmount = 0
        val freeShippingMinimumPurchase = 0
        val freeShippingQuota = 0

        //When
        viewModel.validateInput(
            couponType,
            discountType,
            minimumPurchaseType,
            cashbackPercentage,
            cashbackMaximumDiscountAmount,
            cashbackDiscountAmount,
            cashbackMinimumPurchase,
            cashbackQuota,
            freeShippingDiscountAmount,
            freeShippingMinimumPurchase,
            freeShippingQuota
        )

        //Then
        assertEquals(false, viewModel.areInputValid.getOrAwaitValue())
    }
    //endregion

    //region Coupon Type: Cashback, Discount Type : Nominal, Discount amount is valid
    @Test
    fun `When discount amount is valid, observer should receive true as a result`() {
        //Given

        val couponType = CouponType.CASHBACK

        val discountType = DiscountType.NOMINAL
        val minimumPurchaseType = MinimumPurchaseType.NOMINAL
        val cashbackPercentage = 0
        val cashbackMaximumDiscountAmount = 500_000
        val cashbackDiscountAmount = 100_000
        val cashbackMinimumPurchase = 1_000_000
        val cashbackQuota = 20

        val freeShippingDiscountAmount = 0
        val freeShippingMinimumPurchase = 0
        val freeShippingQuota = 0

        //When
        viewModel.validateInput(
            couponType,
            discountType,
            minimumPurchaseType,
            cashbackPercentage,
            cashbackMaximumDiscountAmount,
            cashbackDiscountAmount,
            cashbackMinimumPurchase,
            cashbackQuota,
            freeShippingDiscountAmount,
            freeShippingMinimumPurchase,
            freeShippingQuota
        )

        //Then
        assertEquals(true, viewModel.areInputValid.getOrAwaitValue())
    }
    //endregion
    //region Coupon Type: Cashback, Discount Type : Nominal, Discount amount is invalid
    @Test
    fun `When discount amount is invalid, observer should receive false as a result`() {
        //Given

        val couponType = CouponType.CASHBACK

        val discountType = DiscountType.NOMINAL
        val minimumPurchaseType = MinimumPurchaseType.QUANTITY
        val cashbackPercentage = 0
        val cashbackMaximumDiscountAmount = 500_000
        val cashbackDiscountAmount = 0
        val cashbackMinimumPurchase = 1
        val cashbackQuota = 0

        val freeShippingDiscountAmount = 0
        val freeShippingMinimumPurchase = 0
        val freeShippingQuota = 0

        //When
        viewModel.validateInput(
            couponType,
            discountType,
            minimumPurchaseType,
            cashbackPercentage,
            cashbackMaximumDiscountAmount,
            cashbackDiscountAmount,
            cashbackMinimumPurchase,
            cashbackQuota,
            freeShippingDiscountAmount,
            freeShippingMinimumPurchase,
            freeShippingQuota
        )

        //Then
        assertEquals(false, viewModel.areInputValid.getOrAwaitValue())
    }
    //endregion

    //region Coupon Type: Cashback, Discount Type : Nominal, Minimum purchase selected
    @Test
    fun `When minimum purchase is selected, observer should receive true as a result`() {
        //Given

        val couponType = CouponType.CASHBACK

        val discountType = DiscountType.NOMINAL
        val minimumPurchaseType = MinimumPurchaseType.NOMINAL
        val cashbackPercentage = 0
        val cashbackMaximumDiscountAmount = 500_000
        val cashbackDiscountAmount = 100_000
        val cashbackMinimumPurchase = 1_000_000
        val cashbackQuota = 20

        val freeShippingDiscountAmount = 0
        val freeShippingMinimumPurchase = 0
        val freeShippingQuota = 0

        //When
        viewModel.validateInput(
            couponType,
            discountType,
            minimumPurchaseType,
            cashbackPercentage,
            cashbackMaximumDiscountAmount,
            cashbackDiscountAmount,
            cashbackMinimumPurchase,
            cashbackQuota,
            freeShippingDiscountAmount,
            freeShippingMinimumPurchase,
            freeShippingQuota
        )

        //Then
        assertEquals(true, viewModel.areInputValid.getOrAwaitValue())
    }
    //endregion
    //region Coupon Type: Cashback, Discount Type : Nominal, Minimum purchase not selected
    @Test
    fun `When minimum purchase is not selected, observer should receive false as a result`() {
        //Given

        val couponType = CouponType.CASHBACK

        val discountType = DiscountType.NOMINAL
        val minimumPurchaseType = MinimumPurchaseType.NONE
        val cashbackPercentage = 0
        val cashbackMaximumDiscountAmount = 500_000
        val cashbackDiscountAmount = 100_000
        val cashbackMinimumPurchase = 10_000
        val cashbackQuota = 20

        val freeShippingDiscountAmount = 0
        val freeShippingMinimumPurchase = 0
        val freeShippingQuota = 0

        //When
        viewModel.validateInput(
            couponType,
            discountType,
            minimumPurchaseType,
            cashbackPercentage,
            cashbackMaximumDiscountAmount,
            cashbackDiscountAmount,
            cashbackMinimumPurchase,
            cashbackQuota,
            freeShippingDiscountAmount,
            freeShippingMinimumPurchase,
            freeShippingQuota
        )

        //Then
        assertEquals(false, viewModel.areInputValid.getOrAwaitValue())
    }
    //endregion

    //region Coupon Type: Cashback, Discount Type : Nominal, Minimal Purchase Valid
    @Test
    fun `When minimum purchase is greater than discount amount, observer should receive true as a result`() {
        //Given

        val couponType = CouponType.CASHBACK

        val discountType = DiscountType.NOMINAL
        val minimumPurchaseType = MinimumPurchaseType.NOMINAL
        val cashbackPercentage = 0
        val cashbackMaximumDiscountAmount = 500_000
        val cashbackDiscountAmount = 100_000
        val cashbackMinimumPurchase = 1_000_000
        val cashbackQuota = 20

        val freeShippingDiscountAmount = 0
        val freeShippingMinimumPurchase = 0
        val freeShippingQuota = 0

        //When
        viewModel.validateInput(
            couponType,
            discountType,
            minimumPurchaseType,
            cashbackPercentage,
            cashbackMaximumDiscountAmount,
            cashbackDiscountAmount,
            cashbackMinimumPurchase,
            cashbackQuota,
            freeShippingDiscountAmount,
            freeShippingMinimumPurchase,
            freeShippingQuota
        )

        //Then
        assertEquals(true, viewModel.areInputValid.getOrAwaitValue())
    }
    //endregion
    //region Coupon Type: Cashback, Discount Type : Nominal, Minimal Purchase Invalid
    @Test
    fun `When minimum purchase is less than discount amount, observer should receive false as a result`() {
        //Given

        val couponType = CouponType.CASHBACK

        val discountType = DiscountType.NOMINAL
        val minimumPurchaseType = MinimumPurchaseType.NOMINAL
        val cashbackPercentage = 0
        val cashbackMaximumDiscountAmount = 500_000
        val cashbackDiscountAmount = 100_000
        val cashbackMinimumPurchase = 10_000
        val cashbackQuota = 20

        val freeShippingDiscountAmount = 0
        val freeShippingMinimumPurchase = 0
        val freeShippingQuota = 0

        //When
        viewModel.validateInput(
            couponType,
            discountType,
            minimumPurchaseType,
            cashbackPercentage,
            cashbackMaximumDiscountAmount,
            cashbackDiscountAmount,
            cashbackMinimumPurchase,
            cashbackQuota,
            freeShippingDiscountAmount,
            freeShippingMinimumPurchase,
            freeShippingQuota
        )

        //Then
        assertEquals(false, viewModel.areInputValid.getOrAwaitValue())
    }
    //endregion

    //region Coupon Type: Cashback, Discount Type : Nominal, Valid Quota
    @Test
    fun `When quota is valid, observer should receive true as a result`() {
        //Given

        val couponType = CouponType.CASHBACK

        val discountType = DiscountType.NOMINAL
        val minimumPurchaseType = MinimumPurchaseType.QUANTITY
        val cashbackPercentage = 0
        val cashbackMaximumDiscountAmount = 500_000
        val cashbackDiscountAmount = 100_000
        val cashbackMinimumPurchase = 1
        val cashbackQuota = 200

        val freeShippingDiscountAmount = 0
        val freeShippingMinimumPurchase = 0
        val freeShippingQuota = 0

        //When
        viewModel.validateInput(
            couponType,
            discountType,
            minimumPurchaseType,
            cashbackPercentage,
            cashbackMaximumDiscountAmount,
            cashbackDiscountAmount,
            cashbackMinimumPurchase,
            cashbackQuota,
            freeShippingDiscountAmount,
            freeShippingMinimumPurchase,
            freeShippingQuota
        )

        //Then
        assertEquals(true, viewModel.areInputValid.getOrAwaitValue())
    }
    //endregion
    //region Coupon Type: Cashback, Discount Type : Nominal, Invalid Quota
    @Test
    fun `When quota is invalid, observer should receive false as a result`() {
        //Given

        val couponType = CouponType.CASHBACK

        val discountType = DiscountType.NOMINAL
        val minimumPurchaseType = MinimumPurchaseType.QUANTITY
        val cashbackPercentage = 0
        val cashbackMaximumDiscountAmount = 500_000
        val cashbackDiscountAmount = 100_000
        val cashbackMinimumPurchase = 1
        val cashbackQuota = 0

        val freeShippingDiscountAmount = 0
        val freeShippingMinimumPurchase = 0
        val freeShippingQuota = 0

        //When
        viewModel.validateInput(
            couponType,
            discountType,
            minimumPurchaseType,
            cashbackPercentage,
            cashbackMaximumDiscountAmount,
            cashbackDiscountAmount,
            cashbackMinimumPurchase,
            cashbackQuota,
            freeShippingDiscountAmount,
            freeShippingMinimumPurchase,
            freeShippingQuota
        )

        //Then
        assertEquals(false, viewModel.areInputValid.getOrAwaitValue())
    }
    //endregion

    //region Coupon Type: Cashback, Discount Type : Percentage, Cashback percentage is below minimum
    @Test
    fun `When discount type is percentage and cashback percentage is below minimum, observer should receive false as a result`() {
        //Given

        val couponType = CouponType.CASHBACK

        val discountType = DiscountType.PERCENTAGE
        val minimumPurchaseType = MinimumPurchaseType.NOMINAL
        val cashbackPercentage = 1
        val cashbackMaximumDiscountAmount = 500_000
        val cashbackDiscountAmount = 100_000
        val cashbackMinimumPurchase = 1_000_000
        val cashbackQuota = 20

        val freeShippingDiscountAmount = 0
        val freeShippingMinimumPurchase = 0
        val freeShippingQuota = 0

        //When
        viewModel.validateInput(
            couponType,
            discountType,
            minimumPurchaseType,
            cashbackPercentage,
            cashbackMaximumDiscountAmount,
            cashbackDiscountAmount,
            cashbackMinimumPurchase,
            cashbackQuota,
            freeShippingDiscountAmount,
            freeShippingMinimumPurchase,
            freeShippingQuota
        )

        //Then
        assertEquals(false, viewModel.areInputValid.getOrAwaitValue())
    }
    //endregion
    //region Coupon Type: Cashback, Discount Type : Percentage, Cashback percentage is above maximum
    @Test
    fun `When discount type is percentage and cashback percentage is above maximum, observer should receive false as a result`() {
        //Given

        val couponType = CouponType.CASHBACK

        val discountType = DiscountType.PERCENTAGE
        val minimumPurchaseType = MinimumPurchaseType.NONE
        val cashbackPercentage = 101
        val cashbackMaximumDiscountAmount = 500_000
        val cashbackDiscountAmount = 100_000
        val cashbackMinimumPurchase = 10_000
        val cashbackQuota = 20

        val freeShippingDiscountAmount = 0
        val freeShippingMinimumPurchase = 0
        val freeShippingQuota = 0

        //When
        viewModel.validateInput(
            couponType,
            discountType,
            minimumPurchaseType,
            cashbackPercentage,
            cashbackMaximumDiscountAmount,
            cashbackDiscountAmount,
            cashbackMinimumPurchase,
            cashbackQuota,
            freeShippingDiscountAmount,
            freeShippingMinimumPurchase,
            freeShippingQuota
        )

        //Then
        assertEquals(false, viewModel.areInputValid.getOrAwaitValue())
    }
    //endregion
    //region Coupon Type: Cashback, Discount Type : Percentage, Cashback percentage is in valid range
    @Test
    fun `When discount type is percentage and cashback percentage is in valid range, observer should receive true as a result`() {
        //Given

        val couponType = CouponType.CASHBACK

        val discountType = DiscountType.PERCENTAGE
        val minimumPurchaseType = MinimumPurchaseType.NOMINAL
        val cashbackPercentage = 50
        val cashbackMaximumDiscountAmount = 100_000
        val cashbackDiscountAmount = 100_000
        val cashbackMinimumPurchase = 200_000
        val cashbackQuota = 20

        val freeShippingDiscountAmount = 0
        val freeShippingMinimumPurchase = 0
        val freeShippingQuota = 0

        //When
        viewModel.validateInput(
            couponType,
            discountType,
            minimumPurchaseType,
            cashbackPercentage,
            cashbackMaximumDiscountAmount,
            cashbackDiscountAmount,
            cashbackMinimumPurchase,
            cashbackQuota,
            freeShippingDiscountAmount,
            freeShippingMinimumPurchase,
            freeShippingQuota
        )

        //Then
        assertEquals(true, viewModel.areInputValid.getOrAwaitValue())
    }
    //endregion

    //region Coupon Type: Cashback, Discount Type : Percentage, Maximum discount amount is below minimum
    @Test
    fun `When discount type is percentage and discount amount is below minimum, observer should receive false as a result`() {
        //Given

        val couponType = CouponType.CASHBACK

        val discountType = DiscountType.PERCENTAGE
        val minimumPurchaseType = MinimumPurchaseType.NOMINAL
        val cashbackPercentage = 20
        val cashbackMaximumDiscountAmount = 2_000
        val cashbackDiscountAmount = 500_000
        val cashbackMinimumPurchase = 1_000_000
        val cashbackQuota = 20

        val freeShippingDiscountAmount = 0
        val freeShippingMinimumPurchase = 0
        val freeShippingQuota = 0

        //When
        viewModel.validateInput(
            couponType,
            discountType,
            minimumPurchaseType,
            cashbackPercentage,
            cashbackMaximumDiscountAmount,
            cashbackDiscountAmount,
            cashbackMinimumPurchase,
            cashbackQuota,
            freeShippingDiscountAmount,
            freeShippingMinimumPurchase,
            freeShippingQuota
        )

        //Then
        assertEquals(false, viewModel.areInputValid.getOrAwaitValue())
    }
    //endregion
    //region Coupon Type: Cashback, Discount Type : Percentage, Maximum discount amount is above maximum
    @Test
    fun `When discount type is percentage and discount amount is above maximum, observer should receive false as a result`() {
        //Given

        val couponType = CouponType.CASHBACK

        val discountType = DiscountType.PERCENTAGE
        val minimumPurchaseType = MinimumPurchaseType.NOMINAL
        val cashbackPercentage = 20
        val cashbackMaximumDiscountAmount = 500_000
        val cashbackDiscountAmount = 100_000_000
        val cashbackMinimumPurchase = 120_000_000
        val cashbackQuota = 0

        val freeShippingDiscountAmount = 0
        val freeShippingMinimumPurchase = 0
        val freeShippingQuota = 0

        //When
        viewModel.validateInput(
            couponType,
            discountType,
            minimumPurchaseType,
            cashbackPercentage,
            cashbackMaximumDiscountAmount,
            cashbackDiscountAmount,
            cashbackMinimumPurchase,
            cashbackQuota,
            freeShippingDiscountAmount,
            freeShippingMinimumPurchase,
            freeShippingQuota
        )

        //Then
        assertEquals(false, viewModel.areInputValid.getOrAwaitValue())
    }
    //endregion
    //region Coupon Type: Cashback, Discount Type : Percentage, Maximum discount amount is in valid range
    @Test
    fun `When discount type is percentage and discount amount is in valid range, observer should receive true as a result`() {
        //Given

        val couponType = CouponType.CASHBACK

        val discountType = DiscountType.PERCENTAGE
        val minimumPurchaseType = MinimumPurchaseType.NOMINAL
        val cashbackPercentage = 20
        val cashbackMaximumDiscountAmount = 500_000
        val cashbackDiscountAmount = 200_000
        val cashbackMinimumPurchase = 1_000_000
        val cashbackQuota = 20

        val freeShippingDiscountAmount = 0
        val freeShippingMinimumPurchase = 0
        val freeShippingQuota = 0

        //When
        viewModel.validateInput(
            couponType,
            discountType,
            minimumPurchaseType,
            cashbackPercentage,
            cashbackMaximumDiscountAmount,
            cashbackDiscountAmount,
            cashbackMinimumPurchase,
            cashbackQuota,
            freeShippingDiscountAmount,
            freeShippingMinimumPurchase,
            freeShippingQuota
        )

        //Then
        assertEquals(true, viewModel.areInputValid.getOrAwaitValue())
    }
    //endregion

    //region Coupon Type: Cashback, Discount Type : Percentage, Minimum purchase selected
    @Test
    fun `When discount type is percentage and minimum purchase is selected, observer should receive true as a result`() {
        //Given

        val couponType = CouponType.CASHBACK

        val discountType = DiscountType.PERCENTAGE
        val minimumPurchaseType = MinimumPurchaseType.NOMINAL
        val cashbackPercentage = 20
        val cashbackMaximumDiscountAmount = 500_000
        val cashbackDiscountAmount = 100_000
        val cashbackMinimumPurchase = 1_000_000
        val cashbackQuota = 20

        val freeShippingDiscountAmount = 0
        val freeShippingMinimumPurchase = 0
        val freeShippingQuota = 0

        //When
        viewModel.validateInput(
            couponType,
            discountType,
            minimumPurchaseType,
            cashbackPercentage,
            cashbackMaximumDiscountAmount,
            cashbackDiscountAmount,
            cashbackMinimumPurchase,
            cashbackQuota,
            freeShippingDiscountAmount,
            freeShippingMinimumPurchase,
            freeShippingQuota
        )

        //Then
        assertEquals(true, viewModel.areInputValid.getOrAwaitValue())
    }
    //endregion
    //region Coupon Type: Cashback, Discount Type : Percentage, Minimum purchase not selected
    @Test
    fun `When discount type is percentage and minimum purchase is not selected, observer should receive false as a result`() {
        //Given

        val couponType = CouponType.CASHBACK

        val discountType = DiscountType.PERCENTAGE
        val minimumPurchaseType = MinimumPurchaseType.NONE
        val cashbackPercentage = 20
        val cashbackMaximumDiscountAmount = 500_000
        val cashbackDiscountAmount = 100_000
        val cashbackMinimumPurchase = 10_000
        val cashbackQuota = 20

        val freeShippingDiscountAmount = 0
        val freeShippingMinimumPurchase = 0
        val freeShippingQuota = 0

        //When
        viewModel.validateInput(
            couponType,
            discountType,
            minimumPurchaseType,
            cashbackPercentage,
            cashbackMaximumDiscountAmount,
            cashbackDiscountAmount,
            cashbackMinimumPurchase,
            cashbackQuota,
            freeShippingDiscountAmount,
            freeShippingMinimumPurchase,
            freeShippingQuota
        )

        //Then
        assertEquals(false, viewModel.areInputValid.getOrAwaitValue())
    }
    //endregion

    //region Coupon Type: Cashback, Discount Type : Percentage, Minimum Purchase: Valid
    @Test
    fun `When  discount type is percentage and minimum purchase is greater than discount amount, observer should receive true as a result`() {
        //Given

        val couponType = CouponType.CASHBACK

        val discountType = DiscountType.PERCENTAGE
        val minimumPurchaseType = MinimumPurchaseType.NOMINAL
        val cashbackPercentage = 20
        val cashbackMaximumDiscountAmount = 500_000
        val cashbackDiscountAmount = 100_000
        val cashbackMinimumPurchase = 1_000_000
        val cashbackQuota = 20

        val freeShippingDiscountAmount = 0
        val freeShippingMinimumPurchase = 0
        val freeShippingQuota = 0

        //When
        viewModel.validateInput(
            couponType,
            discountType,
            minimumPurchaseType,
            cashbackPercentage,
            cashbackMaximumDiscountAmount,
            cashbackDiscountAmount,
            cashbackMinimumPurchase,
            cashbackQuota,
            freeShippingDiscountAmount,
            freeShippingMinimumPurchase,
            freeShippingQuota
        )

        //Then
        assertEquals(true, viewModel.areInputValid.getOrAwaitValue())
    }
    //endregion
    //region Coupon Type: Cashback, Discount Type : Percentage, Minimum Purchase: Invalid
    @Test
    fun `When discount type is percentage and minimum purchase is less than discount amount, observer should receive false as a result`() {
        //Given

        val couponType = CouponType.CASHBACK

        val discountType = DiscountType.PERCENTAGE
        val minimumPurchaseType = MinimumPurchaseType.NOMINAL
        val cashbackPercentage = 20
        val cashbackMaximumDiscountAmount = 500_000
        val cashbackDiscountAmount = 100_000
        val cashbackMinimumPurchase = 10_000
        val cashbackQuota = 20

        val freeShippingDiscountAmount = 0
        val freeShippingMinimumPurchase = 0
        val freeShippingQuota = 0

        //When
        viewModel.validateInput(
            couponType,
            discountType,
            minimumPurchaseType,
            cashbackPercentage,
            cashbackMaximumDiscountAmount,
            cashbackDiscountAmount,
            cashbackMinimumPurchase,
            cashbackQuota,
            freeShippingDiscountAmount,
            freeShippingMinimumPurchase,
            freeShippingQuota
        )

        //Then
        assertEquals(false, viewModel.areInputValid.getOrAwaitValue())
    }
    //endregion

    //region Coupon Type: Cashback, Discount Type : Percentage, Valid Quota
    @Test
    fun `When discount type is percentage and quota is valid, observer should receive true as a result`() {
        //Given

        val couponType = CouponType.CASHBACK

        val discountType = DiscountType.PERCENTAGE
        val minimumPurchaseType = MinimumPurchaseType.QUANTITY
        val cashbackPercentage = 20
        val cashbackMaximumDiscountAmount = 500_000
        val cashbackDiscountAmount = 100_000
        val cashbackMinimumPurchase = 1
        val cashbackQuota = 200

        val freeShippingDiscountAmount = 0
        val freeShippingMinimumPurchase = 0
        val freeShippingQuota = 0

        //When
        viewModel.validateInput(
            couponType,
            discountType,
            minimumPurchaseType,
            cashbackPercentage,
            cashbackMaximumDiscountAmount,
            cashbackDiscountAmount,
            cashbackMinimumPurchase,
            cashbackQuota,
            freeShippingDiscountAmount,
            freeShippingMinimumPurchase,
            freeShippingQuota
        )

        //Then
        assertEquals(true, viewModel.areInputValid.getOrAwaitValue())
    }
    //endregion
    //region Coupon Type: Cashback, Discount Type : Percentage, Invalid Quota
    @Test
    fun `When discount type is percentage and quota is invalid, observer should receive false as a result`() {
        //Given

        val couponType = CouponType.CASHBACK

        val discountType = DiscountType.PERCENTAGE
        val minimumPurchaseType = MinimumPurchaseType.QUANTITY
        val cashbackPercentage = 20
        val cashbackMaximumDiscountAmount = 500_000
        val cashbackDiscountAmount = 100_000
        val cashbackMinimumPurchase = 1
        val cashbackQuota = 0

        val freeShippingDiscountAmount = 0
        val freeShippingMinimumPurchase = 0
        val freeShippingQuota = 0

        //When
        viewModel.validateInput(
            couponType,
            discountType,
            minimumPurchaseType,
            cashbackPercentage,
            cashbackMaximumDiscountAmount,
            cashbackDiscountAmount,
            cashbackMinimumPurchase,
            cashbackQuota,
            freeShippingDiscountAmount,
            freeShippingMinimumPurchase,
            freeShippingQuota
        )

        //Then
        assertEquals(false, viewModel.areInputValid.getOrAwaitValue())
    }
    //endregion

    //region Coupon Type: Cashback, Discount Type : None, Valid Quota
    @Test
    fun `When discount type is none and quota is valid, observer should receive false as a result`() {
        //Given

        val couponType = CouponType.CASHBACK

        val discountType = DiscountType.NONE
        val minimumPurchaseType = MinimumPurchaseType.NOMINAL
        val cashbackPercentage = 20
        val cashbackMaximumDiscountAmount = 500_000
        val cashbackDiscountAmount = 100_000
        val cashbackMinimumPurchase = 500_000
        val cashbackQuota = 20

        val freeShippingDiscountAmount = 0
        val freeShippingMinimumPurchase = 0
        val freeShippingQuota = 0

        //When
        viewModel.validateInput(
            couponType,
            discountType,
            minimumPurchaseType,
            cashbackPercentage,
            cashbackMaximumDiscountAmount,
            cashbackDiscountAmount,
            cashbackMinimumPurchase,
            cashbackQuota,
            freeShippingDiscountAmount,
            freeShippingMinimumPurchase,
            freeShippingQuota
        )

        //Then
        assertEquals(false, viewModel.areInputValid.getOrAwaitValue())
    }
    //endregion
    //region Coupon Type: Cashback, Discount Type : None, Invalid Quota
    @Test
    fun `When discount type is none and and quota is invalid, observer should receive false as a result`() {
        //Given

        val couponType = CouponType.CASHBACK

        val discountType = DiscountType.NONE
        val minimumPurchaseType = MinimumPurchaseType.QUANTITY
        val cashbackPercentage = 20
        val cashbackMaximumDiscountAmount = 500_000
        val cashbackDiscountAmount = 100_000
        val cashbackMinimumPurchase = 500_000
        val cashbackQuota = 0

        val freeShippingDiscountAmount = 0
        val freeShippingMinimumPurchase = 0
        val freeShippingQuota = 0

        //When
        viewModel.validateInput(
            couponType,
            discountType,
            minimumPurchaseType,
            cashbackPercentage,
            cashbackMaximumDiscountAmount,
            cashbackDiscountAmount,
            cashbackMinimumPurchase,
            cashbackQuota,
            freeShippingDiscountAmount,
            freeShippingMinimumPurchase,
            freeShippingQuota
        )

        //Then
        assertEquals(false, viewModel.areInputValid.getOrAwaitValue())
    }
    //endregion

    //region Coupon Type: Free Shipping, Discount Amount is valid
    @Test
    fun `When discount amount is bigger than minimum amount, observer should receive true as a result`() {
        //Given

        val couponType = CouponType.FREE_SHIPPING

        val discountType = DiscountType.NONE
        val minimumPurchaseType = MinimumPurchaseType.NONE
        val cashbackPercentage = 0
        val cashbackMaximumDiscountAmount = 0
        val cashbackDiscountAmount = 0
        val cashbackMinimumPurchase = 0
        val cashbackQuota = 0

        val freeShippingDiscountAmount = 10_000
        val freeShippingMinimumPurchase = 50_000
        val freeShippingQuota = 20

        //When
        viewModel.validateInput(
            couponType,
            discountType,
            minimumPurchaseType,
            cashbackPercentage,
            cashbackMaximumDiscountAmount,
            cashbackDiscountAmount,
            cashbackMinimumPurchase,
            cashbackQuota,
            freeShippingDiscountAmount,
            freeShippingMinimumPurchase,
            freeShippingQuota
        )

        //Then
        assertEquals(true, viewModel.areInputValid.getOrAwaitValue())
    }

    //endregion

    //region Coupon Type: Free Shipping, Discount Amount is invalid
    @Test
    fun `When discount amount is less than minimum amount, observer should receive false as a result`() {
        //Given

        val couponType = CouponType.FREE_SHIPPING

        val discountType = DiscountType.NONE
        val minimumPurchaseType = MinimumPurchaseType.NONE
        val cashbackPercentage = 0
        val cashbackMaximumDiscountAmount = 0
        val cashbackDiscountAmount = 0
        val cashbackMinimumPurchase = 0
        val cashbackQuota = 0

        val freeShippingDiscountAmount = 3_000
        val freeShippingMinimumPurchase = 50_000
        val freeShippingQuota = 20

        //When
        viewModel.validateInput(
            couponType,
            discountType,
            minimumPurchaseType,
            cashbackPercentage,
            cashbackMaximumDiscountAmount,
            cashbackDiscountAmount,
            cashbackMinimumPurchase,
            cashbackQuota,
            freeShippingDiscountAmount,
            freeShippingMinimumPurchase,
            freeShippingQuota
        )

        //Then
        assertEquals(false, viewModel.areInputValid.getOrAwaitValue())
    }

    //endregion

    //region Coupon Type: Free Shipping, Minimum purchase is valid
    @Test
    fun `When free shipping minimum purchase amount is bigger than discount amount, observer should receive true as a result`() {
        //Given

        val couponType = CouponType.FREE_SHIPPING

        val discountType = DiscountType.NONE
        val minimumPurchaseType = MinimumPurchaseType.NONE
        val cashbackPercentage = 0
        val cashbackMaximumDiscountAmount = 0
        val cashbackDiscountAmount = 0
        val cashbackMinimumPurchase = 0
        val cashbackQuota = 0

        val freeShippingDiscountAmount = 10_000
        val freeShippingMinimumPurchase = 50_000
        val freeShippingQuota = 20

        //When
        viewModel.validateInput(
            couponType,
            discountType,
            minimumPurchaseType,
            cashbackPercentage,
            cashbackMaximumDiscountAmount,
            cashbackDiscountAmount,
            cashbackMinimumPurchase,
            cashbackQuota,
            freeShippingDiscountAmount,
            freeShippingMinimumPurchase,
            freeShippingQuota
        )

        //Then
        assertEquals(true, viewModel.areInputValid.getOrAwaitValue())
    }

    //endregion

    //region Coupon Type: Free Shipping, Minimum purchase is invalid
    @Test
    fun `When free shipping minimum purchase is less than discount amount, observer should receive false as a result`() {
        //Given

        val couponType = CouponType.FREE_SHIPPING

        val discountType = DiscountType.NONE
        val minimumPurchaseType = MinimumPurchaseType.NONE
        val cashbackPercentage = 0
        val cashbackMaximumDiscountAmount = 0
        val cashbackDiscountAmount = 0
        val cashbackMinimumPurchase = 0
        val cashbackQuota = 0

        val freeShippingDiscountAmount = 300_000
        val freeShippingMinimumPurchase = 50_000
        val freeShippingQuota = 20

        //When
        viewModel.validateInput(
            couponType,
            discountType,
            minimumPurchaseType,
            cashbackPercentage,
            cashbackMaximumDiscountAmount,
            cashbackDiscountAmount,
            cashbackMinimumPurchase,
            cashbackQuota,
            freeShippingDiscountAmount,
            freeShippingMinimumPurchase,
            freeShippingQuota
        )

        //Then
        assertEquals(false, viewModel.areInputValid.getOrAwaitValue())
    }

    //endregion

    //region Coupon Type: Free Shipping, Quota is bigger than maximum
    @Test
    fun `When free shipping quota is bigger than maximum quantity, observer should receive false as a result`() {
        //Given

        val couponType = CouponType.FREE_SHIPPING

        val discountType = DiscountType.NONE
        val minimumPurchaseType = MinimumPurchaseType.NONE
        val cashbackPercentage = 0
        val cashbackMaximumDiscountAmount = 0
        val cashbackDiscountAmount = 0
        val cashbackMinimumPurchase = 0
        val cashbackQuota = 0

        val freeShippingDiscountAmount = 10_000
        val freeShippingMinimumPurchase = 50_000
        val freeShippingQuota = 1000

        //When
        viewModel.validateInput(
            couponType,
            discountType,
            minimumPurchaseType,
            cashbackPercentage,
            cashbackMaximumDiscountAmount,
            cashbackDiscountAmount,
            cashbackMinimumPurchase,
            cashbackQuota,
            freeShippingDiscountAmount,
            freeShippingMinimumPurchase,
            freeShippingQuota
        )

        //Then
        assertEquals(false, viewModel.areInputValid.getOrAwaitValue())
    }
    //endregion

    //region Coupon Type: Free Shipping, Quota is less than minimum
    @Test
    fun `When free shipping quota is less than minimum quantity, observer should receive false as a result`() {
        //Given

        val couponType = CouponType.FREE_SHIPPING

        val discountType = DiscountType.NONE
        val minimumPurchaseType = MinimumPurchaseType.NONE
        val cashbackPercentage = 0
        val cashbackMaximumDiscountAmount = 0
        val cashbackDiscountAmount = 0
        val cashbackMinimumPurchase = 0
        val cashbackQuota = 0

        val freeShippingDiscountAmount = 300_000
        val freeShippingMinimumPurchase = 50_000
        val freeShippingQuota = 0

        //When
        viewModel.validateInput(
            couponType,
            discountType,
            minimumPurchaseType,
            cashbackPercentage,
            cashbackMaximumDiscountAmount,
            cashbackDiscountAmount,
            cashbackMinimumPurchase,
            cashbackQuota,
            freeShippingDiscountAmount,
            freeShippingMinimumPurchase,
            freeShippingQuota
        )

        //Then
        assertEquals(false, viewModel.areInputValid.getOrAwaitValue())
    }
    //endregion

    //region Coupon Type: Free Shipping, Quota is in valid range
    @Test
    fun `When free shipping quota is in valid range quantity, observer should receive true as a result`() {
        //Given

        val couponType = CouponType.FREE_SHIPPING

        val discountType = DiscountType.NONE
        val minimumPurchaseType = MinimumPurchaseType.NONE
        val cashbackPercentage = 0
        val cashbackMaximumDiscountAmount = 0
        val cashbackDiscountAmount = 0
        val cashbackMinimumPurchase = 0
        val cashbackQuota = 0

        val freeShippingDiscountAmount = 30_000
        val freeShippingMinimumPurchase = 50_000
        val freeShippingQuota = 20

        //When
        viewModel.validateInput(
            couponType,
            discountType,
            minimumPurchaseType,
            cashbackPercentage,
            cashbackMaximumDiscountAmount,
            cashbackDiscountAmount,
            cashbackMinimumPurchase,
            cashbackQuota,
            freeShippingDiscountAmount,
            freeShippingMinimumPurchase,
            freeShippingQuota
        )

        //Then
        assertEquals(true, viewModel.areInputValid.getOrAwaitValue())
    }
    //endregion

    @Test
    fun `When cashback amount less than minimum, should return false`() {
        //Given
        val cashbackDiscountAmount = 4000

        //When
        val actual = viewModel.isValidCashbackDiscountAmount(cashbackDiscountAmount)

        //Then
        assertEquals(false, actual)
    }

    @Test
    fun `When cashback amount bigger than minimum, should return true`() {
        //Given
        val cashbackDiscountAmount = 100_000_000

        //When
        val actual = viewModel.isValidCashbackDiscountAmount(cashbackDiscountAmount)

        //Then
        assertEquals(true, actual)
    }

    @Test
    fun `When minimum purchase type is none,  should return false`() {
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
    fun `When minimum purchase type is nominal and minimum purchase bigger than discount amount, should return true`() {
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
    fun `When minimum purchase type is nominal and minimum purchase less than discount amount, should return false`() {
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
    fun `When minimum purchase type is quantity and minimum purchase is zero, should return false`() {
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
    fun `When minimum purchase type is quantity and minimum purchase is greater than zero, should return true`() {
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
    fun `When minimum purchase type is nothing, should return true`() {
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
    fun `When free shipping discount amount less than minimum, should return false`() {
        //Given
        val freeShippingDiscountAmount = 4000

        //When
        val actual = viewModel.isValidFreeShippingDiscountAmount(freeShippingDiscountAmount)

        //Then
        assertEquals(false, actual)
    }

    @Test
    fun `When free shipping discount amount bigger than minimum, should return true`() {
        //Given
        val freeShippingDiscountAmount = 100_000

        //When
        val actual = viewModel.isValidFreeShippingDiscountAmount(freeShippingDiscountAmount)

        //Then
        assertEquals(true, actual)
    }

    @Test
    fun `When cashback amount less than minimum, should return BelowAllowedMinimumPercentage`() {
        //Given
        val cashbackPercentage = 1

        //When
        val actual = viewModel.isValidCashbackPercentage(cashbackPercentage)

        //Then
        assertEquals(
            CouponSettingViewModel.CashbackPercentageState.BelowAllowedMinimumPercentage,
            actual
        )
    }

    @Test
    fun `When cashback amount bigger than maximum, should return ExceedAllowedMaximumPercentage`() {
        //Given
        val cashbackPercentage = 101

        //When
        val actual = viewModel.isValidCashbackPercentage(cashbackPercentage)

        //Then
        assertEquals(
            CouponSettingViewModel.CashbackPercentageState.ExceedAllowedMaximumPercentage,
            actual
        )
    }

    @Test
    fun `When cashback amount bigger than maximum, should return ValidPercentage`() {
        //Given
        val cashbackPercentage = 50

        //When
        val actual = viewModel.isValidCashbackPercentage(cashbackPercentage)

        //Then
        assertEquals(CouponSettingViewModel.CashbackPercentageState.ValidPercentage, actual)
    }

    @Test
    fun `When cashback amount less than minimum, should return BelowAllowedMinimumAmount`() {
        //Given
        val cashbackDiscountAmount = 4_000

        //When
        val actual = viewModel.isValidMaximumCashbackAmount(cashbackDiscountAmount)

        //Then
        assertEquals(CouponSettingViewModel.CashbackAmountState.BelowAllowedMinimumAmount, actual)
    }

    @Test
    fun `When cashback amount bigger than maximum, should return ExceedAllowedMinimumAmount`() {
        //Given
        val cashbackDiscountAmount = 100_000_000

        //When
        val actual = viewModel.isValidMaximumCashbackAmount(cashbackDiscountAmount)

        //Then
        assertEquals(CouponSettingViewModel.CashbackAmountState.ExceedAllowedMinimumAmount, actual)
    }

    @Test
    fun `When cashback amount is in valid range, should return ValidAmount`() {
        //Given
        val cashbackDiscountAmount = 100_000

        //When
        val actual = viewModel.isValidMaximumCashbackAmount(cashbackDiscountAmount)

        //Then
        assertEquals(CouponSettingViewModel.CashbackAmountState.ValidAmount, actual)
    }

    @Test
    fun `When free shipping minimum purchase bigger than discount amount, should return true`() {
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
    fun `When free shipping minimum purchase less than discount amount, should return false`() {
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
    fun `When quota quantity less than minimum, should return BelowAllowedQuotaAmount`() {
        //Given
        val quotaQuantity = 0

        //When
        val actual = viewModel.isValidQuota(quotaQuantity)

        //Then
        assertEquals(CouponSettingViewModel.QuotaState.BelowAllowedQuotaAmount, actual)
    }

    @Test
    fun `When quota quantity bigger than maximum, should return ExceedAllowedQuotaAmount`() {
        //Given
        val quotaQuantity = 1_000

        //When
        val actual = viewModel.isValidQuota(quotaQuantity)

        //Then
        assertEquals(CouponSettingViewModel.QuotaState.ExceedAllowedQuotaAmount, actual)
    }

    @Test
    fun `When quota quantity is in valid range, should return ValidQuota`() {
        //Given
        val quotaQuantity = 20

        //When
        val actual = viewModel.isValidQuota(quotaQuantity)

        //Then
        assertEquals(CouponSettingViewModel.QuotaState.ValidQuota, actual)
    }

    @Test
    fun `When coupon type changed, coupon type should be updated and previous input validation should be false`() {
        //Given
        val couponType = CouponType.FREE_SHIPPING

        //When
        viewModel.couponTypeChanged(couponType)

        //Then
        assertEquals(couponType, viewModel.couponType.getOrAwaitValue())
        assertEquals(false, viewModel.areInputValid.getOrAwaitValue())
    }

    @Test
    fun `When coupon type is cashback and minimum purchase is nominal, should return correct error message`() {
        //Given
        val couponType = CouponType.CASHBACK
        val minimumPurchaseType = MinimumPurchaseType.NOMINAL

        //When
        viewModel.getMinimalPurchaseErrorMessage(couponType, minimumPurchaseType)

        //Then
        verify { resourceProvider.getInvalidMinimalPurchaseNominalErrorMessage() }
    }

    @Test
    fun `When coupon type is cashback and minimum purchase is quantity, should return correct error message`() {
        //Given
        val couponType = CouponType.CASHBACK
        val minimumPurchaseType = MinimumPurchaseType.QUANTITY

        //When
        viewModel.getMinimalPurchaseErrorMessage(couponType, minimumPurchaseType)

        //Then
        verify { resourceProvider.getInvalidMinimalQuantityQuantityErrorMessage() }
    }

    @Test
    fun `When coupon type is cashback and minimum purchase is none, should return correct error message`() {
        //Given
        val couponType = CouponType.CASHBACK
        val minimumPurchaseType = MinimumPurchaseType.NONE

        //When
        viewModel.getMinimalPurchaseErrorMessage(couponType, minimumPurchaseType)

        //Then
        verify { resourceProvider.getInvalidMinimalPurchaseNominalErrorMessage() }
    }

    @Test
    fun `When coupon type is cashback and minimum purchase is nothing, should return correct error message`() {
        //Given
        val couponType = CouponType.CASHBACK
        val minimumPurchaseType = MinimumPurchaseType.NOTHING

        //When
        viewModel.getMinimalPurchaseErrorMessage(couponType, minimumPurchaseType)

        //Then
        verify { resourceProvider.getInvalidMinimalPurchaseNominalErrorMessage() }
    }

    @Test
    fun `When coupon type is free shipping, should return correct error message`() {
        //Given
        val couponType = CouponType.FREE_SHIPPING
        val minimumPurchaseType = MinimumPurchaseType.NONE

        //When
        viewModel.getMinimalPurchaseErrorMessage(couponType, minimumPurchaseType)

        //Then
        verify { resourceProvider.getInvalidMinimalPurchaseNominalErrorMessage() }
    }

    @Test
    fun `When coupon type is not selected, should return correct error message`() {
        //Given
        val couponType = CouponType.NONE
        val minimumPurchaseType = MinimumPurchaseType.NONE

        //When
        viewModel.getMinimalPurchaseErrorMessage(couponType, minimumPurchaseType)

        //Then
        verify { resourceProvider.getInvalidMinimalPurchaseNominalErrorMessage() }
    }

    @Test
    fun `When coupon type is cashback and discount type is nominal, should return correct expense estimation`() {
        //Given
        val expected : Long = 100_000
        val couponType = CouponType.CASHBACK
        val discountType = DiscountType.NOMINAL
        val cashbackDiscountAmount = 20_000
        val cashbackMaximumDiscountAmount = 100_000
        val freeShippingDiscountAmount = 0
        val cashbackQuota = 5
        val freeShippingQuota = 0

        //When
        viewModel.calculateMaxExpenseEstimation(
            couponType,
            discountType,
            cashbackDiscountAmount,
            cashbackMaximumDiscountAmount,
            freeShippingDiscountAmount,
            cashbackQuota,
            freeShippingQuota
        )

        //Then
        assertEquals(expected, viewModel.maxExpenseEstimation.getOrAwaitValue())
    }

    @Test
    fun `When coupon type is cashback and discount type is percentage, should return correct expense estimation`() {
        //Given
        val expected : Long = 600_000
        val couponType = CouponType.CASHBACK
        val discountType = DiscountType.PERCENTAGE
        val cashbackDiscountAmount = 20_000
        val cashbackMaximumDiscountAmount = 120_000
        val freeShippingDiscountAmount = 0
        val cashbackQuota = 5
        val freeShippingQuota = 0

        //When
        viewModel.calculateMaxExpenseEstimation(
            couponType,
            discountType,
            cashbackDiscountAmount,
            cashbackMaximumDiscountAmount,
            freeShippingDiscountAmount,
            cashbackQuota,
            freeShippingQuota
        )

        //Then
        assertEquals(expected, viewModel.maxExpenseEstimation.getOrAwaitValue())
    }

    @Test
    fun `When coupon type is cashback and discount type is none, should return zero`() {
        //Given
        val expected: Long  = 0
        val couponType = CouponType.CASHBACK
        val discountType = DiscountType.NONE
        val cashbackDiscountAmount = 20_000
        val cashbackMaximumDiscountAmount = 120_000
        val freeShippingDiscountAmount = 0
        val cashbackQuota = 5
        val freeShippingQuota = 0

        //When
        viewModel.calculateMaxExpenseEstimation(
            couponType,
            discountType,
            cashbackDiscountAmount,
            cashbackMaximumDiscountAmount,
            freeShippingDiscountAmount,
            cashbackQuota,
            freeShippingQuota
        )

        //Then
        assertEquals(expected, viewModel.maxExpenseEstimation.getOrAwaitValue())
    }

    @Test
    fun `When coupon type is free shipping, should return correct expense estimation`() {
        //Given
        val expected : Long = 200_000
        val couponType = CouponType.FREE_SHIPPING
        val discountType = DiscountType.NONE
        val cashbackDiscountAmount = 0
        val cashbackMaximumDiscountAmount = 0
        val freeShippingDiscountAmount = 20_000
        val cashbackQuota = 0
        val freeShippingQuota = 10

        //When
        viewModel.calculateMaxExpenseEstimation(
            couponType,
            discountType,
            cashbackDiscountAmount,
            cashbackMaximumDiscountAmount,
            freeShippingDiscountAmount,
            cashbackQuota,
            freeShippingQuota
        )

        //Then
        assertEquals(expected, viewModel.maxExpenseEstimation.getOrAwaitValue())
    }

    @Test
    fun `When coupon type is not selected, should return zero`() {
        //Given
        val expected : Long = 0
        val couponType = CouponType.NONE
        val discountType = DiscountType.NONE
        val cashbackDiscountAmount = 0
        val cashbackMaximumDiscountAmount = 0
        val freeShippingDiscountAmount = 20_000
        val cashbackQuota = 0
        val freeShippingQuota = 10

        //When
        viewModel.calculateMaxExpenseEstimation(
            couponType,
            discountType,
            cashbackDiscountAmount,
            cashbackMaximumDiscountAmount,
            freeShippingDiscountAmount,
            cashbackQuota,
            freeShippingQuota
        )

        //Then
        assertEquals(expected, viewModel.maxExpenseEstimation.getOrAwaitValue())
    }

    @Test
    fun `When coupon type is not selected, should save correct value`() {
        //Given
        val expected = CouponSettings(
            CouponType.NONE,
            DiscountType.NONE,
            MinimumPurchaseType.NONE,
            0, 
            0, 
            0,
            0,
            0,
            0
        )
        val couponType = CouponType.NONE

        val discountType = DiscountType.NONE
        val minimumPurchaseType = MinimumPurchaseType.NONE
        val cashbackPercentage = 0
        val cashbackMaximumDiscountAmount = 0
        val cashbackDiscountAmount = 0
        val cashbackMinimumPurchase = 0
        val cashbackQuota = 0

        val freeShippingDiscountAmount = 0
        val freeShippingMinimumPurchase = 0
        val freeShippingQuota = 0

        val estimatedMaxExpense : Long = 0

        //When
        viewModel.saveCoupon(
            couponType,
            discountType,
            minimumPurchaseType,
            cashbackPercentage,
            cashbackMaximumDiscountAmount,
            cashbackDiscountAmount,
            cashbackMinimumPurchase,
            cashbackQuota,
            freeShippingDiscountAmount,
            freeShippingMinimumPurchase,
            freeShippingQuota,
            estimatedMaxExpense
        )

        //Then
        assertEquals(expected, viewModel.saveCoupon.getOrAwaitValue())
    }

    @Test
    fun `When coupon type is free shipping, should save correct value`() {
        //Given
        val expected = CouponSettings(
            CouponType.FREE_SHIPPING,
            DiscountType.NONE,
            MinimumPurchaseType.NONE,
            20_000, 
            100, 
            20_000,
            5,
            200_000,
            250_000
        )
        val couponType = CouponType.FREE_SHIPPING

        val discountType = DiscountType.NONE
        val minimumPurchaseType = MinimumPurchaseType.NONE
        val cashbackPercentage = 0
        val cashbackMaximumDiscountAmount = 0
        val cashbackDiscountAmount = 0
        val cashbackMinimumPurchase = 0
        val cashbackQuota = 0

        val freeShippingDiscountAmount = 20_000
        val freeShippingMinimumPurchase = 200_000
        val freeShippingQuota = 5

        val estimatedMaxExpense : Long = 250_000

        //When
        viewModel.saveCoupon(
            couponType,
            discountType,
            minimumPurchaseType,
            cashbackPercentage,
            cashbackMaximumDiscountAmount,
            cashbackDiscountAmount,
            cashbackMinimumPurchase,
            cashbackQuota,
            freeShippingDiscountAmount,
            freeShippingMinimumPurchase,
            freeShippingQuota,
            estimatedMaxExpense
        )

        //Then
        assertEquals(expected, viewModel.saveCoupon.getOrAwaitValue())
    }

    @Test
    fun `When coupon type is cashback, discount type is nominal and minimal purchase is nothing, should save correct value`() {
        //Given
        val expected = CouponSettings(
            CouponType.CASHBACK,
            DiscountType.NOMINAL,
            MinimumPurchaseType.NOTHING,
            20_000, 
            0, 
            20_000,
            5,
            0,
            250_000
        )
        val couponType = CouponType.CASHBACK

        val discountType = DiscountType.NOMINAL
        val minimumPurchaseType = MinimumPurchaseType.NOTHING
        val cashbackPercentage = 0
        val cashbackMaximumDiscountAmount = 0
        val cashbackDiscountAmount = 20_000
        val cashbackMinimumPurchase = 200_000
        val cashbackQuota = 5

        val freeShippingDiscountAmount = 0
        val freeShippingMinimumPurchase = 0
        val freeShippingQuota = 0

        val estimatedMaxExpense : Long = 250_000

        //When
        viewModel.saveCoupon(
            couponType,
            discountType,
            minimumPurchaseType,
            cashbackPercentage,
            cashbackMaximumDiscountAmount,
            cashbackDiscountAmount,
            cashbackMinimumPurchase,
            cashbackQuota,
            freeShippingDiscountAmount,
            freeShippingMinimumPurchase,
            freeShippingQuota,
            estimatedMaxExpense
        )

        //Then
        assertEquals(expected, viewModel.saveCoupon.getOrAwaitValue())
    }

    @Test
    fun `When coupon type is cashback, and discount type is nominal, should save correct value`() {
        //Given
        val expected = CouponSettings(
            CouponType.CASHBACK,
            DiscountType.NOMINAL,
            MinimumPurchaseType.NOMINAL,
            20_000, 
            0, 
            20_000,
            5,
            200_000,
            250_000
        )
        val couponType = CouponType.CASHBACK

        val discountType = DiscountType.NOMINAL
        val minimumPurchaseType = MinimumPurchaseType.NOMINAL
        val cashbackPercentage = 0
        val cashbackMaximumDiscountAmount = 0
        val cashbackDiscountAmount = 20_000
        val cashbackMinimumPurchase = 200_000
        val cashbackQuota = 5

        val freeShippingDiscountAmount = 0
        val freeShippingMinimumPurchase = 0
        val freeShippingQuota = 0

        val estimatedMaxExpense : Long = 250_000

        //When
        viewModel.saveCoupon(
            couponType,
            discountType,
            minimumPurchaseType,
            cashbackPercentage,
            cashbackMaximumDiscountAmount,
            cashbackDiscountAmount,
            cashbackMinimumPurchase,
            cashbackQuota,
            freeShippingDiscountAmount,
            freeShippingMinimumPurchase,
            freeShippingQuota,
            estimatedMaxExpense
        )

        //Then
        assertEquals(expected, viewModel.saveCoupon.getOrAwaitValue())
    }

    @Test
    fun `When coupon type is cashback, and discount type is percentage, should save correct value`() {
        //Given
        val expected = CouponSettings(
            CouponType.CASHBACK,
            DiscountType.PERCENTAGE,
            MinimumPurchaseType.NOMINAL,
            1_000_000, 
            20, 
            1_000_000,
            5,
            200_000,
            250_000
        )
        val couponType = CouponType.CASHBACK

        val discountType = DiscountType.PERCENTAGE
        val minimumPurchaseType = MinimumPurchaseType.NOMINAL
        val cashbackPercentage = 20
        val cashbackMaximumDiscountAmount = 1_000_000
        val cashbackDiscountAmount = 20_000
        val cashbackMinimumPurchase = 200_000
        val cashbackQuota = 5

        val freeShippingDiscountAmount = 0
        val freeShippingMinimumPurchase = 0
        val freeShippingQuota = 0

        val estimatedMaxExpense : Long = 250_000

        //When
        viewModel.saveCoupon(
            couponType,
            discountType,
            minimumPurchaseType,
            cashbackPercentage,
            cashbackMaximumDiscountAmount,
            cashbackDiscountAmount,
            cashbackMinimumPurchase,
            cashbackQuota,
            freeShippingDiscountAmount,
            freeShippingMinimumPurchase,
            freeShippingQuota,
            estimatedMaxExpense
        )

        //Then
        assertEquals(expected, viewModel.saveCoupon.getOrAwaitValue())
    }

    @Test
    fun `When coupon type is cashback, and discount type is not set, should save correct value`() {
        //Given
        val expected = CouponSettings(
            CouponType.CASHBACK,
            DiscountType.NONE,
            MinimumPurchaseType.NOMINAL,
            20_000, 
            0, 
            20_000,
            5,
            200_000,
            250_000
        )
        val couponType = CouponType.CASHBACK

        val discountType = DiscountType.NONE
        val minimumPurchaseType = MinimumPurchaseType.NOMINAL
        val cashbackPercentage = 0
        val cashbackMaximumDiscountAmount = 1_000_000
        val cashbackDiscountAmount = 20_000
        val cashbackMinimumPurchase = 200_000
        val cashbackQuota = 5

        val freeShippingDiscountAmount = 0
        val freeShippingMinimumPurchase = 0
        val freeShippingQuota = 0

        val estimatedMaxExpense : Long = 250_000

        //When
        viewModel.saveCoupon(
            couponType,
            discountType,
            minimumPurchaseType,
            cashbackPercentage,
            cashbackMaximumDiscountAmount,
            cashbackDiscountAmount,
            cashbackMinimumPurchase,
            cashbackQuota,
            freeShippingDiscountAmount,
            freeShippingMinimumPurchase,
            freeShippingQuota,
            estimatedMaxExpense
        )

        //Then
        assertEquals(expected, viewModel.saveCoupon.getOrAwaitValue())
    }

}