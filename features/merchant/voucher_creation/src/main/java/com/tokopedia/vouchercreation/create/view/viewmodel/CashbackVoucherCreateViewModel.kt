package com.tokopedia.vouchercreation.create.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.kotlin.extensions.view.toZeroIfNull
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.vouchercreation.common.consts.VoucherDiscountTypeConst
import com.tokopedia.vouchercreation.common.consts.VoucherRecommendationStatus.Companion.EDITED_RECOMMENDATION
import com.tokopedia.vouchercreation.common.consts.VoucherRecommendationStatus.Companion.NO_RECOMMENDATION
import com.tokopedia.vouchercreation.common.consts.VoucherRecommendationStatus.Companion.WITH_RECOMMENDATION
import com.tokopedia.vouchercreation.common.consts.VoucherTypeConst
import com.tokopedia.vouchercreation.create.data.source.PromotionTypeUiListStaticDataSource.InitialValue
import com.tokopedia.vouchercreation.create.domain.model.VoucherRecommendationData
import com.tokopedia.vouchercreation.create.domain.usecase.GetVoucherRecommendationUseCase
import com.tokopedia.vouchercreation.create.domain.usecase.validation.CashbackPercentageValidationUseCase
import com.tokopedia.vouchercreation.create.domain.usecase.validation.CashbackRupiahValidationUseCase
import com.tokopedia.vouchercreation.create.view.enums.CashbackType
import com.tokopedia.vouchercreation.create.view.enums.PromotionType
import com.tokopedia.vouchercreation.create.view.enums.VoucherImageType
import com.tokopedia.vouchercreation.create.view.uimodel.validation.CashbackPercentageValidation
import com.tokopedia.vouchercreation.create.view.uimodel.validation.CashbackRupiahValidation
import com.tokopedia.vouchercreation.create.view.uimodel.vouchertype.item.CashbackPercentageInfoUiModel
import kotlinx.coroutines.withContext
import javax.inject.Inject

