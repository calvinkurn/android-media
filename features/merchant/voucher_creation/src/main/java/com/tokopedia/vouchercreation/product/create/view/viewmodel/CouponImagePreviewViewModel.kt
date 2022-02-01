package com.tokopedia.vouchercreation.product.create.view.viewmodel

import androidx.lifecycle.LiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.utils.lifecycle.SingleLiveEvent
import com.tokopedia.vouchercreation.product.create.domain.entity.CouponInformation
import com.tokopedia.vouchercreation.product.create.domain.entity.CouponSettings
import com.tokopedia.vouchercreation.product.create.domain.entity.ImageRatio
import com.tokopedia.vouchercreation.product.create.domain.usecase.GetCouponImagePreviewFacadeUseCase
import kotlinx.coroutines.withContext
import javax.inject.Inject

class CouponImagePreviewViewModel @Inject constructor(
    private val dispatchers: CoroutineDispatchers,
    private val getCouponImagePreviewUseCase: GetCouponImagePreviewFacadeUseCase
) : BaseViewModel(dispatchers.main) {


    private val _couponImage = SingleLiveEvent<Result<ByteArray>>()
    val couponImage: LiveData<Result<ByteArray>>
        get() = _couponImage

    fun previewImage(
        couponInformation: CouponInformation,
        couponSettings: CouponSettings,
        productCount: Int,
        firstProductImageUrl: String,
        secondProductImageUrl: String,
        thirdProductImageUrl: String,
        imageRatio: ImageRatio
    ) {
        launchCatchError(
            block = {
                val result = withContext(dispatchers.io) {
                    getCouponImagePreviewUseCase.execute(
                        this,
                        couponInformation,
                        couponSettings,
                        productCount,
                        firstProductImageUrl,
                        secondProductImageUrl,
                        thirdProductImageUrl,
                        imageRatio
                    )
                }
                _couponImage.value = Success(result)
            },
            onError = {
                _couponImage.setValue(Fail(it))
            }
        )
    }
}