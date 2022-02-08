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
import com.tokopedia.vouchercreation.common.consts.GqlQueryConstant
import com.tokopedia.vouchercreation.common.consts.ImageGeneratorConstant
import com.tokopedia.vouchercreation.common.domain.usecase.InitiateVoucherUseCase
import com.tokopedia.vouchercreation.product.create.data.mapper.CouponPreviewMapper
import com.tokopedia.vouchercreation.product.create.domain.entity.Coupon
import com.tokopedia.vouchercreation.product.create.domain.entity.CouponInformation
import com.tokopedia.vouchercreation.product.create.domain.entity.CouponProduct
import com.tokopedia.vouchercreation.product.create.domain.entity.CouponSettings
import com.tokopedia.vouchercreation.product.create.domain.usecase.GetCouponDetailUseCase
import com.tokopedia.vouchercreation.product.create.domain.usecase.create.CreateCouponFacadeUseCase
import com.tokopedia.vouchercreation.product.create.domain.usecase.update.UpdateCouponFacadeUseCase
import com.tokopedia.vouchercreation.product.create.view.fragment.ProductCouponPreviewFragment
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ProductCouponPreviewViewModel @Inject constructor(
    private val dispatchers: CoroutineDispatchers,
    private val createCouponUseCase: CreateCouponFacadeUseCase,
    private val updateCouponUseCase: UpdateCouponFacadeUseCase,
    private val initiateVoucherUseCase: InitiateVoucherUseCase,
    private val getCouponDetailUseCase: GetCouponDetailUseCase,
    private val couponPreviewMapper: CouponPreviewMapper
) : BaseViewModel(dispatchers.main) {

    companion object {
        private const val NUMBER_OF_MOST_SOLD_PRODUCT_TO_TAKE = 3
    }

    private val _areInputValid = SingleLiveEvent<Boolean>()
    val areInputValid: LiveData<Boolean>
        get() = _areInputValid

    private val _createCoupon = SingleLiveEvent<Result<Int>>()
    val createCoupon: LiveData<Result<Int>>
        get() = _createCoupon

    private val _updateCouponResult = SingleLiveEvent<Result<Boolean>>()
    val updateCouponResult: LiveData<Result<Boolean>>
        get() = _updateCouponResult

    private val _maxAllowedProductCount = MutableLiveData<Result<Int>>()
    val maxAllowedProductCount: LiveData<Result<Int>>
        get() = _maxAllowedProductCount

    private val _couponDetail = SingleLiveEvent<Result<Coupon>>()
    val couponDetail: LiveData<Result<Coupon>>
        get() = _couponDetail

    fun validateCoupon(
        pageMode : ProductCouponPreviewFragment.Mode,
        couponSettings: CouponSettings?,
        couponInformation: CouponInformation?,
        couponProducts: List<CouponProduct>
    ) {
        if (isUpdateMode(pageMode) || isDuplicateMode(pageMode)) {
            _areInputValid.value = true
            return
        }

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
                        ImageGeneratorConstant.IMAGE_TEMPLATE_COUPON_PRODUCT_SOURCE_ID,
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

    fun getMaxAllowedProducts(mode : ProductCouponPreviewFragment.Mode) {
        val isUpdateMode = mode == ProductCouponPreviewFragment.Mode.UPDATE

        launchCatchError(block = {
            initiateVoucherUseCase.query = GqlQueryConstant.GET_INIT_VOUCHER_ELIGIBILITY_QUERY
            initiateVoucherUseCase.params = InitiateVoucherUseCase.createRequestParam(isUpdateMode)
            val result = withContext(dispatchers.io) {
                initiateVoucherUseCase.executeOnBackground()
            }
            _maxAllowedProductCount.value = Success(result.maxProducts)
        }, onError = {
            _maxAllowedProductCount.value = Fail(it)
        })
    }

    fun isCouponInformationValid(couponInformation: CouponInformation): Boolean {
        if (couponInformation.target == CouponInformation.Target.NOT_SELECTED) {
            return false
        }

        if (couponInformation.target == CouponInformation.Target.PRIVATE && couponInformation.code.isEmpty()) {
            return false
        }

        if (couponInformation.name.isEmpty()) {
            return false
        }

        return true
    }


    fun getCouponDetail(couponId : Long) {
        launchCatchError(
            block = {
                val result = withContext(dispatchers.io) {
                    getCouponDetailUseCase.params = GetCouponDetailUseCase.createRequestParam(couponId)
                    val coupon = getCouponDetailUseCase.executeOnBackground()
                    couponPreviewMapper.map(coupon)
                }
                _couponDetail.value = Success(result)
            },
            onError = {
                _couponDetail.value = Fail(it)
            }
        )
    }

    fun isCreateMode(mode : ProductCouponPreviewFragment.Mode): Boolean {
        return mode == ProductCouponPreviewFragment.Mode.CREATE
    }

    fun isUpdateMode(mode : ProductCouponPreviewFragment.Mode): Boolean {
        return mode == ProductCouponPreviewFragment.Mode.UPDATE
    }

    fun isDuplicateMode(mode : ProductCouponPreviewFragment.Mode): Boolean {
        return mode == ProductCouponPreviewFragment.Mode.DUPLICATE
    }
}