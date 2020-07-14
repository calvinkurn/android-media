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
import com.tokopedia.vouchercreation.create.view.uimodel.vouchertype.item.CashbackPercentageInfoUiModel
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
import org.mockito.ArgumentMatchers.anyInt

@ExperimentalCoroutinesApi
class CashbackVoucherCreateViewModelTest {

    companion object {
        private const val DUMMY_PERCENTAGE = 10
        private const val DUMMY_QUOTA = 10
        private const val DUMMY_MAX_VALUE = 10000
        private const val DUMMY_MIN_PURCHASE = 5000
        private const val DUMMY_ESTIMATION = DUMMY_QUOTA * DUMMY_MAX_VALUE
        private const val DUMMY_ERROR_MESSAGE = "error"
    }

    @RelaxedMockK
    lateinit var cashbackRupiahValidationUseCase: CashbackRupiahValidationUseCase

    @RelaxedMockK
    lateinit var cashbackPercentageValidationUseCase: CashbackPercentageValidationUseCase

    @RelaxedMockK
    lateinit var expenseEstimationObserver: Observer<in Int>

    @RelaxedMockK
    lateinit var cashbackPercentageInfoUiModelObserver: Observer<in CashbackPercentageInfoUiModel>

    @get:Rule
    val rule = InstantTaskExecutorRule()

    lateinit var mViewModel: CashbackVoucherCreateViewModel

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        mViewModel = CashbackVoucherCreateViewModel(testDispatcher, cashbackRupiahValidationUseCase, cashbackPercentageValidationUseCase)

        mViewModel.expenseEstimationLiveData.observeForever(expenseEstimationObserver)
        mViewModel.cashbackPercentageInfoUiModelLiveData.observeForever(cashbackPercentageInfoUiModelObserver)
    }

    @After
    fun cleanup() {
        mViewModel.expenseEstimationLiveData.removeObserver(expenseEstimationObserver)
        mViewModel.cashbackPercentageInfoUiModelLiveData.removeObserver(cashbackPercentageInfoUiModelObserver)

        testDispatcher.cleanupTestCoroutines()
    }

    private val testDispatcher = TestCoroutineDispatcher()

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

            coroutineContext[Job]?.children?.forEach { it.join() }

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

            coroutineContext[Job]?.children?.forEach { it.join() }

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

            coroutineContext[Job]?.children?.forEach { it.join() }

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

            coroutineContext[Job]?.children?.forEach { it.join() }

            val isSuccess = (voucherImageValueLiveData.value as? VoucherImageType.Percentage)?.run {
                value == DUMMY_MAX_VALUE && percentage == DUMMY_PERCENTAGE
            }

            assert(isSuccess ?: false)
        }
    }

    @Test
    fun `changing cashback type will change value list live data`() {
        with(mViewModel) {
            changeCashbackType(CashbackType.Rupiah)

            assert(rupiahValueList.value?.contentEquals(arrayOf(anyInt(), anyInt(), anyInt()))
                    ?: false)

            changeCashbackType(CashbackType.Percentage)

            assert(percentageValueList.value?.contentEquals(arrayOf(anyInt(), anyInt(), anyInt(), anyInt()))
                    ?: false)
        }
    }

    @Test
    fun `adding error pair will change error pair live data`() {
        with(mViewModel) {
            val dummyPair = Pair(true, DUMMY_ERROR_MESSAGE)
            addErrorPair(dummyPair.first, dummyPair.second, PromotionType.Cashback.Rupiah.MaximumDiscount)
            addErrorPair(dummyPair.first, dummyPair.second, PromotionType.Cashback.Rupiah.MinimumPurchase)
            addErrorPair(dummyPair.first, dummyPair.second, PromotionType.Cashback.Rupiah.VoucherQuota)
            addErrorPair(dummyPair.first, dummyPair.second, PromotionType.Cashback.Percentage.Amount)
            addErrorPair(dummyPair.first, dummyPair.second, PromotionType.Cashback.Percentage.MaximumDiscount)
            addErrorPair(dummyPair.first, dummyPair.second, PromotionType.Cashback.Percentage.MinimumPurchase)
            addErrorPair(dummyPair.first, dummyPair.second, PromotionType.Cashback.Percentage.VoucherQuota)

            changeCashbackType(CashbackType.Rupiah)

            assert(rupiahErrorPairList.value?.contentEquals(arrayOf(dummyPair, dummyPair, dummyPair))
                    ?: false)

            changeCashbackType(CashbackType.Percentage)

            assert(percentageErrorPairList.value?.contentEquals(arrayOf(dummyPair, dummyPair, dummyPair, dummyPair))
                    ?: false)
        }
    }

    @Test
    fun `changing cashback type will change its live data`() {
        with(mViewModel) {
            changeCashbackType(CashbackType.Percentage)

            assert(cashbackTypeData.value == CashbackType.Percentage)
        }
    }

    @Test
    fun `changing percentage maximum discount will change cashback percentage info ui model`() {
        with(mViewModel) {
            addTextFieldValueToCalculation(DUMMY_MAX_VALUE, PromotionType.Cashback.Percentage.MaximumDiscount)

            assert(cashbackPercentageInfoUiModelLiveData.value == CashbackPercentageInfoUiModel(
                    anyInt(),
                    anyInt(),
                    anyInt(),
                    DUMMY_MAX_VALUE
            ))
        }
    }

}