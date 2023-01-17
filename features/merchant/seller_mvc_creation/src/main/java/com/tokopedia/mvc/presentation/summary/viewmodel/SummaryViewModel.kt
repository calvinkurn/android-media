package com.tokopedia.mvc.presentation.summary.viewmodel

import android.graphics.Bitmap
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.mvc.domain.entity.SelectedProduct
import com.tokopedia.mvc.domain.entity.VoucherConfiguration
import com.tokopedia.mvc.domain.entity.enums.BenefitType
import com.tokopedia.mvc.domain.entity.enums.ImageRatio
import com.tokopedia.mvc.domain.usecase.AddEditCouponFacadeUseCase
import com.tokopedia.mvc.domain.usecase.GetCouponImagePreviewFacadeUseCase
import com.tokopedia.mvc.domain.usecase.MerchantPromotionGetMVDataByIDUseCase
import com.tokopedia.mvc.presentation.bottomsheet.voucherperiod.DateStartEndData
import com.tokopedia.mvc.util.formatTo
import com.tokopedia.utils.date.DateUtil
import com.tokopedia.utils.date.addTimeToSpesificDate
import java.util.*
import javax.inject.Inject

class SummaryViewModel @Inject constructor(
    private val dispatchers: CoroutineDispatchers,
    private val merchantPromotionGetMVDataByIDUseCase: MerchantPromotionGetMVDataByIDUseCase,
    private val getCouponImagePreviewUseCase: GetCouponImagePreviewFacadeUseCase,
    private val addEditCouponFacadeUseCase: AddEditCouponFacadeUseCase
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
            it.copy(voucherId = ADDING_VOUCHER_ID)
        } else {
            it
        }
    }
    val enableCouponTypeChange = Transformations.map(_configuration) {
        it.voucherId == ADDING_VOUCHER_ID
    }

    private val _products = MutableLiveData<List<SelectedProduct>>()
    val products: LiveData<List<SelectedProduct>> get() = _products

    private val _isInputValid = MutableLiveData(false)
    val isInputValid: LiveData<Boolean> get() = _isInputValid

    private val _couponImage = MutableLiveData<Bitmap>()
    val couponImage: LiveData<Bitmap>
        get() = _couponImage

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
        isCreateMode: Boolean,
        voucherConfiguration: VoucherConfiguration,
        parentProductIds : List<Long>,
        imageRatio: ImageRatio
    ) {
        launchCatchError(
            dispatchers.io,
            block = {
                val result = getCouponImagePreviewUseCase.execute(
                        isCreateMode,
                        voucherConfiguration,
                        parentProductIds,
                        imageRatio
                    )
                _couponImage.postValue(result)
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
        } else {
            addCoupon(voucherConfiguration)
        }
    }

    fun getOtherPeriod(configuration: VoucherConfiguration): MutableList<DateStartEndData> {
        val list = mutableListOf<DateStartEndData>()
        repeat(configuration.totalPeriod) {
            val start = configuration.startPeriod.addTimeToSpesificDate(Calendar.MONTH, it.inc())
            val end = configuration.endPeriod.addTimeToSpesificDate(Calendar.MONTH, it.inc())
            list.add(
                DateStartEndData(
                    start.formatTo(DateUtil.YYYY_MM_DD),
                    end.formatTo(DateUtil.YYYY_MM_DD),
                    start.formatTo(DateUtil.HH_MM),
                    end.formatTo(DateUtil.HH_MM),
                )
            )
        }
        return list
    }

    fun validateTnc(checked: Boolean) {
        _isInputValid.value = checked
    }
}
