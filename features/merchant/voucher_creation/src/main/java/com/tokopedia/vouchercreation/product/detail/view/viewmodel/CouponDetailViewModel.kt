package com.tokopedia.vouchercreation.product.detail.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.usecase.launch_cache_error.launchCatchError
import com.tokopedia.vouchercreation.common.consts.VoucherStatusConst
import com.tokopedia.vouchercreation.common.mapper.CouponMapper
import com.tokopedia.vouchercreation.product.create.domain.entity.CouponDetailWithMetadata
import com.tokopedia.vouchercreation.product.create.domain.entity.CouponSettings
import com.tokopedia.vouchercreation.product.create.domain.entity.CouponUiModel
import com.tokopedia.vouchercreation.product.create.domain.usecase.GetCouponDetailFacadeUseCase
import com.tokopedia.vouchercreation.product.share.domain.entity.ShopWithTopProducts
import com.tokopedia.vouchercreation.product.share.domain.usecase.GetShopAndTopProductsUseCase
import kotlinx.coroutines.withContext
import javax.inject.Inject

class CouponDetailViewModel @Inject constructor(
    private val dispatchers: CoroutineDispatchers,
    private val getCouponDetailFacadeUseCase: GetCouponDetailFacadeUseCase,
    private val getShopAndTopProductsUseCase: GetShopAndTopProductsUseCase,
    private val mapper: CouponMapper
) : BaseViewModel(dispatchers.main) {

    private val _couponDetail = MutableLiveData<Result<CouponDetailWithMetadata>>()
    val couponDetail: LiveData<Result<CouponDetailWithMetadata>>
        get() = _couponDetail

    private val _shopWithTopProducts = MutableLiveData<Result<ShopWithTopProducts>>()
    val shopWithTopProducts: LiveData<Result<ShopWithTopProducts>> = _shopWithTopProducts

    private var coupon: CouponUiModel? = null
    private var maxProductLimit: Int = 0

    fun getCouponDetail(couponId : Long) {
        launchCatchError(
            block = {
                val result = withContext(dispatchers.io) {
                    getCouponDetailFacadeUseCase.execute(couponId)
                }
                _couponDetail.value = Success(result)
            },
            onError = {
                _couponDetail.value = Fail(it)
            }
        )
    }

    fun getShopAndTopProducts(coupon : CouponUiModel) {
        launchCatchError(
            block = {
                val result = withContext(dispatchers.io) {
                    getShopAndTopProductsUseCase.execute(coupon)
                }
                _shopWithTopProducts.value = Success(result)
            },
            onError = {
                _shopWithTopProducts.setValue(Fail(it))
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

    fun getCouponSettings(coupon: CouponUiModel): CouponSettings {
        return mapper.map(coupon).settings
    }

    fun isOngoingCoupon(@VoucherStatusConst couponStatus : Int) : Boolean {
        return couponStatus == VoucherStatusConst.ONGOING
    }
}