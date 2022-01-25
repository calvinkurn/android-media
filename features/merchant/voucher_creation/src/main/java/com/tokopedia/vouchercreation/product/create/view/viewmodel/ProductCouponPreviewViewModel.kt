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
import com.tokopedia.vouchercreation.product.create.domain.usecase.create.CreateCouponUseCase
import com.tokopedia.vouchercreation.product.create.domain.usecase.update.UpdateCouponFacadeUseCase
import com.tokopedia.vouchercreation.shop.voucherlist.domain.usecase.GetBroadCastMetaDataUseCase
import com.tokopedia.vouchercreation.shop.voucherlist.model.remote.ChatBlastSellerMetadata
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ProductCouponPreviewViewModel @Inject constructor(
    private val dispatchers: CoroutineDispatchers,
    private val createCouponUseCase: CreateCouponUseCase,
    private val getBroadCastMetaDataUseCase: GetBroadCastMetaDataUseCase,
    private val updateCouponUseCase: UpdateCouponFacadeUseCase
) : BaseViewModel(dispatchers.main) {

    private val _areInputValid = MutableLiveData<Boolean>()
    val areInputValid: LiveData<Boolean>
        get() = _areInputValid

    private val _createCoupon = SingleLiveEvent<Result<Int>>()
    val createCoupon: LiveData<Result<Int>>
        get() = _createCoupon

    private val _broadCastMetadata = MutableLiveData<Result<ChatBlastSellerMetadata>>()
    val broadCastMetadata: LiveData<Result<ChatBlastSellerMetadata>> = _broadCastMetadata

    private val _updateCouponResult = MutableLiveData<Result<Boolean>>()
    val updateCouponResult: LiveData<Result<Boolean>> = _updateCouponResult

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


    fun createCoupon(
        sourceId: String,
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

    fun getBroadCastMetaData() {
        launchCatchError(
            block = {
                val result = withContext(dispatchers.io) {
                    getBroadCastMetaDataUseCase.executeOnBackground()
                }
                _broadCastMetadata.value = Success(result)
            },
            onError = {
                _broadCastMetadata.setValue(Fail(it))
            }
        )
    }

    fun updateCoupon(
        sourceId: String,
        couponInformation: CouponInformation,
        couponSettings: CouponSettings,
        couponProducts: List<CouponProduct>
    ) {
        launchCatchError(
            block = {
                val result = withContext(dispatchers.io) {
                    updateCouponUseCase.execute(
                        this,
                        sourceId,
                        couponInformation,
                        couponSettings,
                        couponProducts
                    )
                }
                _updateCouponResult.setValue(Success(result))
            },
            onError = {
                _updateCouponResult.setValue(Fail(it))
            }
        )
    }
}