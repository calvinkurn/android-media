package com.tokopedia.vouchercreation.create.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.kotlin.extensions.view.toZeroIfNull
import com.tokopedia.vouchercreation.create.view.enums.PromotionType
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject
import javax.inject.Named

class FreeDeliveryVoucherCreateViewModel @Inject constructor(
        @Named("Main") dispatcher: CoroutineDispatcher
) : BaseViewModel(dispatcher) {

    private val mFreeDeliveryAmount = MutableLiveData<Int>()
    private val mMinimumPurchase = MutableLiveData<Int>()
    private val mVoucherQuota = MutableLiveData<Int>()

    private val mExpensesExtimationLiveData = MediatorLiveData<Int>().apply {
        addSource(mFreeDeliveryAmount) {
            calculateExpenseEstimation()
        }
        addSource(mVoucherQuota) {
            calculateExpenseEstimation()
        }
    }
    val expensesExtimationLiveData: LiveData<Int>
        get() = mExpensesExtimationLiveData

    fun addTextFieldValueToCalculation(value: Int?, type: PromotionType.FreeDelivery) {
        when(type) {
            PromotionType.FreeDelivery.Amount -> {
                mFreeDeliveryAmount.value = value.toZeroIfNull()
            }
            PromotionType.FreeDelivery.MinimumPurchase -> {
                mMinimumPurchase.value = value.toZeroIfNull()
            }
            PromotionType.FreeDelivery.VoucherQuota -> {
                mVoucherQuota.value = value.toZeroIfNull()
            }
        }
    }

    private fun calculateExpenseEstimation() {
        mFreeDeliveryAmount.value?.let { amount ->
            mVoucherQuota.value?.let { quota ->
                mExpensesExtimationLiveData.value = amount * quota
            }
        }
    }

}