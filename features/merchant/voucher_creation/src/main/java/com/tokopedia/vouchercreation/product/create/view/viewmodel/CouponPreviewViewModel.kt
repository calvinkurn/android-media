package com.tokopedia.vouchercreation.product.create.view.viewmodel

import androidx.lifecycle.LiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.utils.lifecycle.SingleLiveEvent
import com.tokopedia.vouchercreation.product.create.domain.entity.Coupon
import com.tokopedia.vouchercreation.product.create.domain.entity.ImageRatio
import com.tokopedia.vouchercreation.product.create.domain.usecase.GetCouponImagePreviewUseCase
import kotlinx.coroutines.withContext
import javax.inject.Inject

class CouponPreviewViewModel @Inject constructor(
    private val dispatchers: CoroutineDispatchers,
    private val getCouponImagePreviewUseCase: GetCouponImagePreviewUseCase
) : BaseViewModel(dispatchers.main) {


    private val _couponImage = SingleLiveEvent<Result<Int>>()
    val couponImage: LiveData<Result<Int>>
        get() = _couponImage

    fun previewImage(imageRatio: ImageRatio, coupon: Coupon) {
        launchCatchError(
            block = {
                val result = withContext(dispatchers.io) {
                    getCouponImagePreviewUseCase.execute(this, imageRatio, coupon)
                }
                _couponImage.value = Success(result)
            },
            onError = {
                _couponImage.setValue(Fail(it))
            }
        )
    }
}