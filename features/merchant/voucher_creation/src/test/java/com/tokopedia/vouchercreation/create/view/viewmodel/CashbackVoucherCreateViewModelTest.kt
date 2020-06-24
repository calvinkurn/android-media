package com.tokopedia.vouchercreation.create.view.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.vouchercreation.create.domain.usecase.validation.CashbackPercentageValidationUseCase
import com.tokopedia.vouchercreation.create.domain.usecase.validation.CashbackRupiahValidationUseCase
import com.tokopedia.vouchercreation.create.view.enums.CashbackType
import com.tokopedia.vouchercreation.create.view.enums.PromotionType
import com.tokopedia.vouchercreation.create.view.enums.VoucherImageType
import com.tokopedia.vouchercreation.create.view.uimodel.validation.CashbackPercentageValidation
import com.tokopedia.vouchercreation.create.view.uimodel.validation.CashbackRupiahValidation
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.TestCoroutineDispatcher
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class CashbackVoucherCreateViewModelTest {

    companion object {
        private const val DUMMY_PERCENTAGE = 10
        private const val DUMMY_QUOTA = 10
        private const val DUMMY_MAX_VALUE = 10000
        private const val DUMMY_MIN_PURCHASE = 5000
        private const val DUMMY_ESTIMATION = DUMMY_QUOTA * DUMMY_MAX_VALUE
    }

    @RelaxedMockK
    lateinit var cashbackRupiahValidationUseCase: CashbackRupiahValidationUseCase

    @RelaxedMockK
    lateinit var cashbackPercentageValidationUseCase: CashbackPercentageValidationUseCase

    @RelaxedMockK
    lateinit var expenseEstimationObserver: Observer<in Int>

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @Before
    fun setup() {
        MockKAnnotations.init(this)

        mViewModel.expenseEstimationLiveData.observeForever(expenseEstimationObserver)
    }

    @After
    fun cleanup() {
        mViewModel.expenseEstimationLiveData.removeObserver(expenseEstimationObserver)
        testDispatcher.cleanupTestCoroutines()
    }

    private val testDispatcher = TestCoroutineDispatcher()

    private val mViewModel by lazy {
        CashbackVoucherCreateViewModel(testDispatcher, cashbackRupiahValidationUseCase, cashbackPercentageValidationUseCase)
    }

    @Test
    fun `adding rupiah maximum discount text field value will change expense estimation value if quota is already set`() {
        with(mViewModel) {
            addTextFieldValueToCalculation(DUMMY_QUOTA, PromotionType.Cashback.Rupiah.VoucherQuota)
            addTextFieldValueToCalculation(DUMMY_MAX_VALUE, PromotionType.Cashback.Rupiah.MaximumDiscount)

            assert(expenseEstimationLiveData.value == DUMMY_ESTIMATION)
        }
    }

    @Test
    fun `adding rupiah maximum discount text field value wont change expense estimation value if quota hasn't been set`() {
        with(mViewModel) {
            val initialValue = expenseEstimationLiveData.value
            addTextFieldValueToCalculation(DUMMY_MAX_VALUE, PromotionType.Cashback.Rupiah.MaximumDiscount)

            assert(expenseEstimationLiveData.value == initialValue)
        }
    }

    @Test
    fun `adding rupiah voucher quota text field value will change expense estimation value if maximum discount is already set`() {
        with(mViewModel) {
            addTextFieldValueToCalculation(DUMMY_MAX_VALUE, PromotionType.Cashback.Rupiah.MaximumDiscount)
            addTextFieldValueToCalculation(DUMMY_QUOTA, PromotionType.Cashback.Rupiah.VoucherQuota)

            assert(expenseEstimationLiveData.value == DUMMY_ESTIMATION)
        }
    }

    @Test
    fun `adding rupiah voucher quota text field value wont change expense estimation value if maximum discount hasn't been set`() {
        with(mViewModel) {
            val initialValue = expenseEstimationLiveData.value
            addTextFieldValueToCalculation(DUMMY_QUOTA, PromotionType.Cashback.Rupiah.VoucherQuota)

            assert(expenseEstimationLiveData.value == initialValue)
        }
    }

    @Test
    fun `adding percentage maximum discount text field value will change expense estimation value if quota is already set`() {
        with(mViewModel) {
            addTextFieldValueToCalculation(DUMMY_QUOTA, PromotionType.Cashback.Percentage.VoucherQuota)
            addTextFieldValueToCalculation(DUMMY_MAX_VALUE, PromotionType.Cashback.Percentage.MaximumDiscount)

            assert(expenseEstimationLiveData.value == DUMMY_ESTIMATION)
        }
    }

    @Test
    fun `adding percentage maximum discount text field value wont change expense estimation value if quota hasn't been set`() {
        with(mViewModel) {
            val initialValue = expenseEstimationLiveData.value
            addTextFieldValueToCalculation(DUMMY_MAX_VALUE, PromotionType.Cashback.Percentage.MaximumDiscount)

            assert(expenseEstimationLiveData.value == initialValue)
        }
    }

    @Test
    fun `adding percentage voucher quota text field value will change expense estimation value if maximum discount is already set`() {
        with(mViewModel) {
            addTextFieldValueToCalculation(DUMMY_MAX_VALUE, PromotionType.Cashback.Percentage.MaximumDiscount)
            addTextFieldValueToCalculation(DUMMY_QUOTA, PromotionType.Cashback.Percentage.VoucherQuota)

            assert(expenseEstimationLiveData.value == DUMMY_ESTIMATION)
        }
    }

    @Test
    fun `adding percentage voucher quota text field value wont change expense estimation value if maximum discount hasn't been set`() {
        with(mViewModel) {
            val initialValue = expenseEstimationLiveData.value
            addTextFieldValueToCalculation(DUMMY_QUOTA, PromotionType.Cashback.Percentage.VoucherQuota)

            assert(expenseEstimationLiveData.value == initialValue)
        }
    }

    @Test
    fun `setting cashback type to Rupiah will change expense estimation value if required values are provided`() {
        with(mViewModel) {
            addTextFieldValueToCalculation(DUMMY_QUOTA, PromotionType.Cashback.Rupiah.VoucherQuota)
            addTextFieldValueToCalculation(DUMMY_MAX_VALUE, PromotionType.Cashback.Rupiah.MaximumDiscount)
            changeCashbackType(CashbackType.Rupiah)

            assert(expenseEstimationLiveData.value == DUMMY_ESTIMATION)
        }
    }

    @Test
    fun `setting cashback type to Percentage will change expense estimation value if required values are provided`() {
        with(mViewModel) {
            addTextFieldValueToCalculation(DUMMY_QUOTA, PromotionType.Cashback.Percentage.VoucherQuota)
            addTextFieldValueToCalculation(DUMMY_MAX_VALUE, PromotionType.Cashback.Percentage.MaximumDiscount)
            changeCashbackType(CashbackType.Percentage)

            assert(expenseEstimationLiveData.value == DUMMY_ESTIMATION)
        }
    }

    @Test
    fun `setting cashback type to Rupiah without provided value will change expense estimation value to zero`() {
        with(mViewModel) {
            changeCashbackType(CashbackType.Rupiah)

            assert(expenseEstimationLiveData.value == 0)
        }
    }

    @Test
    fun `setting cashback type to Percentage without provided value will change expense estimation value to zero`() {
        with(mViewModel) {
            changeCashbackType(CashbackType.Percentage)

            assert(expenseEstimationLiveData.value == 0)
        }
    }

    @Test
    fun `success validating cashback rupiah value`() = runBlocking {
        val successResponse = CashbackRupiahValidation()

        coEvery {
            cashbackRupiahValidationUseCase.executeOnBackground()
        } returns successResponse

        with(mViewModel) {
            addTextFieldValueToCalculation(DUMMY_MAX_VALUE, PromotionType.Cashback.Rupiah.MaximumDiscount)
            addTextFieldValueToCalculation(DUMMY_MIN_PURCHASE, PromotionType.Cashback.Rupiah.MinimumPurchase)
            addTextFieldValueToCalculation(DUMMY_QUOTA, PromotionType.Cashback.Rupiah.VoucherQuota)

            validateCashbackRupiahValues()

            coVerify {
                cashbackRupiahValidationUseCase.executeOnBackground()
            }

            assert(rupiahValidationLiveData.value == Success(successResponse))
        }
    }

    @Test
    fun `fail validating cashback rupiah value`() = runBlocking {
        val throwable = MessageErrorException("")

        coEvery {
            cashbackRupiahValidationUseCase.executeOnBackground()
        } throws throwable

        with(mViewModel) {
            addTextFieldValueToCalculation(DUMMY_MAX_VALUE, PromotionType.Cashback.Rupiah.MaximumDiscount)
            addTextFieldValueToCalculation(DUMMY_MIN_PURCHASE, PromotionType.Cashback.Rupiah.MinimumPurchase)
            addTextFieldValueToCalculation(DUMMY_QUOTA, PromotionType.Cashback.Rupiah.VoucherQuota)

            validateCashbackRupiahValues()

            coroutineContext[Job]?.children?.forEach { it.join() }

            coVerify {
                cashbackRupiahValidationUseCase.executeOnBackground()
            }

            assert(rupiahValidationLiveData.value is Fail)
        }
    }

    @Test
    fun `success validating cashback percentage value`() = runBlocking {
        val successResponse = CashbackPercentageValidation()

        coEvery {
            cashbackPercentageValidationUseCase.executeOnBackground()
        } returns successResponse

        with(mViewModel) {
            addTextFieldValueToCalculation(DUMMY_PERCENTAGE, PromotionType.Cashback.Percentage.Amount)
            addTextFieldValueToCalculation(DUMMY_MAX_VALUE, PromotionType.Cashback.Percentage.MaximumDiscount)
            addTextFieldValueToCalculation(DUMMY_MIN_PURCHASE, PromotionType.Cashback.Percentage.MinimumPurchase)
            addTextFieldValueToCalculation(DUMMY_QUOTA, PromotionType.Cashback.Percentage.VoucherQuota)

            validateCashbackPercentageValues()

            coVerify {
                cashbackPercentageValidationUseCase.executeOnBackground()
            }

            assert(percentageValidationLiveData.value == Success(successResponse))
        }
    }

    @Test
    fun `fail validating cashback percentage value`() = runBlocking {
        val throwable = MessageErrorException("")

        coEvery {
            cashbackPercentageValidationUseCase.executeOnBackground()
        } throws throwable

        with(mViewModel) {
            addTextFieldValueToCalculation(DUMMY_PERCENTAGE, PromotionType.Cashback.Percentage.Amount)
            addTextFieldValueToCalculation(DUMMY_MAX_VALUE, PromotionType.Cashback.Percentage.MaximumDiscount)
            addTextFieldValueToCalculation(DUMMY_MIN_PURCHASE, PromotionType.Cashback.Percentage.MinimumPurchase)
            addTextFieldValueToCalculation(DUMMY_QUOTA, PromotionType.Cashback.Percentage.VoucherQuota)

            validateCashbackPercentageValues()

            coroutineContext[Job]?.children?.forEach { it.join() }

            coVerify {
                cashbackPercentageValidationUseCase.executeOnBackground()
            }

            assert(percentageValidationLiveData.value is Fail)
        }
    }

    @Test
    fun `refreshing value twice without initially changing cashback type will change voucher image value to rupiah`() = runBlocking {
        with(mViewModel) {
            addTextFieldValueToCalculation(DUMMY_MAX_VALUE, PromotionType.Cashback.Rupiah.MaximumDiscount)
            refreshValue()
            refreshValue()

            assert((voucherImageValueLiveData.value as? VoucherImageType.Rupiah)?.value == DUMMY_MAX_VALUE)
        }
    }

    @Test
    fun `refreshing value twice with changing cashback type will change voucher image value to percentage`() = runBlocking {
        with(mViewModel) {
            changeCashbackType(CashbackType.Percentage)
            addTextFieldValueToCalculation(DUMMY_PERCENTAGE, PromotionType.Cashback.Percentage.Amount)
            addTextFieldValueToCalculation(DUMMY_MAX_VALUE, PromotionType.Cashback.Percentage.MaximumDiscount)
            refreshValue()
            refreshValue()

            val isSuccess = (voucherImageValueLiveData.value as? VoucherImageType.Percentage)?.run {
                value == DUMMY_MAX_VALUE && percentage == DUMMY_PERCENTAGE
            }

            assert(isSuccess ?: false)
        }
    }


}