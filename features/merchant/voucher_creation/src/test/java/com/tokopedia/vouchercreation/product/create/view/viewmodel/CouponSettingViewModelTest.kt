package com.tokopedia.vouchercreation.product.create.view.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.vouchercreation.product.create.domain.entity.CouponType
import com.tokopedia.vouchercreation.product.create.domain.entity.DiscountType
import com.tokopedia.vouchercreation.product.create.domain.entity.MinimumPurchaseType
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.RelaxedMockK
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class CouponSettingViewModelTest {

    private val viewModel by lazy { CouponSettingViewModel(CoroutineTestDispatchersProvider) }

    companion object {
        private const val ZERO = 0
    }
    @get:Rule
    val rule = InstantTaskExecutorRule()

    @RelaxedMockK
    lateinit var validInputObserver: Observer<CouponSettingViewModel.ValidationResult>

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        viewModel.areInputValid.observeForever(validInputObserver)
    }

    @Test
    fun `When coupon type is not selected should return coupon type not selected state`()  {
        viewModel.validateInput(
            CouponType.NONE,
            DiscountType.NONE,
            MinimumPurchaseType.NONE,
            ZERO,
            ZERO,
            ZERO,
            ZERO,
            ZERO,
            ZERO
        )

        assert(viewModel.areInputValid.value is CouponSettingViewModel.ValidationResult.CouponTypeNotSelected)
    }

    @Test
    fun `When cashback discount type is not selected should return discount type not selected state`()  {
        viewModel.validateInput(
            CouponType.CASHBACK,
            DiscountType.NONE,
            MinimumPurchaseType.NONE,
            ZERO,
            ZERO,
            ZERO,
            ZERO,
            ZERO,
            ZERO
        )

        assert(viewModel.areInputValid.value is CouponSettingViewModel.ValidationResult.Cashback.DiscountTypeNotSelected)
    }

    @Test
    fun `When cashback minimum purchase is not selected should return invalid minimum purchase state`()  {
        viewModel.validateInput(
            CouponType.CASHBACK,
            DiscountType.NOMINAL,
            MinimumPurchaseType.NONE,
            ZERO,
            ZERO,
            ZERO,
            ZERO,
            ZERO,
            ZERO
        )

        assert(viewModel.areInputValid.value is CouponSettingViewModel.ValidationResult.Cashback.MinimumPurchaseTypeNotSelected)
    }

    @Test
    fun `When cashback amount below minimum should return invalid discount amount`()  {
        val discountAmount = 4000

        viewModel.validateInput(
            CouponType.CASHBACK,
            DiscountType.NOMINAL,
            MinimumPurchaseType.NOMINAL,
            discountAmount,
            ZERO,
            ZERO,
            ZERO,
            ZERO,
            ZERO
        )

        assert(viewModel.areInputValid.value is CouponSettingViewModel.ValidationResult.Cashback.InvalidDiscountAmount)
    }

    @Test
    fun `When cashback minimum purchase below cashback discount should return invalid minimum purchase `()  {
        val discountAmount = 100_000
        val minimumPurchase = 200

        viewModel.validateInput(
            CouponType.CASHBACK,
            DiscountType.NOMINAL,
            MinimumPurchaseType.NOMINAL,
            discountAmount,
            minimumPurchase,
            ZERO,
            ZERO,
            ZERO,
            ZERO
        )

        assert(viewModel.areInputValid.value is CouponSettingViewModel.ValidationResult.Cashback.InvalidMinimumPurchase)
    }


    @Test
    fun `When cashback quota is zero should return invalid quota`()  {
        val discountAmount = 100_000
        val minimumPurchase = 150_000
        val cashbackQuota = 0

        viewModel.validateInput(
            CouponType.CASHBACK,
            DiscountType.NOMINAL,
            MinimumPurchaseType.NOMINAL,
            discountAmount,
            minimumPurchase,
            cashbackQuota,
            ZERO,
            ZERO,
            ZERO
        )

        assert(viewModel.areInputValid.value is CouponSettingViewModel.ValidationResult.Cashback.InvalidQuota)
    }

    @Test
    fun `When all cashback coupon requirement satisfied should return valid cashback coupon`()  {
        val discountAmount = 100_000
        val minimumPurchase = 150_000
        val cashbackQuota = 20

        viewModel.validateInput(
            CouponType.CASHBACK,
            DiscountType.NOMINAL,
            MinimumPurchaseType.NOMINAL,
            discountAmount,
            minimumPurchase,
            cashbackQuota,
            ZERO,
            ZERO,
            ZERO
        )

        assert(viewModel.areInputValid.value is CouponSettingViewModel.ValidationResult.Cashback.Valid)
    }


    @Test
    fun `When free shipping discount under threshold should return invalid minimum purchase state`()  {
        val freeShippingDiscountAmount = 4_000

        viewModel.validateInput(
            CouponType.FREE_SHIPPING,
            DiscountType.NOMINAL,
            MinimumPurchaseType.NONE,
            ZERO,
            ZERO,
            ZERO,
            freeShippingDiscountAmount,
            ZERO,
            ZERO
        )

        assert(viewModel.areInputValid.value is CouponSettingViewModel.ValidationResult.FreeShipping.InvalidFreeShippingAmount)
    }

    @Test
    fun `When free shipping minimum purchase below free shipping discount should return invalid minimum purchase `()  {
        val freeShippingDiscount = 6_000
        val minimumPurchase = 2_000

        viewModel.validateInput(
            CouponType.FREE_SHIPPING,
            DiscountType.NOMINAL,
            MinimumPurchaseType.NOMINAL,
            ZERO,
            ZERO,
            ZERO,
            freeShippingDiscount,
            minimumPurchase,
            ZERO
        )

        assert(viewModel.areInputValid.value is CouponSettingViewModel.ValidationResult.FreeShipping.InvalidMinimumPurchase)
    }

    @Test
    fun `When free shipping quota is zero should return invalid quota`()  {
        val discountAmount = 100_000
        val minimumPurchase = 150_000
        val freeShippingQuota = 0

        viewModel.validateInput(
            CouponType.FREE_SHIPPING,
            DiscountType.NOMINAL,
            MinimumPurchaseType.NOMINAL,
            ZERO,
            ZERO,
            ZERO,
            discountAmount,
            minimumPurchase,
            freeShippingQuota
        )

        assert(viewModel.areInputValid.value is CouponSettingViewModel.ValidationResult.FreeShipping.InvalidQuota)
    }

    @Test
    fun `When all free shipping coupon requirement satisfied should return valid free shipping coupon`()  {
        val discountAmount = 100_000
        val minimumPurchase = 150_000
        val freeShippingQuota = 20

        viewModel.validateInput(
            CouponType.FREE_SHIPPING,
            DiscountType.NOMINAL,
            MinimumPurchaseType.NOMINAL,
            ZERO,
            ZERO,
            ZERO,
            discountAmount,
            minimumPurchase,
            freeShippingQuota,
        )

        assert(viewModel.areInputValid.value is CouponSettingViewModel.ValidationResult.FreeShipping.Valid)
    }
}