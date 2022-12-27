package com.tokopedia.mvc.presentation.summary.viewmodel

import android.graphics.Bitmap
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.mvc.domain.entity.VoucherConfiguration
import com.tokopedia.mvc.domain.entity.enums.BenefitType
import com.tokopedia.mvc.domain.entity.enums.ImageRatio
import com.tokopedia.mvc.domain.usecase.GetCouponImagePreviewFacadeUseCase
import com.tokopedia.mvc.domain.usecase.MerchantPromotionGetMVDataByIDUseCase
import javax.inject.Inject

class SummaryViewModel @Inject constructor(
    private val dispatchers: CoroutineDispatchers,
    private val merchantPromotionGetMVDataByIDUseCase: MerchantPromotionGetMVDataByIDUseCase,
    private val getCouponImagePreviewUseCase: GetCouponImagePreviewFacadeUseCase
) : BaseViewModel(dispatchers.main) {

    private val _error = MutableLiveData<Throwable>()
    val error: LiveData<Throwable> get() = _error

    private val _configuration = MutableLiveData<VoucherConfiguration>()
    val configuration: LiveData<VoucherConfiguration> get() = _configuration
    val maxExpense = Transformations.map(configuration) { getMaxExpenses(it) }

    fun setConfiguration(configuration: VoucherConfiguration) {
        _configuration.value = configuration
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

    private val _couponImage = MutableLiveData<Bitmap>()
    val couponImage: LiveData<Bitmap>
        get() = _couponImage

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
}
