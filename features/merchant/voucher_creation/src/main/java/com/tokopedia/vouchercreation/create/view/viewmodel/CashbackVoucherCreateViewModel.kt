package com.tokopedia.vouchercreation.create.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.kotlin.extensions.view.toZeroIfNull
import com.tokopedia.utils.text.currency.CurrencyFormatHelper
import com.tokopedia.vouchercreation.create.view.enums.CashbackType
import com.tokopedia.vouchercreation.create.view.enums.PromotionType
import com.tokopedia.vouchercreation.create.view.enums.VoucherImageType
import com.tokopedia.vouchercreation.create.view.uimodel.vouchertype.item.CashbackPercentageInfoUiModel
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject
import javax.inject.Named

class CashbackVoucherCreateViewModel @Inject constructor(
        @Named("Main") dispatcher: CoroutineDispatcher
) : BaseViewModel(dispatcher) {

    private val mActiveCashbackPromoTypeLiveData = MutableLiveData<CashbackType>().apply {
        value = CashbackType.Rupiah
    }
    private val mRupiahMaximumDiscountLiveData = MutableLiveData<Int>()
    private val mRupiahMinimumPurchaseLiveData = MutableLiveData<Int>()
    private val mRupiahVoucherQuotaLiveData = MutableLiveData<Int>()

    private val mPercentageDiscountAmountLiveData = MutableLiveData<Int>()
    private val mPercentageMaximumDiscountLiveData = MutableLiveData<Int>()
    private val mPercentageMinimumPurchaseLiveData = MutableLiveData<Int>()
    private val mPercentageVoucherQuotaLiveData = MutableLiveData<Int>()

    private val mRupiahMaximumDiscountErrorPairLiveData = MutableLiveData<Pair<Boolean,String>>()
    private val mRupiahMinimumPurchaseErrorPairLiveData = MutableLiveData<Pair<Boolean,String>>()
    private val mRupiahVoucherQuotaErrorPairLiveData = MutableLiveData<Pair<Boolean,String>>()

    private val mPercentageDiscountAmountErrorPairLiveData = MutableLiveData<Pair<Boolean,String>>()
    private val mPercentageMaximumDiscountErrorPairLiveData = MutableLiveData<Pair<Boolean,String>>()
    private val mPercentageMinimumPurchaseErrorPairLiveData = MutableLiveData<Pair<Boolean,String>>()
    private val mPercentageVoucherQuotaErrorPairLiveData = MutableLiveData<Pair<Boolean,String>>()

    private val mRupiahValueList = MutableLiveData<Array<Int>>()
    val rupiahValueList : LiveData<Array<Int>>
        get() = mRupiahValueList
    private val mPercentageValueList = MutableLiveData<Array<Int>>()
    val percentageValueList : LiveData<Array<Int>>
        get() = mPercentageValueList

    private val mRupiahErrorPairList = MutableLiveData<Array<Pair<Boolean, String>?>>()
    val rupiahErrorPairList : LiveData<Array<Pair<Boolean, String>?>>
        get() = mRupiahErrorPairList
    private val mPercentageErrorPairList = MutableLiveData<Array<Pair<Boolean, String>?>>()
    val percentageErrorPairList : LiveData<Array<Pair<Boolean, String>?>>
        get() = mPercentageErrorPairList

    private val mCashbackTypeLiveData = MutableLiveData<CashbackType>()
    val cashbackTypeData: LiveData<CashbackType>
        get() = mCashbackTypeLiveData

    private val mExpenseEstimationLiveData: LiveData<Int> = MediatorLiveData<Int>().apply {
        addSource(mActiveCashbackPromoTypeLiveData) { cashbackType ->
            value = when(cashbackType) {
                CashbackType.Rupiah -> {
                    mRupiahMaximumDiscountLiveData.value.toZeroIfNull() * mRupiahVoucherQuotaLiveData.value.toZeroIfNull()
                }
                CashbackType.Percentage -> {
                    mPercentageMaximumDiscountLiveData.value.toZeroIfNull() * mPercentageVoucherQuotaLiveData.value.toZeroIfNull()
                }
            }
        }
        addSource(mRupiahMaximumDiscountLiveData) { maxDiscount ->
            mRupiahVoucherQuotaLiveData.value?.let { quota ->
                value = maxDiscount * quota
            }
        }
        addSource(mRupiahVoucherQuotaLiveData) { quota ->
            mRupiahMaximumDiscountLiveData.value?.let { maxDiscount ->
                value = maxDiscount * quota
            }
        }
        addSource(mPercentageMaximumDiscountLiveData) { maxDiscount ->
            mPercentageVoucherQuotaLiveData.value?.let { quota ->
                value = maxDiscount * quota
            }
        }
        addSource(mPercentageVoucherQuotaLiveData) { quota ->
            mPercentageMaximumDiscountLiveData.value?.let { maxDiscount ->
                value = maxDiscount * quota
            }
        }
    }
    val expenseEstimationLiveData: LiveData<Int>
        get() = mExpenseEstimationLiveData

    private val mTresholdValueLiveData = MediatorLiveData<Int>().apply {
        addSource(mPercentageMinimumPurchaseLiveData) { minimumPurchase ->
            mPercentageDiscountAmountLiveData.value?.let { percentage ->
                val percentValue = minimumPurchase.toDouble() * percentage / 100
                value = percentValue.toInt()
            }
        }
        addSource(mPercentageDiscountAmountLiveData) { percentage ->
            mPercentageMinimumPurchaseLiveData.value?.let { minimumPurchase ->
                val percentValue = minimumPurchase.toDouble() * percentage / 100
                value = percentValue.toInt()
            }
        }
    }

    private val mCashbackPercentageInfoUiModelLiveData = MediatorLiveData<CashbackPercentageInfoUiModel>().apply {
        addSource(mTresholdValueLiveData) {
            value = CashbackPercentageInfoUiModel(
                    mPercentageMinimumPurchaseLiveData.value.toZeroIfNull(),
                    mPercentageDiscountAmountLiveData.value.toZeroIfNull(),
                    mTresholdValueLiveData.value.toZeroIfNull())
        }
    }
    val cashbackPercentageInfoUiModelLiveData : LiveData<CashbackPercentageInfoUiModel>
        get() = mCashbackPercentageInfoUiModelLiveData

    private val mVoucherImageValueLiveData = MediatorLiveData<VoucherImageType>().apply {
        addSource(mRupiahMaximumDiscountLiveData) { amount ->
            value = VoucherImageType.Rupiah(amount)
        }
        addSource(mPercentageDiscountAmountLiveData) { percentage ->
            mPercentageMaximumDiscountLiveData.value?.let { amount ->
                value = VoucherImageType.Percentage(amount, percentage)
            }
        }
        addSource(mPercentageMaximumDiscountLiveData) { amount ->
            mPercentageDiscountAmountLiveData.value?.let { percentage ->
                value = VoucherImageType.Percentage(amount, percentage)
            }
        }
        addSource(mActiveCashbackPromoTypeLiveData) { type ->
            when(type) {
                CashbackType.Rupiah -> {
                    mRupiahMaximumDiscountLiveData.value?.let { amount ->
                        value = VoucherImageType.Rupiah(amount)
                    }
                }
                CashbackType.Percentage -> {
                    mPercentageDiscountAmountLiveData.value?.let { percentage ->
                        mPercentageMaximumDiscountLiveData.value?.let { amount ->
                            value = VoucherImageType.Percentage(amount, percentage)
                        }
                    }
                }
            }
        }
    }
    val voucherImageValueLiveData: LiveData<VoucherImageType>
        get() = mVoucherImageValueLiveData

    fun<T> addTextFieldValueToCalculation(value: Int?, type: T) {
        when(type) {
            PromotionType.Cashback.Rupiah.MaximumDiscount -> {
                mRupiahMaximumDiscountLiveData.value = value.toZeroIfNull()
            }
            PromotionType.Cashback.Rupiah.MinimumPurchase -> {
                mRupiahMinimumPurchaseLiveData.value = value.toZeroIfNull()
            }
            PromotionType.Cashback.Rupiah.VoucherQuota -> {
                mRupiahVoucherQuotaLiveData.value = value.toZeroIfNull()
            }
            PromotionType.Cashback.Percentage.Amount -> {
                mPercentageDiscountAmountLiveData.value = value.toZeroIfNull()
            }
            PromotionType.Cashback.Percentage.MaximumDiscount -> {
                mPercentageMaximumDiscountLiveData.value = value.toZeroIfNull()
            }
            PromotionType.Cashback.Percentage.MinimumPurchase -> {
                mPercentageMinimumPurchaseLiveData.value = value.toZeroIfNull()
            }
            PromotionType.Cashback.Percentage.VoucherQuota -> {
                mPercentageVoucherQuotaLiveData. value = value.toZeroIfNull()
            }
        }
    }

    fun refreshImageType() {
        mActiveCashbackPromoTypeLiveData.value?.let { type ->
            when(type) {
                CashbackType.Rupiah -> {
                    mRupiahMaximumDiscountLiveData.value?.let { amount ->
                        mVoucherImageValueLiveData.value = VoucherImageType.Rupiah(amount)
                    }
                }
                CashbackType.Percentage -> {
                    mPercentageDiscountAmountLiveData.value?.let { percentage ->
                        mPercentageMaximumDiscountLiveData.value?.let { amount ->
                            mVoucherImageValueLiveData.value = VoucherImageType.Percentage(amount, percentage)
                        }
                    }
                }
            }
        }
    }

    fun addErrorPair(isError: Boolean, errorMessage: String, type: PromotionType.Cashback) {
        when(type) {
            PromotionType.Cashback.Rupiah.MaximumDiscount -> {
                mRupiahMaximumDiscountErrorPairLiveData.value = Pair(isError, errorMessage)
            }
            PromotionType.Cashback.Rupiah.MinimumPurchase -> {
                mRupiahMinimumPurchaseErrorPairLiveData.value = Pair(isError, errorMessage)
            }
            PromotionType.Cashback.Rupiah.VoucherQuota -> {
                mRupiahVoucherQuotaErrorPairLiveData.value = Pair(isError, errorMessage)
            }
            PromotionType.Cashback.Percentage.Amount -> {
                mPercentageDiscountAmountErrorPairLiveData.value = Pair(isError, errorMessage)
            }
            PromotionType.Cashback.Percentage.MaximumDiscount -> {
                mPercentageMaximumDiscountErrorPairLiveData.value = Pair(isError, errorMessage)
            }
            PromotionType.Cashback.Percentage.MinimumPurchase -> {
                mPercentageMinimumPurchaseErrorPairLiveData.value = Pair(isError, errorMessage)
            }
            PromotionType.Cashback.Percentage.VoucherQuota -> {
                mPercentageVoucherQuotaErrorPairLiveData. value = Pair(isError, errorMessage)
            }
        }
    }

    fun changeCashbackType(cashbackType: CashbackType) {
        mCashbackTypeLiveData.value = cashbackType
        when(cashbackType) {
            CashbackType.Rupiah -> {
                mRupiahValueList.value = arrayOf(
                        mRupiahMaximumDiscountLiveData.value.toZeroIfNull(),
                        mRupiahMinimumPurchaseLiveData.value.toZeroIfNull(),
                        mRupiahVoucherQuotaLiveData.value.toZeroIfNull()
                )
                mRupiahErrorPairList.value = arrayOf(
                        mRupiahMaximumDiscountErrorPairLiveData.value,
                        mRupiahMinimumPurchaseErrorPairLiveData.value,
                        mRupiahVoucherQuotaErrorPairLiveData.value
                )
            }
            CashbackType.Percentage -> {
                mPercentageValueList.value = arrayOf(
                        mPercentageDiscountAmountLiveData.value.toZeroIfNull(),
                        mPercentageMaximumDiscountLiveData.value.toZeroIfNull(),
                        mPercentageMinimumPurchaseLiveData.value.toZeroIfNull(),
                        mPercentageVoucherQuotaLiveData.value.toZeroIfNull()
                )
                mPercentageErrorPairList.value = arrayOf(
                        mPercentageDiscountAmountErrorPairLiveData.value,
                        mPercentageMaximumDiscountErrorPairLiveData.value,
                        mPercentageMinimumPurchaseErrorPairLiveData.value,
                        mPercentageVoucherQuotaErrorPairLiveData.value
                )
            }
        }
        mActiveCashbackPromoTypeLiveData.value = cashbackType
    }

    fun checkRupiahMinimumPurchase(currentValue: Int, errorMessage: String) : Pair<Boolean, String> {
        mRupiahMaximumDiscountLiveData.value?.let { threshold ->
            return Pair(currentValue >= threshold, errorMessage)
        }
        return Pair(true, "")
    }

    fun checkPercentageMaximumDiscount(currentValue: Int, errorMessage: String) : Pair<Boolean, String> {
        mTresholdValueLiveData.value?.let { thresholdValue ->
            val fullErrorMessage = "$errorMessage${CurrencyFormatHelper.convertToRupiah(thresholdValue.toString())}"
            return Pair(currentValue > thresholdValue, fullErrorMessage)
        }
        return Pair(true, "")
    }

}