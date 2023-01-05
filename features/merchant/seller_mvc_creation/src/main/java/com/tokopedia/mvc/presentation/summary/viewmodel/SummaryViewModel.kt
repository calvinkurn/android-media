package com.tokopedia.mvc.presentation.summary.viewmodel

import android.graphics.Bitmap
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.kotlin.extensions.view.isMoreThanZero
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

    private val _error = MutableLiveData<Throwable>()
    val error: LiveData<Throwable> get() = _error

    private val _uploadCouponSuccess = MutableLiveData<VoucherConfiguration>()
    val uploadCouponSuccess: LiveData<VoucherConfiguration> get() = _uploadCouponSuccess

    private val _configuration = MutableLiveData<VoucherConfiguration>()
    val configuration: LiveData<VoucherConfiguration> get() = _configuration
    val maxExpense = Transformations.map(configuration) { getMaxExpenses(it) }

    private val _products = MutableLiveData<List<SelectedProduct>>()
    val products: LiveData<List<SelectedProduct>> get() = _products

    private val _couponImage = MutableLiveData<Bitmap>()
    val couponImage: LiveData<Bitmap>
        get() = _couponImage

    fun setConfiguration(configuration: VoucherConfiguration) {
        _configuration.value = configuration
    }

    fun updateProductList(products: List<SelectedProduct>) {
        _products.value = products
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

    fun addCoupon() {
        launchCatchError(
            dispatchers.io,
            block = {
                val voucherConfiguration = configuration.value ?: return@launchCatchError
                addEditCouponFacadeUseCase.executeAdd(
                    voucherConfiguration,
                    products.value ?: return@launchCatchError,
                    voucherConfiguration.warehouseId.toString()
                )
                _uploadCouponSuccess.postValue(voucherConfiguration)
            },
            onError = {
                _error.postValue(it)
            }
        )
    }

    fun editCoupon() {
        launchCatchError(
            dispatchers.io,
            block = {
                val voucherConfiguration = configuration.value ?: return@launchCatchError
                val result = addEditCouponFacadeUseCase.executeEdit(
                    voucherConfiguration,
                    products.value ?: return@launchCatchError
                )
                _uploadCouponSuccess.postValue(voucherConfiguration)
            },
            onError = {
                _error.postValue(it)
            }
        )
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
}
