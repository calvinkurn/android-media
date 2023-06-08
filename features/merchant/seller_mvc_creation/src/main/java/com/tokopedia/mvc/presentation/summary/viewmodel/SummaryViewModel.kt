package com.tokopedia.mvc.presentation.summary.viewmodel

import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.campaign.utils.constant.DateConstant
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.mvc.R
import com.tokopedia.mvc.domain.entity.SelectedProduct
import com.tokopedia.mvc.domain.entity.VoucherConfiguration
import com.tokopedia.mvc.domain.entity.VoucherValidationResult
import com.tokopedia.mvc.domain.entity.enums.BenefitType
import com.tokopedia.mvc.domain.entity.enums.ImageRatio
import com.tokopedia.mvc.domain.usecase.AddEditCouponFacadeUseCase
import com.tokopedia.mvc.domain.usecase.GetCouponImagePreviewFacadeUseCase
import com.tokopedia.mvc.domain.usecase.MerchantPromotionGetMVDataByIDUseCase
import com.tokopedia.mvc.domain.usecase.VoucherValidationPartialUseCase
import com.tokopedia.mvc.presentation.bottomsheet.voucherperiod.DateStartEndData
import com.tokopedia.mvc.util.constant.CommonConstant
import com.tokopedia.mvc.util.formatTo
import com.tokopedia.mvc.util.tracker.SummaryPageTracker
import java.util.*
import javax.inject.Inject

