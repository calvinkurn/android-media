package com.tokopedia.vouchercreation.create.view.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.vouchercreation.create.domain.usecase.validation.FreeDeliveryValidationUseCase
import com.tokopedia.vouchercreation.create.view.enums.PromotionType
import com.tokopedia.vouchercreation.create.view.enums.VoucherImageType
import com.tokopedia.vouchercreation.create.view.uimodel.validation.FreeDeliveryValidation
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.ArgumentMatchers.anyInt

@ExperimentalCoroutinesApi
class FreeDeliveryVoucherCreateViewModelTest {

    companion object {
        private const val DUMMY_QUOTA = 10
        private const val DUMMY_AMOUNT = 10000
        private const val DUMMY_MIN_PURCHASE = 5000
        private const val DUMMY_ESTIMATION = DUMMY_QUOTA * DUMMY_AMOUNT
        private const val DUMMY_ERROR_MESSAGE = "error"
    }

    @RelaxedMockK
    lateinit var freeDeliveryValidationUseCase: FreeDeliveryValidationUseCase

    @RelaxedMockK
    lateinit var expenseEstimationObserver: Observer<in Int>

    lateinit var mViewModel: FreeDeliveryVoucherCreateViewModel

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        mViewModel = FreeDeliveryVoucherCreateViewModel(CoroutineTestDispatchersProvider, freeDeliveryValidationUseCase)
        mViewModel.expensesExtimationLiveData.observeForever(expenseEstimationObserver)
    }

    @After
    fun cleanup() {
        mViewModel.expensesExtimationLiveData.removeObserver(expenseEstimationObserver)
    }

    @Test
    fun `adding free delivery amount text field value to calculation will change image value`() {
        with(mViewModel) {
            addTextFieldValueToCalculation(DUMMY_AMOUNT, PromotionType.FreeDelivery.Amount)

            assert(voucherImageValueLiveData.value is VoucherImageType.FreeDelivery && voucherImageValueLiveData.value?.value == DUMMY_AMOUNT)
        }
    }

    @Test
    fun `refreshing text field value will change voucher image value if amount already set`() {
        with(mViewModel) {
            addTextFieldValueToCalculation(DUMMY_AMOUNT, PromotionType.FreeDelivery.Amount)

            refreshTextFieldValue()

            assert(voucherImageValueLiveData.value is VoucherImageType.FreeDelivery && voucherImageValueLiveData.value?.value == DUMMY_AMOUNT)
        }
    }

    @Test
    fun `refreshing text field value will change value list live data`() {
        with(mViewModel) {
            refreshTextFieldValue()

            assert(valueListLiveData.value?.contentEquals(arrayOf(anyInt(), anyInt(), anyInt())) ?: false)
        }
    }

    @Test
    fun `refreshing text field value if edit voucher will change vocuher voucher image value`() {
        with(mViewModel) {
            addTextFieldValueToCalculation(DUMMY_AMOUNT, PromotionType.FreeDelivery.Amount)

            refreshTextFieldValue(true)

            assert(voucherImageValueLiveData.value is VoucherImageType.FreeDelivery && voucherImageValueLiveData.value?.value == DUMMY_AMOUNT)
        }
    }

    @Test
    fun `refreshing text field will change error pair list`() {
        with(mViewModel) {
            val dummyPair = Pair(true, DUMMY_ERROR_MESSAGE)
            addErrorPair(dummyPair.first, dummyPair.second, PromotionType.FreeDelivery.Amount)
            addErrorPair(dummyPair.first, dummyPair.second, PromotionType.FreeDelivery.MinimumPurchase)
            addErrorPair(dummyPair.first, dummyPair.second, PromotionType.FreeDelivery.VoucherQuota)

            refreshTextFieldValue()

            assert(errorPairListLiveData.value?.all { it == dummyPair } ?: false)
        }
    }

    @Test
    fun `adding free delivery amount text field will change expense estimation if quota is set`() {
        with(mViewModel) {
            addTextFieldValueToCalculation(DUMMY_QUOTA, PromotionType.FreeDelivery.VoucherQuota)

            addTextFieldValueToCalculation(DUMMY_AMOUNT, PromotionType.FreeDelivery.Amount)

            assert(expensesExtimationLiveData.value == DUMMY_ESTIMATION)
        }
    }

    @Test
    fun `adding quota text field value will change expense estimation if amount is set`() {
        with(mViewModel) {
            addTextFieldValueToCalculation(DUMMY_AMOUNT, PromotionType.FreeDelivery.Amount)

            addTextFieldValueToCalculation(DUMMY_QUOTA, PromotionType.FreeDelivery.VoucherQuota)

            assert(expensesExtimationLiveData.value == DUMMY_ESTIMATION)
        }
    }

    @Test
    fun `success validating free delivery values`() = runBlocking {
        with(mViewModel) {
            val dummySuccessFreeDeliveryValidation = FreeDeliveryValidation()

            coEvery {
                freeDeliveryValidationUseCase.executeOnBackground()
            } returns dummySuccessFreeDeliveryValidation

            addTextFieldValueToCalculation(DUMMY_AMOUNT, PromotionType.FreeDelivery.Amount)
            addTextFieldValueToCalculation(DUMMY_MIN_PURCHASE, PromotionType.FreeDelivery.MinimumPurchase)
            addTextFieldValueToCalculation(DUMMY_QUOTA, PromotionType.FreeDelivery.VoucherQuota)

            validateFreeDeliveryValues()

            assert(freeDeliveryValidationLiveData.value == Success(dummySuccessFreeDeliveryValidation))
        }
    }

    @Test
    fun `fail validating free delivery values`() = runBlocking {
        with(mViewModel) {
            val dummyThrowable = MessageErrorException("")

            coEvery {
                freeDeliveryValidationUseCase.executeOnBackground()
            } throws dummyThrowable

            addTextFieldValueToCalculation(DUMMY_AMOUNT, PromotionType.FreeDelivery.Amount)
            addTextFieldValueToCalculation(DUMMY_MIN_PURCHASE, PromotionType.FreeDelivery.MinimumPurchase)
            addTextFieldValueToCalculation(DUMMY_QUOTA, PromotionType.FreeDelivery.VoucherQuota)

            validateFreeDeliveryValues()

            assert(freeDeliveryValidationLiveData.value is Fail)
        }
    }

}