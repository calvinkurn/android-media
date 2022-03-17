package com.tokopedia.vouchercreation.product.preview

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.utils.lifecycle.SingleLiveEvent
import com.tokopedia.vouchercreation.common.consts.GqlQueryConstant
import com.tokopedia.vouchercreation.common.consts.ImageGeneratorConstant
import com.tokopedia.vouchercreation.product.create.data.response.ProductId
import com.tokopedia.vouchercreation.product.create.domain.entity.CouponInformation
import com.tokopedia.vouchercreation.product.create.domain.entity.CouponProduct
import com.tokopedia.vouchercreation.product.create.domain.entity.CouponSettings
import com.tokopedia.vouchercreation.product.create.domain.entity.CouponWithMetadata
import com.tokopedia.vouchercreation.product.create.domain.usecase.GetCouponFacadeUseCase
import com.tokopedia.vouchercreation.product.create.domain.usecase.InitiateCouponUseCase
import com.tokopedia.vouchercreation.product.create.domain.usecase.create.CreateCouponFacadeUseCase
import com.tokopedia.vouchercreation.product.create.domain.usecase.update.UpdateCouponFacadeUseCase
import com.tokopedia.vouchercreation.product.list.view.model.ProductUiModel
import com.tokopedia.vouchercreation.product.list.view.model.VariantUiModel
import kotlinx.coroutines.withContext
import javax.inject.Inject

class CouponPreviewViewModel @Inject constructor(
    private val dispatchers: CoroutineDispatchers,
    private val initiateCouponUseCase: InitiateCouponUseCase,
    private val createCouponUseCase: CreateCouponFacadeUseCase,
    private val updateCouponUseCase: UpdateCouponFacadeUseCase,
    private val getCouponDetailUseCase: GetCouponFacadeUseCase
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

    private val _couponDetail = SingleLiveEvent<Result<CouponWithMetadata>>()
    val couponDetail: LiveData<Result<CouponWithMetadata>>
        get() = _couponDetail

    private val _couponCreationEligibility = MutableLiveData<Result<Int>>()
    val couponCreationEligibility: LiveData<Result<Int>>
        get() = _couponCreationEligibility

    var selectedWarehouseId:String = ""

    fun validateCoupon(
        pageMode : CouponPreviewFragment.Mode,
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
        isCreateMode: Boolean,
        couponInformation: CouponInformation,
        couponSettings: CouponSettings,
        couponProducts: List<CouponProduct>
    ) {
        launchCatchError(
            block = {
                val result = withContext(dispatchers.io) {
                    createCouponUseCase.execute(
                        this,
                        isCreateMode,
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

    fun checkCouponCreationEligibility() {
        launchCatchError(block = {
            initiateCouponUseCase.query = GqlQueryConstant.INITIATE_COUPON_PRODUCT_QUERY
            initiateCouponUseCase.params = InitiateCouponUseCase.createRequestParam(
                isUpdate = false,
                isToCreateNewCoupon = true
            )
            val result = withContext(dispatchers.io) {
                initiateCouponUseCase.executeOnBackground()
            }
            _couponCreationEligibility.value = Success(result.maxProducts)
        }, onError = {
            _couponCreationEligibility.value = Fail(it)
        })
    }

    fun getCouponDetail(couponId: Long, pageMode: CouponPreviewFragment.Mode) {
        launchCatchError(
            block = {
                val isToCreateNewCoupon = isCreateMode(pageMode) || isDuplicateMode(pageMode)
                val result = withContext(dispatchers.io) {
                    getCouponDetailUseCase.execute(this, couponId, isToCreateNewCoupon)
                }
                _couponDetail.value = Success(result)
            },
            onError = {
                _couponDetail.value = Fail(it)
            }
        )
    }

    fun isCreateMode(mode : CouponPreviewFragment.Mode): Boolean {
        return mode == CouponPreviewFragment.Mode.CREATE
    }

    fun isUpdateMode(mode : CouponPreviewFragment.Mode): Boolean {
        return mode == CouponPreviewFragment.Mode.UPDATE
    }

    fun isDuplicateMode(mode : CouponPreviewFragment.Mode): Boolean {
        return mode == CouponPreviewFragment.Mode.DUPLICATE
    }

    fun mapSelectedProductsToCouponProductData(selectedProducts: List<ProductUiModel>): List<CouponProduct> {
        val couponProductData = mutableListOf<CouponProduct>()
        selectedProducts.forEach { selectedProduct ->
            val isParentProductSelected = selectedProduct.isSelected
            if (isParentProductSelected) {
                val selectedVariants = selectedProduct.variants.filter { it.isSelected }
                if (selectedVariants.isNotEmpty()) {
                    selectedVariants.forEach { variant ->
                        couponProductData.add(CouponProduct(
                                id = variant.variantId,
                                imageUrl = selectedProduct.imageUrl,
                                soldCount = selectedProduct.sold
                        ))
                    }
                } else {
                    couponProductData.add(CouponProduct(
                            id = selectedProduct.id,
                            imageUrl = selectedProduct.imageUrl,
                            soldCount = selectedProduct.sold
                    ))
                }
            }
        }
        return couponProductData.toList()
    }

    fun mapSelectedProductIdsToProductUiModels(selectedProductIds: List<ProductId>): List<ProductUiModel> {
        return selectedProductIds.map { productId ->
            ProductUiModel(
                    id = productId.parentProductId.toString(),
                    variants = productId.childProductId.map { variantId ->
                        VariantUiModel(
                                isSelected = true,
                                variantId = variantId.toString()
                        )
                    }
            )
        }
    }
}