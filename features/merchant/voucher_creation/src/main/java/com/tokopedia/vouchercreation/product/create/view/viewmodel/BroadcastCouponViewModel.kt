package com.tokopedia.vouchercreation.product.create.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.vouchercreation.product.create.domain.entity.CouponUiModel
import com.tokopedia.vouchercreation.product.create.domain.usecase.GetCouponDetailUseCase
import com.tokopedia.vouchercreation.product.share.domain.entity.ShopWithTopProducts
import com.tokopedia.vouchercreation.product.share.domain.usecase.GetShopAndTopProductsUseCase
import com.tokopedia.vouchercreation.shop.voucherlist.domain.model.ShopBasicDataResult
import com.tokopedia.vouchercreation.shop.voucherlist.domain.usecase.GetBroadCastMetaDataUseCase
import com.tokopedia.vouchercreation.shop.voucherlist.model.remote.ChatBlastSellerMetadata
import kotlinx.coroutines.withContext
import javax.inject.Inject

class BroadcastCouponViewModel @Inject constructor(
    private val dispatchers: CoroutineDispatchers,
    private val getBroadCastMetaDataUseCase: GetBroadCastMetaDataUseCase,
    private val getShopAndTopProductsUseCase: GetShopAndTopProductsUseCase,
    private val getCouponDetailUseCase: GetCouponDetailUseCase
) : BaseViewModel(dispatchers.main) {


    private val _broadcastMetadata = MutableLiveData<Result<ChatBlastSellerMetadata>>()
    val broadcastMetadata: LiveData<Result<ChatBlastSellerMetadata>> = _broadcastMetadata

    private val _shop = MutableLiveData<Result<ShopBasicDataResult>>()
    val shop: LiveData<Result<ShopBasicDataResult>> = _shop


    private val _couponDetail = MutableLiveData<Result<CouponUiModel>>()
    val couponDetail: LiveData<Result<CouponUiModel>>
        get() = _couponDetail

    private val _shopWithTopProducts = MutableLiveData<Result<ShopWithTopProducts>>()
    val shopWithTopProducts: LiveData<Result<ShopWithTopProducts>> = _shopWithTopProducts

    private var coupon: CouponUiModel? = null

    fun setCoupon(coupon: CouponUiModel) {
        this.coupon = coupon
    }

    fun getCoupon(): CouponUiModel? {
        return coupon
    }

    fun getBroadcastMetaData() {
        launchCatchError(
            block = {
                val result = withContext(dispatchers.io) {
                    getBroadCastMetaDataUseCase.executeOnBackground()
                }
                _broadcastMetadata.value = Success(result)
            },
            onError = {
                _broadcastMetadata.setValue(Fail(it))
            }
        )
    }


    fun getShopAndTopProducts(coupon: CouponUiModel) {
        launchCatchError(
            block = {
                val result = withContext(dispatchers.io) {
                    getShopAndTopProductsUseCase.execute(
                        this,
                        coupon
                    )
                }
                _shopWithTopProducts.value = Success(result)
            },
            onError = {
                _shopWithTopProducts.setValue(Fail(it))
            }
        )
    }

    fun getCouponDetail(couponId: Long) {
        launchCatchError(
            block = {
                val result = withContext(dispatchers.io) {
                    getCouponDetailUseCase.params =
                        GetCouponDetailUseCase.createRequestParam(couponId)
                    getCouponDetailUseCase.executeOnBackground()
                }
                _couponDetail.value = Success(result)
            },
            onError = {
                _couponDetail.value = Fail(it)
            }
        )
    }
}