class SummaryViewModel @Inject constructor(
    private val dispatchers: CoroutineDispatchers,
    private val merchantPromotionGetMVDataByIDUseCase: MerchantPromotionGetMVDataByIDUseCase,
    private val getCouponImagePreviewUseCase: GetCouponImagePreviewFacadeUseCase,
    private val addEditCouponFacadeUseCase: AddEditCouponFacadeUseCase,
    private val voucherValidationPartialUseCase: VoucherValidationPartialUseCase,
    private val tracker: SummaryPageTracker,
    private val sharedPreferences: SharedPreferences
) : BaseViewModel(dispatchers.main) {

    companion object {
        private const val ADDING_VOUCHER_ID = 0L
    }

    private val _error = MutableLiveData<Throwable>()
    val error: LiveData<Throwable> get() = _error

    private val _errorUpload = MutableLiveData<Throwable>()
    val errorUpload: LiveData<Throwable> get() = _errorUpload

    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> get() = _isLoading

    private val _uploadCouponSuccess = MutableLiveData<VoucherConfiguration>()
    val uploadCouponSuccess: LiveData<VoucherConfiguration> get() = _uploadCouponSuccess

    private val _configuration = MutableLiveData<VoucherConfiguration>()
    val maxExpense = Transformations.map(_configuration) { getMaxExpenses(it) }
    val configuration = Transformations.map(_configuration) {
        if (isDuplicate) {
            it.copy(
                voucherId = ADDING_VOUCHER_ID,
                startPeriod = Date(),
                endPeriod = Date(),
                duplicatedVoucherId = it.voucherId
            )
        } else {
            it
        }
    }
    val enableCouponTypeChange = Transformations.map(configuration) {
        it.voucherId == ADDING_VOUCHER_ID
    }
    val submitButtonText = Transformations.map(configuration) {
        if (it.voucherId == ADDING_VOUCHER_ID) {
            R.string.smvc_summary_page_submit_text
        } else {
            R.string.smvc_save
        }
    }

    private val _products = MutableLiveData<List<SelectedProduct>>()
    val products: LiveData<List<SelectedProduct>> get() = _products

    private val _isInputValid = MutableLiveData(false)
    val isInputValid: LiveData<Boolean> get() = _isInputValid

    private val _couponImage = MutableLiveData<Bitmap>()
    val couponImage: LiveData<Bitmap>
        get() = _couponImage

    private val _couponPeriods = MutableLiveData<List<DateStartEndData>>()
    val couponPeriods: LiveData<List<DateStartEndData>>
        get() = _couponPeriods

    private var isDuplicate = false

    fun setConfiguration(configuration: VoucherConfiguration) {
        _configuration.value = configuration
    }

    fun updateProductList(products: List<SelectedProduct>) {
        _products.value = products
    }

    fun setAsDuplicateCoupon() {
        isDuplicate = true
    }

    fun setupEditMode(voucherId: Long) {
        launchCatchError(
            dispatchers.io,
            block = {
                val param = MerchantPromotionGetMVDataByIDUseCase.Param(
                    voucherId = voucherId
                )
                val result = merchantPromotionGetMVDataByIDUseCase.execute(param)
                _configuration.postValue(result.toVoucherConfiguration())
                _products.postValue(result.toSelectedProducts())
            },
            onError = {
                _error.postValue(it)
            }
        )
    }

    fun getMaxExpenses(configuration: VoucherConfiguration): Long {
        return with(configuration) {
            if (benefitType == BenefitType.NOMINAL) {
                benefitIdr
            } else {
                benefitMax
            } * quota
        }
    }

    fun previewImage(
        voucherConfiguration: VoucherConfiguration,
        parentProductIds : List<Long>,
        imageRatio: ImageRatio
    ) {
        launchCatchError(
            dispatchers.io,
            block = {
                val result = getCouponImagePreviewUseCase.execute(
                        checkIsAdding(voucherConfiguration),
                        voucherConfiguration,
                        parentProductIds,
                        imageRatio
                    )
                _couponImage.postValue(BitmapFactory.decodeByteArray(result, Int.ZERO, result.size))
            },
            onError = {
                _error.postValue(it)
            }
        )
    }

    fun addCoupon(voucherConfiguration: VoucherConfiguration) {
        launchCatchError(
            dispatchers.io,
            block = {
                addEditCouponFacadeUseCase.executeAdd(
                    voucherConfiguration,
                    products.value ?: return@launchCatchError,
                    voucherConfiguration.warehouseId.toString()
                )
                _uploadCouponSuccess.postValue(voucherConfiguration)
                _isLoading.postValue(false)
            },
            onError = {
                _errorUpload.postValue(it)
                _isLoading.postValue(false)
            }
        )
    }

    fun editCoupon(voucherConfiguration: VoucherConfiguration) {
        launchCatchError(
            dispatchers.io,
            block = {
                addEditCouponFacadeUseCase.executeEdit(
                    voucherConfiguration,
                    products.value ?: return@launchCatchError
                )
                _uploadCouponSuccess.postValue(voucherConfiguration)
                _isLoading.postValue(false)
            },
            onError = {
                _errorUpload.postValue(it)
                _isLoading.postValue(false)
            }
        )
    }

    fun saveCoupon() {
        val voucherConfiguration = configuration.value ?: return
        _isLoading.value = true
        if (voucherConfiguration.voucherId > ADDING_VOUCHER_ID) {
            editCoupon(voucherConfiguration)
            tracker.sendClickSimpanEvent(voucherConfiguration.voucherId.toString())
        } else {
            addCoupon(voucherConfiguration)
            tracker.sendClickBuatKuponEvent()
        }
    }

    fun validateTnc(checked: Boolean) {
        _isInputValid.value = checked
    }

    fun checkIsAdding(configuration: VoucherConfiguration): Boolean {
        return configuration.voucherId == ADDING_VOUCHER_ID
    }

    fun handleVoucherInputValidation(voucherConfiguration: VoucherConfiguration) {
        launchCatchError(
            dispatchers.io,
            block = {
                val voucherValidationParam = VoucherValidationPartialUseCase.Param(
                    benefitIdr = voucherConfiguration.benefitIdr,
                    benefitMax = voucherConfiguration.benefitMax,
                    benefitPercent = voucherConfiguration.benefitPercent,
                    benefitType = voucherConfiguration.benefitType,
                    promoType = voucherConfiguration.promoType,
                    isVoucherProduct = voucherConfiguration.isVoucherProduct,
                    minPurchase = voucherConfiguration.minPurchase,
                    productIds = emptyList(),
                    targetBuyer = voucherConfiguration.targetBuyer,
                    couponName = voucherConfiguration.voucherName,
                    isPublic = voucherConfiguration.isVoucherPublic,
                    code = voucherConfiguration.voucherCode,
                    isPeriod = voucherConfiguration.isPeriod,
                    periodType = voucherConfiguration.periodType,
                    periodRepeat = voucherConfiguration.periodRepeat,
                    totalPeriod = voucherConfiguration.totalPeriod,
                    startDate = voucherConfiguration.startPeriod.formatTo(DateConstant.DATE_MONTH_YEAR_BASIC),
                    endDate = voucherConfiguration.endPeriod.formatTo(DateConstant.DATE_MONTH_YEAR_BASIC),
                    startHour = voucherConfiguration.startPeriod.formatTo(DateConstant.TIME_MINUTE_PRECISION),
                    endHour = voucherConfiguration.endPeriod.formatTo(DateConstant.TIME_MINUTE_PRECISION),
                    quota = voucherConfiguration.quota
                )
                val validationResult = voucherValidationPartialUseCase.execute(voucherValidationParam)
                _couponPeriods.postValue(mapVoucherRecurringPeriodData(validationResult.validationDate))
            },
            onError = {
                _error.postValue(it)
            }
        )
    }

    fun mapVoucherRecurringPeriodData(validationDate: List<VoucherValidationResult.ValidationDate>): List<DateStartEndData> {
        return validationDate
            .filter { it.available }
            .map {
                DateStartEndData(
                    dateStart = it.dateStart,
                    dateEnd = it.dateEnd,
                    hourStart = it.hourStart,
                    hourEnd = it.hourEnd
                )
            }
    }

    fun coachMarkIsShown(): Boolean {
        return sharedPreferences.getBoolean(
            CommonConstant.SHARED_PREF_VOUCHER_CREATION_SUMMARY_COACH_MARK,
            false
        )
    }

    fun setSharedPrefCoachMarkAlreadyShown() {
        sharedPreferences.edit()
            .putBoolean(CommonConstant.SHARED_PREF_VOUCHER_CREATION_SUMMARY_COACH_MARK, true)
            .apply()
    }
}
