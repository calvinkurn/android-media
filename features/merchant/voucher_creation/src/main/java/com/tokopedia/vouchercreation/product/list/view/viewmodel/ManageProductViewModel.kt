package com.tokopedia.vouchercreation.product.list.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.usecase.launch_cache_error.launchCatchError
import com.tokopedia.vouchercreation.product.create.data.response.ProductId
import com.tokopedia.vouchercreation.product.create.domain.entity.CouponSettings
import com.tokopedia.vouchercreation.product.create.domain.entity.CouponType
import com.tokopedia.vouchercreation.product.create.domain.entity.DiscountType
import com.tokopedia.vouchercreation.product.list.domain.model.response.*
import com.tokopedia.vouchercreation.product.list.domain.usecase.GetProductListUseCase
import com.tokopedia.vouchercreation.product.list.domain.usecase.ValidateVoucherUseCase
import com.tokopedia.vouchercreation.product.list.view.model.ProductUiModel
import com.tokopedia.vouchercreation.product.list.view.model.VariantUiModel
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ManageProductViewModel @Inject constructor(
        private val dispatchers: CoroutineDispatchers,
        private val getProductListUseCase: GetProductListUseCase,
        private val validateVoucherUseCase: ValidateVoucherUseCase,
) : BaseViewModel(dispatchers.main) {

    // PRODUCT SELECTIONS
    var isSelectAllMode = true

    private var isViewing = false
    private var isEditing = false
    private var maxProductLimit = 0
    private var couponSettings: CouponSettings? = null
    private var selectedProductIds = ArrayList<ProductId>()
    private var productUiModels: List<ProductUiModel> = listOf()

    private val getProductListResultLiveData = MutableLiveData<Result<ProductListResponse>>()
    val getProductListResult: LiveData<Result<ProductListResponse>> get() = getProductListResultLiveData

    private val validateVoucherResultLiveData = MutableLiveData<Result<VoucherValidationPartialResponse>>()
    val validateVoucherResult: LiveData<Result<VoucherValidationPartialResponse>> get() = validateVoucherResultLiveData

    val selectedProductListLiveData = MutableLiveData<List<ProductUiModel>>(listOf())

    fun getProductList(
            shopId: String? = null,
            selectedProductIds: List<String>? = null
    ) {
        launchCatchError(block = {
            val result = withContext(dispatchers.io) {
                val params = GetProductListUseCase.createRequestParams(
                        shopId = shopId,
                        productIds = selectedProductIds
                )
                getProductListUseCase.setRequestParams(params = params.parameters)
                getProductListUseCase.executeOnBackground()
            }
            getProductListResultLiveData.value = Success(result)
        }, onError = {
            getProductListResultLiveData.value = Fail(it)
        })
    }

    fun validateProductList(benefitType: String,
                            couponType: String,
                            benefitIdr: Int,
                            benefitMax: Int,
                            benefitPercent: Int,
                            minPurchase: Int,
                            productIds: List<String>) {
        launchCatchError(block = {
            val result = withContext(dispatchers.io) {
                val params = ValidateVoucherUseCase.createRequestParams(
                        benefitType = benefitType,
                        couponType = couponType,
                        benefitIdr = benefitIdr,
                        benefitMax = benefitMax,
                        benefitPercent = benefitPercent,
                        minPurchase = minPurchase,
                        productIds = productIds
                )
                validateVoucherUseCase.setRequestParams(params = params.parameters)
                validateVoucherUseCase.executeOnBackground()
            }
            validateVoucherResultLiveData.value = Success(result)
        }, onError = {
            validateVoucherResultLiveData.value = Fail(it)
        })
    }

    fun updateProductUiModelsDisplayMode(
            isViewing: Boolean,
            isEditing: Boolean,
            productList: List<ProductUiModel>
    ): List<ProductUiModel> {
        val mutableProductList = productList.toMutableList()
        mutableProductList.forEach { productUiModel ->
            productUiModel.isViewing = isViewing
            productUiModel.isEditing = isEditing
            productUiModel.isVariantHeaderExpanded = false
            productUiModel.isError = false
            productUiModel.errorMessage = ""
            productUiModel.variants.forEach { variantUiModel ->
                variantUiModel.isError = false
                variantUiModel.errorMessage = ""
            }
        }
        return mutableProductList.toList()
    }

    fun mapProductDataToProductUiModel(isViewing: Boolean, isEditing: Boolean, productDataList: List<ProductData>): List<ProductUiModel> {
        return productDataList.map { productData ->
            // TODO: implement proper string formatting
            ProductUiModel(
                    isViewing = isViewing,
                    isEditing = isEditing,
                    isSelected = true,
                    imageUrl = productData.pictures.first().urlThumbnail,
                    id = productData.id,
                    productName = productData.name,
                    sku = "SKU : " + productData.sku,
                    price = "Rp " + productData.price.max.toString(),
                    sold = productData.txStats.sold,
                    soldNStock = "Terjual " + productData.txStats.sold + " | " + "Stok " + productData.stock.toString(),
                    hasVariant = productData.isVariant
            )
        }
    }

    fun applyValidationResult(productList: List<ProductUiModel>,
                              validationResults: List<VoucherValidationPartialProduct>): List<ProductUiModel> {
        val mutableProductList = productList.toMutableList()
        validationResults.forEach { validationResult ->
            val productUiModel = mutableProductList.first {
                it.id == validationResult.parentProductId.toString()
            }
            productUiModel.hasVariant = validationResult.isVariant
            productUiModel.variants = mapVariantDataToUiModel(
                    isViewing = productUiModel.isViewing,
                    isEditing = productUiModel.isEditing,
                    variantValidationData = validationResult.variants,
                    sold = productUiModel.sold
            )
        }
        return mutableProductList.toList()
    }

    private fun mapVariantDataToUiModel(
            isViewing: Boolean,
            isEditing: Boolean,
            variantValidationData: List<VariantValidationData>,
            sold: Int
    ): List<VariantUiModel> {
        return variantValidationData.map { data ->
            VariantUiModel(
                    isViewing = isViewing,
                    isEditing = isEditing,
                    variantId = data.productId.toString(),
                    variantName = data.productName,
                    sku = "SKU : " + data.sku,
                    price = data.price.toString(),
                    priceTxt = data.priceFormat,
                    soldNStock = "Terjual " + sold.toString() + " | " + "Stok " + data.stock.toString()
            )
        }
    }

    fun setVariantSelection(productList: List<ProductUiModel>,
                            selectedProductIds: List<ProductId>): MutableList<ProductUiModel> {
        val mutableProductList = productList.toMutableList()
        selectedProductIds.forEach { productId ->
            val productUiModel = mutableProductList.firstOrNull() { productUiModel ->
                productUiModel.id == productId.parentProductId.toString()
            }
            val mutableVariantList = productUiModel?.variants
            mutableVariantList?.run {
                productId.childProductId.forEach { variantId ->
                    val variantUiModel = mutableVariantList.first { variantUiModel ->
                        variantUiModel.variantId == variantId.toString()
                    }
                    variantUiModel.isSelected = true
                }
            }
        }
        return mutableProductList
    }

    fun setProductUiModels(productUiModels: List<ProductUiModel>) {
        this.productUiModels = productUiModels
    }

    fun getProductUiModels(): List<ProductUiModel> {
        return productUiModels
    }

    fun getIdsFromProductList(productList: List<ProductUiModel>): List<String> {
        return productList.map { productUiModel ->
            productUiModel.id
        }
    }

    fun setIsViewing(isViewing: Boolean) {
        this.isViewing = isViewing
    }

    fun setIsEditing(isEditing: Boolean) {
        this.isEditing = isEditing
    }

    fun setMaxProductLimit(maxProductLimit: Int) {
        this.maxProductLimit = maxProductLimit
    }

    fun setCouponSettings(couponSettings: CouponSettings?) {
        this.couponSettings = couponSettings
    }

    fun setSelectedProductIds(selectedProductIds: ArrayList<ProductId>) {
        this.selectedProductIds = selectedProductIds
    }

    fun setSetSelectedProducts(productList: List<ProductUiModel>) {
        this.selectedProductListLiveData.value = productList
    }

    fun getIsViewing(): Boolean {
        return isViewing
    }

    fun getIsEditing(): Boolean {
        return isEditing
    }

    fun getMaxProductLimit(): Int {
        return maxProductLimit
    }

    fun getSelectedProductIds(): List<ProductId> {
        return selectedProductIds.toList()
    }

    fun getSelectedParentProductIds(): List<String> {
        return selectedProductIds.map { it.parentProductId.toString() }
    }

    fun getCouponSettings(): CouponSettings? {
        return couponSettings
    }

    fun getBenefitType(couponSettings: CouponSettings): String {
        return when {
            couponSettings.type == CouponType.FREE_SHIPPING -> AddProductViewModel.BENEFIT_TYPE_IDR
            couponSettings.type == CouponType.CASHBACK && couponSettings.discountType == DiscountType.NOMINAL -> AddProductViewModel.BENEFIT_TYPE_IDR
            couponSettings.type == CouponType.CASHBACK && couponSettings.discountType == DiscountType.PERCENTAGE -> AddProductViewModel.BENEFIT_TYPE_PERCENT
            else -> AddProductViewModel.BENEFIT_TYPE_IDR
        }
    }

    fun getCouponType(couponSettings: CouponSettings): String {
        return when (couponSettings.type) {
            CouponType.NONE -> AddProductViewModel.EMPTY_STRING
            CouponType.CASHBACK -> AddProductViewModel.COUPON_TYPE_CASHBACK
            CouponType.FREE_SHIPPING -> AddProductViewModel.COUPON_TYPE_SHIPPING
        }
    }

    fun getBenefitIdr(couponSettings: CouponSettings): Int {
        return couponSettings.discountAmount
    }

    fun getBenefitMax(couponSettings: CouponSettings): Int {
        return couponSettings.maxDiscount
    }

    fun getBenefitPercent(couponSettings: CouponSettings): Int {
        return couponSettings.discountPercentage
    }

    fun getMinimumPurchase(couponSettings: CouponSettings): Int {
        return couponSettings.minimumPurchase
    }

    fun isMaxProductLimitReached(selectedProductsSize: Int): Boolean {
        return selectedProductsSize > maxProductLimit
    }

    fun resetProductUiModelState(selectedProducts: List<ProductUiModel>): List<ProductUiModel> {
        val mutableSelectedProducts = selectedProducts.toMutableList()
        mutableSelectedProducts.forEach {
            it.isVariantHeaderExpanded = false
            it.isEditing = true
            it.isViewing = false
        }
        return mutableSelectedProducts.toList()
    }
}