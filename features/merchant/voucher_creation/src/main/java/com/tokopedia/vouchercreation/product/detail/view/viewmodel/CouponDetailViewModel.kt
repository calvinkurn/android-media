package com.tokopedia.vouchercreation.product.detail.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.usecase.launch_cache_error.launchCatchError
import com.tokopedia.vouchercreation.common.consts.ImageGeneratorConstant
import com.tokopedia.vouchercreation.product.create.domain.entity.CouponDetailWithMetadata
import com.tokopedia.vouchercreation.product.create.domain.entity.CouponUiModel
import com.tokopedia.vouchercreation.product.create.domain.usecase.GetCouponDetailFacadeUseCase
import com.tokopedia.vouchercreation.product.share.domain.entity.CouponImageWithShop
import com.tokopedia.vouchercreation.product.share.domain.usecase.GenerateImageWithStatisticsFacadeUseCase
import kotlinx.coroutines.withContext
import javax.inject.Inject

class CouponDetailViewModel @Inject constructor(
    private val dispatchers: CoroutineDispatchers,
    private val getCouponDetailFacadeUseCase: GetCouponDetailFacadeUseCase,
    private val generateImageFacadeUseCase: GenerateImageWithStatisticsFacadeUseCase
) : BaseViewModel(dispatchers.main) {

    private val _couponDetail = MutableLiveData<Result<CouponDetailWithMetadata>>()
    val couponDetail: LiveData<Result<CouponDetailWithMetadata>>
        get() = _couponDetail

    private val _couponImageWithShop = MutableLiveData<Result<CouponImageWithShop>>()
    val couponImageWithShop: LiveData<Result<CouponImageWithShop>> = _couponImageWithShop

    private var coupon: CouponUiModel? = null
    private var maxProductLimit: Int = 0

    fun getCouponDetail(couponId : Long) {
        launchCatchError(
            block = {
                val result = withContext(dispatchers.io) {
                    getCouponDetailFacadeUseCase.execute(this, couponId)
                }
                _couponDetail.value = Success(result)
            },
            onError = {
                _couponDetail.value = Fail(it)
            }
        )
    }

    fun generateImage(coupon : CouponUiModel) {
        launchCatchError(
            block = {
                val result = withContext(dispatchers.io) {
                    generateImageFacadeUseCase.execute(
                        this,
                        ImageGeneratorConstant.IMAGE_TEMPLATE_COUPON_PRODUCT_SOURCE_ID,
                        coupon
                    )
                }
                _couponImageWithShop.value = Success(result)
            },
            onError = {
                _couponImageWithShop.setValue(Fail(it))
            }
        )
    }

    fun setCoupon(coupon: CouponUiModel) {
        this.coupon = coupon
    }

    fun getCoupon(): CouponUiModel? {
        return coupon
    }

    fun setMaxProductLimit(maxProductLimit: Int) {
        this.maxProductLimit = maxProductLimit
    }

    fun getMaxProductLimit(): Int {
        return maxProductLimit
    }
}