class CashbackVoucherCreateViewModel @Inject constructor(
        private val dispatchers: CoroutineDispatchers,
        private val cashbackRupiahValidationUseCase: CashbackRupiahValidationUseCase,
        private val cashbackPercentageValidationUseCase: CashbackPercentageValidationUseCase,
        private val getVoucherRecommendationUseCase: GetVoucherRecommendationUseCase
) : BaseViewModel(dispatchers.main) {

    var isRupiahInputError = false
    var isPercentageInputError = false

    private val mRupiahMaximumDiscountLiveData = MutableLiveData<Int>()
    private val mRupiahMinimumPurchaseLiveData = MutableLiveData<Int>()
    private val mRupiahVoucherQuotaLiveData = MutableLiveData<Int>()

    private val mPercentageDiscountAmountLiveData = MutableLiveData<Int>()
    private val mPercentageMaximumDiscountLiveData = MutableLiveData<Int>()
    private val mPercentageMinimumPurchaseLiveData = MutableLiveData<Int>()
    private val mPercentageVoucherQuotaLiveData = MutableLiveData<Int>()

    private val mRupiahMaximumDiscountErrorPairLiveData = MutableLiveData<Pair<Boolean, String>>()
    private val mRupiahMinimumPurchaseErrorPairLiveData = MutableLiveData<Pair<Boolean, String>>()
    private val mRupiahVoucherQuotaErrorPairLiveData = MutableLiveData<Pair<Boolean, String>>()

    private val mPercentageDiscountAmountErrorPairLiveData = MutableLiveData<Pair<Boolean, String>>()
    private val mPercentageMaximumDiscountErrorPairLiveData = MutableLiveData<Pair<Boolean, String>>()
    private val mPercentageMinimumPurchaseErrorPairLiveData = MutableLiveData<Pair<Boolean, String>>()
    private val mPercentageVoucherQuotaErrorPairLiveData = MutableLiveData<Pair<Boolean, String>>()

    private val mRupiahValueList = MutableLiveData<Array<Int>>()
    val rupiahValueList: LiveData<Array<Int>>
        get() = mRupiahValueList

    private val mPercentageValueList = MutableLiveData<Array<Int>>()
    val percentageValueList: LiveData<Array<Int>>
        get() = mPercentageValueList

    private val mRupiahErrorPairList = MutableLiveData<Array<Pair<Boolean, String>?>>()
    val rupiahErrorPairList: LiveData<Array<Pair<Boolean, String>?>>
        get() = mRupiahErrorPairList

    private val mPercentageErrorPairList = MutableLiveData<Array<Pair<Boolean, String>?>>()
    val percentageErrorPairList: LiveData<Array<Pair<Boolean, String>?>>
        get() = mPercentageErrorPairList

    private val mCashbackTypeLiveData = MutableLiveData<CashbackType>().apply {
        value = CashbackType.Rupiah
    }
    val cashbackTypeData: LiveData<CashbackType>
        get() = mCashbackTypeLiveData

    private val mExpenseEstimationLiveData: LiveData<Int> = MediatorLiveData<Int>().apply {
        addSource(mCashbackTypeLiveData) { cashbackType ->
            value = when (cashbackType) {
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
                    mTresholdValueLiveData.value.toZeroIfNull(),
                    mPercentageMaximumDiscountLiveData.value.toZeroIfNull())
        }
        addSource(mPercentageMaximumDiscountLiveData) {
            value = CashbackPercentageInfoUiModel(
                    mPercentageMinimumPurchaseLiveData.value.toZeroIfNull(),
                    mPercentageDiscountAmountLiveData.value.toZeroIfNull(),
                    mTresholdValueLiveData.value.toZeroIfNull(),
                    mPercentageMaximumDiscountLiveData.value.toZeroIfNull())
        }
    }
    val cashbackPercentageInfoUiModelLiveData: LiveData<CashbackPercentageInfoUiModel>
        get() = mCashbackPercentageInfoUiModelLiveData

    private val mVoucherImageValueLiveData = MediatorLiveData<VoucherImageType>().apply {
        addSource(mRupiahMaximumDiscountLiveData) { amount ->
            if (mCashbackTypeLiveData.value == CashbackType.Rupiah) {
                value = VoucherImageType.Rupiah(amount)
            }
        }
        addSource(mPercentageDiscountAmountLiveData) { percentage ->
            if (mCashbackTypeLiveData.value == CashbackType.Percentage) {
                mPercentageMaximumDiscountLiveData.value?.let { amount ->
                    value = VoucherImageType.Percentage(amount, percentage)
                }
            }
        }
        addSource(mPercentageMaximumDiscountLiveData) { amount ->
            if (mCashbackTypeLiveData.value == CashbackType.Percentage) {
                mPercentageDiscountAmountLiveData.value?.let { percentage ->
                    value = VoucherImageType.Percentage(amount, percentage)
                }
            }
        }
        addSource(mCashbackTypeLiveData) { type ->
            mIsFirstTimeDrawLiveData.value?.let { isFirstTimeDraw ->
                if (!isFirstTimeDraw) {
                    when (type) {
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
        }
    }
    val voucherImageValueLiveData: LiveData<VoucherImageType>
        get() = mVoucherImageValueLiveData

    private val mIsFirstTimeDrawLiveData = MutableLiveData<Boolean>().apply {
        value = true
    }

    private val mRupiahValidationLiveData = MutableLiveData<Result<CashbackRupiahValidation>>()
    val rupiahValidationLiveData: LiveData<Result<CashbackRupiahValidation>>
        get() = mRupiahValidationLiveData

    private val mPercentageValidationLiveData = MutableLiveData<Result<CashbackPercentageValidation>>()
    val percentageValidationLiveData: LiveData<Result<CashbackPercentageValidation>>
        get() = mPercentageValidationLiveData

    private val mIdrVoucherRecommendationResult = MutableLiveData<Result<VoucherRecommendationData>>()
    val idrVoucherRecommendationResult: LiveData<Result<VoucherRecommendationData>>
        get() = mIdrVoucherRecommendationResult
    private var idrRecommendationData = VoucherRecommendationData()

    private val mPercentageVoucherRecommendationResult = MutableLiveData<Result<VoucherRecommendationData>>()
    val percentageVoucherRecommendationResult: LiveData<Result<VoucherRecommendationData>>
        get() = mPercentageVoucherRecommendationResult
    private var percentageRecommendationData = VoucherRecommendationData()

    private var mVoucherRecommendationStatus = MutableLiveData<Int>()
    val voucherRecommendationStatus: LiveData<Int>
        get() = mVoucherRecommendationStatus

    fun <T> addTextFieldValueToCalculation(value: Int?, type: T) {
        when (type) {
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
                mPercentageVoucherQuotaLiveData.value = value.toZeroIfNull()
            }
        }
    }

    fun getStaticRecommendationData(): VoucherRecommendationData {
        return VoucherRecommendationData(
                voucherDiscountAmt = InitialValue.DISCOUNT,
                voucherMinimumAmt = InitialValue.MINIMUM_PURCHASE,
                voucherQuota = InitialValue.VOUCHER_QUOTA,
                voucherDiscountAmtMax = InitialValue.MAXIMUM_DISCOUNT
        )
    }

    fun updateVoucherRecommendation(cashbackType: CashbackType, recommendationData: VoucherRecommendationData) {
        when (cashbackType) {
            CashbackType.Rupiah -> {
                idrRecommendationData = recommendationData
            }
            CashbackType.Percentage -> {
                percentageRecommendationData = recommendationData
            }
        }
    }

    fun updateRecommendationStatus(cashbackType: CashbackType) {
        when (cashbackType) {
            CashbackType.Rupiah -> {
                val isMaxDiscountEqual = idrRecommendationData.voucherDiscountAmtMax == mRupiahMaximumDiscountLiveData.value.toZeroIfNull()
                val isMinPurchaseEqual = idrRecommendationData.voucherMinimumAmt == mRupiahMinimumPurchaseLiveData.value.toZeroIfNull()
                val isVoucherQuotaEqual = idrRecommendationData.voucherQuota == mRupiahVoucherQuotaLiveData.value.toZeroIfNull()
                if (isMaxDiscountEqual && isMinPurchaseEqual && isVoucherQuotaEqual) {
                    mVoucherRecommendationStatus.value = WITH_RECOMMENDATION
                } else if (isMaxDiscountEqual || isMinPurchaseEqual || isVoucherQuotaEqual) {
                    mVoucherRecommendationStatus.value = EDITED_RECOMMENDATION
                } else {
                    mVoucherRecommendationStatus.value = NO_RECOMMENDATION
                }
            }
            CashbackType.Percentage -> {
                val isDiscountAmtEqual = percentageRecommendationData.voucherDiscountAmt == mPercentageDiscountAmountLiveData.value.toZeroIfNull()
                val isMaxDiscountEqual = percentageRecommendationData.voucherDiscountAmtMax == mPercentageMaximumDiscountLiveData.value.toZeroIfNull()
                val isMinPurchaseEqual = percentageRecommendationData.voucherMinimumAmt == mPercentageMinimumPurchaseLiveData.value.toZeroIfNull()
                val isVoucherQuotaEqual = percentageRecommendationData.voucherQuota == mPercentageVoucherQuotaLiveData.value.toZeroIfNull()
                if (isDiscountAmtEqual && isMaxDiscountEqual && isMinPurchaseEqual && isVoucherQuotaEqual) {
                    mVoucherRecommendationStatus.value = WITH_RECOMMENDATION
                } else if (isDiscountAmtEqual || isMaxDiscountEqual || isMinPurchaseEqual || isVoucherQuotaEqual) {
                    mVoucherRecommendationStatus.value = EDITED_RECOMMENDATION
                } else {
                    mVoucherRecommendationStatus.value = NO_RECOMMENDATION
                }
            }
        }
    }

    fun refreshValue() {
        mCashbackTypeLiveData.value?.let { type ->
            refreshTextFieldValue(type)
        }

        mIsFirstTimeDrawLiveData.value?.let { isFirstTimeDraw ->
            if (!isFirstTimeDraw) {
                mCashbackTypeLiveData.value?.let { type ->
                    when (type) {
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
            mIsFirstTimeDrawLiveData.value = false
        }
    }

    fun addErrorPair(isError: Boolean, errorMessage: String, type: PromotionType.Cashback) {
        when (type) {
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
                mPercentageVoucherQuotaErrorPairLiveData.value = Pair(isError, errorMessage)
            }
        }
    }

    fun resetErrorPairList(cashbackType: CashbackType) {
        val isError = false
        val errorMessage = ""
        when (cashbackType) {
            CashbackType.Rupiah -> {
                mRupiahMaximumDiscountErrorPairLiveData.value = Pair(isError, errorMessage)
                mRupiahMinimumPurchaseErrorPairLiveData.value = Pair(isError, errorMessage)
                mRupiahVoucherQuotaErrorPairLiveData.value = Pair(isError, errorMessage)
                mRupiahErrorPairList.value = arrayOf(
                        mRupiahMaximumDiscountErrorPairLiveData.value,
                        mRupiahMinimumPurchaseErrorPairLiveData.value,
                        mRupiahVoucherQuotaErrorPairLiveData.value
                )
            }
            CashbackType.Percentage -> {
                mPercentageDiscountAmountErrorPairLiveData.value = Pair(isError, errorMessage)
                mPercentageMaximumDiscountErrorPairLiveData.value = Pair(isError, errorMessage)
                mPercentageMinimumPurchaseErrorPairLiveData.value = Pair(isError, errorMessage)
                mPercentageVoucherQuotaErrorPairLiveData.value = Pair(isError, errorMessage)
                mPercentageErrorPairList.value = arrayOf(
                        mPercentageDiscountAmountErrorPairLiveData.value,
                        mPercentageMaximumDiscountErrorPairLiveData.value,
                        mPercentageMinimumPurchaseErrorPairLiveData.value,
                        mPercentageVoucherQuotaErrorPairLiveData.value
                )
            }
        }
    }

    fun changeCashbackType(cashbackType: CashbackType) {
        mCashbackTypeLiveData.value = cashbackType
        refreshTextFieldValue(cashbackType)
    }

    fun validateCashbackRupiahValues() {
        mRupiahMaximumDiscountLiveData.value?.let { benefitMax ->
            mRupiahMinimumPurchaseLiveData.value?.let { minPurchase ->
                mRupiahVoucherQuotaLiveData.value?.let { quota ->
                    launchCatchError(
                            block = {
                                mRupiahValidationLiveData.value = Success(withContext(dispatchers.io) {
                                    cashbackRupiahValidationUseCase.params = CashbackRupiahValidationUseCase.createRequestParam(benefitMax, minPurchase, quota)
                                    cashbackRupiahValidationUseCase.executeOnBackground()
                                })
                            },
                            onError = {
                                mRupiahValidationLiveData.value = Fail(it)
                            }
                    )
                }
            }
        }
    }

    fun validateCashbackPercentageValues() {
        mPercentageDiscountAmountLiveData.value?.let { benefitPercent ->
            mPercentageMaximumDiscountLiveData.value?.let { benefitMax ->
                mPercentageMinimumPurchaseLiveData.value?.let { minPurchase ->
                    mPercentageVoucherQuotaLiveData.value?.let { quota ->
                        launchCatchError(
                                block = {
                                    mPercentageValidationLiveData.value = Success(withContext(dispatchers.io) {
                                        cashbackPercentageValidationUseCase.params = CashbackPercentageValidationUseCase.createRequestParam(benefitPercent, benefitMax, minPurchase, quota)
                                        cashbackPercentageValidationUseCase.executeOnBackground()
                                    })
                                },
                                onError = {
                                    mPercentageValidationLiveData.value = Fail(it)
                                }
                        )
                    }
                }
            }
        }
    }

    fun getVoucherRecommendationFromApi() {
        launchCatchError(
                block = {
                    // param for rupiah cash back recommendation request
                    mIdrVoucherRecommendationResult.value = Success(withContext(dispatchers.io) {
                        getVoucherRecommendationUseCase.params = GetVoucherRecommendationUseCase.createRequestParam(VoucherTypeConst.CASHBACK, VoucherDiscountTypeConst.IDR)
                        getVoucherRecommendationUseCase.executeOnBackground()
                    })
                    // param for percentage cash back recommendation request
                    mPercentageVoucherRecommendationResult.value = Success(withContext(dispatchers.io) {
                        getVoucherRecommendationUseCase.params = GetVoucherRecommendationUseCase.createRequestParam(VoucherTypeConst.CASHBACK, VoucherDiscountTypeConst.PERCENTAGE)
                        getVoucherRecommendationUseCase.executeOnBackground()
                    })
                },
                onError = {
                    mIdrVoucherRecommendationResult.value = Fail(it)
                    mPercentageVoucherRecommendationResult.value = Fail(it)
                }
        )
    }

    fun getVoucherRecommendationData(cashbackType: CashbackType): VoucherRecommendationData {
        return when (cashbackType) {
            CashbackType.Rupiah -> {
                idrRecommendationData
            }
            CashbackType.Percentage -> {
                percentageRecommendationData
            }
        }
    }

    private fun refreshTextFieldValue(cashbackType: CashbackType) {
        when (cashbackType) {
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
    }

}