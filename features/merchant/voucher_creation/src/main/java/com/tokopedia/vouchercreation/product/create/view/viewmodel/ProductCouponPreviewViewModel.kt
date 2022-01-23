package com.tokopedia.vouchercreation.product.create.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.utils.lifecycle.SingleLiveEvent
import com.tokopedia.vouchercreation.product.create.domain.entity.CouponInformation
import com.tokopedia.vouchercreation.product.create.domain.entity.CouponProduct
import com.tokopedia.vouchercreation.product.create.domain.entity.CouponSettings
import com.tokopedia.vouchercreation.product.create.domain.usecase.CreateCouponUseCase
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ProductCouponPreviewViewModel @Inject constructor(
    private val dispatchers: CoroutineDispatchers,
    private val createCouponUseCase : CreateCouponUseCase
) : BaseViewModel(dispatchers.main) {

    private val _areInputValid = MutableLiveData<Boolean>()
    val areInputValid: LiveData<Boolean>
        get() = _areInputValid

    private val _createCoupon = SingleLiveEvent<Result<Int>>()
    val createCoupon: LiveData<Result<Int>>
        get() = _createCoupon

    fun validateCoupon(couponSettings: CouponSettings? , couponInformation: CouponInformation?, couponProducts: List<CouponProduct>) {
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


    fun createCoupon(
        sourceId: String,
        isUpdateMode : Boolean,
        couponInformation: CouponInformation,
        couponSettings: CouponSettings,
        couponProducts: List<CouponProduct>
    ) {
        launchCatchError(
            block = {
                val result = withContext(dispatchers.io) {
                    createCouponUseCase.execute(
                        this,
                        sourceId,
                        isUpdateMode,
                        couponInformation,
                        couponSettings,
                        couponProducts
                    )
                }
                _createCoupon.setValue(Success(result))
            },
            onError = {
                _createCoupon.setValue(Fail(it))
            }
        )
    }
}