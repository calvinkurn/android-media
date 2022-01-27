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
import com.tokopedia.vouchercreation.product.create.domain.entity.ShareMetadata
import com.tokopedia.vouchercreation.product.create.domain.usecase.GetShareMetadataFacadeUseCase
import com.tokopedia.vouchercreation.product.create.domain.usecase.create.CreateCouponFacadeUseCase
import com.tokopedia.vouchercreation.product.create.domain.usecase.update.UpdateCouponFacadeUseCase
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ProductCouponPreviewViewModel @Inject constructor(
    private val dispatchers: CoroutineDispatchers,
    private val createCouponUseCase: CreateCouponFacadeUseCase,
    private val getShareMetadataUseCase: GetShareMetadataFacadeUseCase,
    private val updateCouponUseCase: UpdateCouponFacadeUseCase
) : BaseViewModel(dispatchers.main) {

    companion object {
        private const val NUMBER_OF_MOST_SOLD_PRODUCT_TO_TAKE = 3
        private const val IMAGE_TEMPLATE_SOURCE_ID = "ZmygOT"
    }

    private val _areInputValid = MutableLiveData<Boolean>()
    val areInputValid: LiveData<Boolean>
        get() = _areInputValid

    private val _createCoupon = SingleLiveEvent<Result<Int>>()
    val createCoupon: LiveData<Result<Int>>
        get() = _createCoupon

    private val _shareMetadata = MutableLiveData<Result<ShareMetadata>>()
    val shareMetadata: LiveData<Result<ShareMetadata>> = _shareMetadata

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
        couponInformation: CouponInformation,
        couponSettings: CouponSettings,
        couponProducts: List<CouponProduct>
    ) {
        launchCatchError(
            block = {
                val result = withContext(dispatchers.io) {
                    createCouponUseCase.execute(
                        this,
                        IMAGE_TEMPLATE_SOURCE_ID,
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

    fun getShareMetaData() {
        launchCatchError(
            block = {
                val result = withContext(dispatchers.io) {
                    getShareMetadataUseCase.execute(this)
                }
                _shareMetadata.value = Success(result)
            },
            onError = {
                _shareMetadata.setValue(Fail(it))
            }
        )
    }


    fun findMostSoldProductImageUrls(couponProducts: List<CouponProduct>): ArrayList<String> {
        val mostSoldProductsImageUrls = couponProducts.sortedByDescending { it.soldCount }
            .take(NUMBER_OF_MOST_SOLD_PRODUCT_TO_TAKE)
            .map { it.imageUrl }

        val imageUrls = arrayListOf<String>()

        mostSoldProductsImageUrls.forEach { imageUrl ->
            imageUrls.add(imageUrl)
        }

        return imageUrls
    }
}