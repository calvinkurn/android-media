package com.tokopedia.vouchercreation.create.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.vouchercreation.create.view.enums.PromotionTextField
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject
import javax.inject.Named

class FreeDeliveryVoucherCreateViewModel @Inject constructor(
        @Named("Main") dispatcher: CoroutineDispatcher
) : BaseViewModel(dispatcher) {

    companion object {
        private const val INITIAL_EXPENSE_ESTIMATION = 0
    }

    private val mExpensesExtimationLiveData = MutableLiveData<Int>()
    val expensesExtimationLiveData: LiveData<Int>
        get() = mExpensesExtimationLiveData

    private val mFreeDeliveryAmount = MutableLiveData<Int>()
    private val mMinimumPurchase = MutableLiveData<Int>()
    private val mVoucherQuota = MutableLiveData<Int>()

    fun addTextFieldValueToCalculation(value: Int?, type: PromotionTextField.FreeDelivery) {
        when(type) {
            PromotionTextField.FreeDelivery.Amount -> {
                mFreeDeliveryAmount.value = value
            }
            PromotionTextField.FreeDelivery.MinimumPurchase -> {
                mMinimumPurchase.value = value
            }
            PromotionTextField.FreeDelivery.VoucherQuota -> {
                mVoucherQuota.value = value
            }
        }
        if (checkIfCanCalculate()) {
            calculateExpenseEstimation()
        } else {
            mExpensesExtimationLiveData.value = INITIAL_EXPENSE_ESTIMATION
        }
    }

    private fun checkIfCanCalculate() =
            mFreeDeliveryAmount.value != null && mVoucherQuota.value != null

    private fun calculateExpenseEstimation() {
        mFreeDeliveryAmount.value?.let { amount ->
            mVoucherQuota.value?.let { quota ->
                mExpensesExtimationLiveData.value = amount * quota
            }
        }
    }

}