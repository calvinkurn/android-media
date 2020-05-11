package com.tokopedia.vouchercreation.create.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.kotlin.extensions.view.toZeroIfNull
import com.tokopedia.vouchercreation.create.view.enums.PromotionType
import com.tokopedia.vouchercreation.create.view.enums.VoucherImageType
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject
import javax.inject.Named

class FreeDeliveryVoucherCreateViewModel @Inject constructor(
        @Named("Main") dispatcher: CoroutineDispatcher
) : BaseViewModel(dispatcher) {

    private val mFreeDeliveryAmountLiveData = MutableLiveData<Int>()
    private val mMinimumPurchaseLiveData = MutableLiveData<Int>()
    private val mVoucherQuotaLiveData = MutableLiveData<Int>()

    private val mFreeDeliveryAmountErrorPairLiveData = MutableLiveData<Pair<Boolean, String>>()
    private val mMinimumPurchaseErrorPairLiveData = MutableLiveData<Pair<Boolean, String>>()
    private val mVoucherQuotaErrorPairLiveData = MutableLiveData<Pair<Boolean, String>>()

    private val mValueListLiveData = MutableLiveData<Array<Int>>()
    val valueListLiveData : LiveData<Array<Int>>
        get() = mValueListLiveData
    private val mErrorPairListLiveData = MutableLiveData<Array<Pair<Boolean, String>?>>()
    val errorPairListLiveData : LiveData<Array<Pair<Boolean, String>?>>
        get() = mErrorPairListLiveData

    private val mIsFirstTimeDraw = MutableLiveData<Boolean>().apply {
        value = true
    }
    private val mVoucherImageValueLiveData = MutableLiveData<VoucherImageType>()
    val voucherImageValueLiveData: LiveData<VoucherImageType>
        get() = mVoucherImageValueLiveData

    private val mExpensesExtimationLiveData = MediatorLiveData<Int>().apply {
        addSource(mFreeDeliveryAmountLiveData) {
            calculateExpenseEstimation()
        }
        addSource(mVoucherQuotaLiveData) {
            calculateExpenseEstimation()
        }
    }
    val expensesExtimationLiveData: LiveData<Int>
        get() = mExpensesExtimationLiveData

    fun refreshTextFieldValue() {
        mIsFirstTimeDraw.value?.let { isFirstTimeDraw ->
            if (!isFirstTimeDraw) {
                mFreeDeliveryAmountLiveData.value?.let { amount ->
                    mVoucherImageValueLiveData.value = VoucherImageType.FreeDelivery(amount)
                }
            }
            mIsFirstTimeDraw.value = false
        }
        mValueListLiveData.value = arrayOf(
                mFreeDeliveryAmountLiveData.value.toZeroIfNull(),
                mMinimumPurchaseLiveData.value.toZeroIfNull(),
                mVoucherQuotaLiveData.value.toZeroIfNull()
        )
        mErrorPairListLiveData.value = arrayOf(
                mFreeDeliveryAmountErrorPairLiveData.value,
                mMinimumPurchaseErrorPairLiveData.value,
                mVoucherQuotaErrorPairLiveData.value
        )
    }

    fun<T> addTextFieldValueToCalculation(value: Int?, type: T) {
        when(type) {
            PromotionType.FreeDelivery.Amount -> {
                mFreeDeliveryAmountLiveData.value = value.toZeroIfNull()
                value?.let { amount ->
                    mVoucherImageValueLiveData.value = VoucherImageType.FreeDelivery(amount)
                }
            }
            PromotionType.FreeDelivery.MinimumPurchase -> {
                mMinimumPurchaseLiveData.value = value.toZeroIfNull()
            }
            PromotionType.FreeDelivery.VoucherQuota -> {
                mVoucherQuotaLiveData.value = value.toZeroIfNull()
            }
        }
    }

    fun addErrorPair(isError: Boolean, errorMessage: String, type: PromotionType.FreeDelivery) {
        when(type) {
            PromotionType.FreeDelivery.Amount -> {
                mFreeDeliveryAmountErrorPairLiveData.value = Pair(isError, errorMessage)
            }
            PromotionType.FreeDelivery.MinimumPurchase -> {
                mMinimumPurchaseErrorPairLiveData.value = Pair(isError, errorMessage)
            }
            PromotionType.FreeDelivery.VoucherQuota -> {
                mVoucherQuotaErrorPairLiveData.value = Pair(isError, errorMessage)
            }
        }
    }

    private fun calculateExpenseEstimation() {
        mFreeDeliveryAmountLiveData.value?.let { amount ->
            mVoucherQuotaLiveData.value?.let { quota ->
                mExpensesExtimationLiveData.value = amount * quota
            }
        }
    }

}