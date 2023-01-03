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
import com.tokopedia.mvc.domain.usecase.CreateCouponFacadeUseCase
import com.tokopedia.mvc.domain.usecase.GetCouponImagePreviewFacadeUseCase
import com.tokopedia.mvc.domain.usecase.MerchantPromotionGetMVDataByIDUseCase
import com.tokopedia.network.exception.MessageErrorException
import javax.inject.Inject

class SummaryViewModel @Inject constructor(
    private val dispatchers: CoroutineDispatchers,
    private val merchantPromotionGetMVDataByIDUseCase: MerchantPromotionGetMVDataByIDUseCase,
    private val getCouponImagePreviewUseCase: GetCouponImagePreviewFacadeUseCase,
    private val createCouponFacadeUseCase: CreateCouponFacadeUseCase
) : BaseViewModel(dispatchers.main) {

    private val _error = MutableLiveData<Throwable>()
    val error: LiveData<Throwable> get() = _error

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
                val result = createCouponFacadeUseCase.execute(
                    configuration.value ?: return@launchCatchError,
                    products.value ?: return@launchCatchError,
                    ""
                )
                _error.postValue(MessageErrorException("Sukses"))
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
                val result = createCouponFacadeUseCase.executeUpdate(
                    configuration.value ?: return@launchCatchError,
                    products.value ?: return@launchCatchError
                )
                _error.postValue(MessageErrorException("Sukses"))
            },
            onError = {
                _error.postValue(it)
            }
        )
    }
}
