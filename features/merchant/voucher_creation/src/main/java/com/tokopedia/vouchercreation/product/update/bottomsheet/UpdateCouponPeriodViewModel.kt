package com.tokopedia.vouchercreation.product.update.bottomsheet

import androidx.lifecycle.LiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.utils.lifecycle.SingleLiveEvent
import com.tokopedia.vouchercreation.common.consts.ImageGeneratorConstant
import com.tokopedia.vouchercreation.product.create.domain.entity.CouponInformation
import com.tokopedia.vouchercreation.product.create.domain.entity.CouponProduct
import com.tokopedia.vouchercreation.product.create.domain.entity.CouponSettings
import com.tokopedia.vouchercreation.product.create.domain.usecase.update.UpdateCouponFacadeUseCase
import kotlinx.coroutines.withContext
import javax.inject.Inject

class UpdateCouponPeriodViewModel @Inject constructor(
    private val dispatchers: CoroutineDispatchers,
    private val updateCouponUseCase: UpdateCouponFacadeUseCase,
) : BaseViewModel(dispatchers.main) {


    private val _areInputValid = SingleLiveEvent<Boolean>()
    val areInputValid: LiveData<Boolean>
        get() = _areInputValid


    private val _updateCouponResult = SingleLiveEvent<Result<Boolean>>()
    val updateCouponResult: LiveData<Result<Boolean>>
        get() = _updateCouponResult



    fun validateCoupon(
        couponSettings: CouponSettings?,
        couponInformation: CouponInformation?,
        couponProducts: List<CouponProduct>
    ) {
        if (couponSettings == null) {
            _areInputValid.value = false
            return
        }

        if (couponInformation == null) {
            _areInputValid.value = false
            return
        }

        if (couponProducts.isEmpty()) {
            _areInputValid.value = false
            return
        }

        _areInputValid.value = true
    }


    fun updateCoupon(
        couponId: Long,
        couponInformation: CouponInformation,
        couponSettings: CouponSettings,
        couponProducts: List<CouponProduct>
    ) {
        launchCatchError(
            block = {
                val result = withContext(dispatchers.io) {
                    updateCouponUseCase.execute(
                        this,
                        ImageGeneratorConstant.IMAGE_TEMPLATE_COUPON_PRODUCT_SOURCE_ID,
                        couponId,
                        couponInformation,
                        couponSettings,
                        couponProducts
                    )
                }
                _updateCouponResult.value = Success(result)
            },
            onError = {
                _updateCouponResult.value = Fail(it)
            }
        )
    }
}