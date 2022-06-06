package com.tokopedia.vouchercreation.product.list.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.view.thousandFormatted
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.usecase.launch_cache_error.launchCatchError
import com.tokopedia.vouchercreation.common.extension.splitByThousand
import com.tokopedia.vouchercreation.common.utils.ResourceProvider
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
import java.util.*
import javax.inject.Inject

class ManageProductViewModel @Inject constructor(
    private val dispatchers: CoroutineDispatchers,
    private val getProductListUseCase: GetProductListUseCase,
    private val validateVoucherUseCase: ValidateVoucherUseCase,
    private val resourceProvider: ResourceProvider
) : BaseViewModel(dispatchers.main) {

    companion object {
        private const val PRODUCT_SOLD_COUNT_LAST_DIGIT_TO_DISPLAY = 1
    }

    // PRODUCT SELECTIONS
    var isSelectAllMode = true
    var isSingleClick = false

    private var isViewing = false
    private var isEditing = false
    private var maxProductLimit = 0
    private var couponSettings: CouponSettings? = null
    private var selectedProductIds = ArrayList<ProductId>()
    private var productUiModels: List<ProductUiModel> = listOf()
    private var warehouseLocationId: String = ""

    private val getProductListResultLiveData = MutableLiveData<Result<ProductListResponse>>()
    val getProductListResult: LiveData<Result<ProductListResponse>> get() = getProductListResultLiveData

    private val validateVoucherResultLiveData = MutableLiveData<Result<VoucherValidationPartialResponse>>()
    val validateVoucherResult: LiveData<Result<VoucherValidationPartialResponse>> get() = validateVoucherResultLiveData

    val selectedProductListLiveData = MutableLiveData<List<ProductUiModel>>(listOf())

    fun getProductList(
            pageSize: Int = 0,
            shopId: String? = null,
            selectedProductIds: List<String>? = null
    ) {
        launchCatchError(block = {
            val result = withContext(dispatchers.io) {
                val params = GetProductListUseCase.createRequestParams(
                        pageSize = pageSize,
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
            if(isViewing) {
                productUiModel.isError = false
                productUiModel.errorMessage = ""
            }
            productUiModel.variants.forEach { variantUiModel ->
                variantUiModel.isViewing = isViewing
                variantUiModel.isEditing = isEditing
                if(isViewing) {
                    variantUiModel.isError = false
                    variantUiModel.errorMessage = ""
                }
            }
        }
        return mutableProductList.toList()
    }

    fun filterSelectedProductVariant(productList: List<ProductUiModel>): List<ProductUiModel> {
        val mutableProductList = productList.toMutableList()
        mutableProductList.forEach { productUiModel ->
            productUiModel.variants = productUiModel.variants.filter { variantUiModel ->
                variantUiModel.isSelected
            }
        }
        return mutableProductList.toList()
    }

    fun mapProductDataToProductUiModel(
        isViewing: Boolean,
        isEditing: Boolean,
        productDataList: List<ProductData>
    ): List<ProductUiModel> {
        return productDataList.map { productData ->
            ProductUiModel(
                isViewing = isViewing,
                isEditing = isEditing,
                isSelected = false,
                imageUrl = productData.pictures.first().urlThumbnail,
                id = productData.id,
                productName = productData.name,
                sku = getFormattedSku(productData.sku),
                price = getFormattedProductPrice(productData.price.max),
                sold = productData.txStats.sold,
                soldNStock = getFormattedStatisticText(productData.txStats.sold, productData.stock),
                hasVariant = productData.isVariant
            )
        }
    }

    fun applyValidationResult(
        isEditing: Boolean,
        productList: List<ProductUiModel>,
        validationResults: List<VoucherValidationPartialProduct>
    ): List<ProductUiModel> {
        val mutableProductList = productList.toMutableList()
        validationResults.forEach { validationResult ->
            val productUiModel = mutableProductList.first {
                it.id == validationResult.parentProductId
            }
            if(isEditing) {
                productUiModel.isError = !validationResult.isEligible
                productUiModel.errorMessage = validationResult.reason
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
            val variantUiModel = VariantUiModel(
                    isViewing = isViewing,
                    isEditing = isEditing,
                    variantId = data.productId,
                    variantName = data.productName,
                    sku = getFormattedSku(data.sku),
                    price = data.price.toString(),
                    priceTxt = data.priceFormat,
                    soldNStock = getFormattedStatisticText(sold, data.stock),
            )
            if(isEditing){
               variantUiModel.copy(
                   isError = !data.is_eligible,
                   errorMessage = data.reason
               )
            } else {
                variantUiModel
            }
        }
    }

    fun setVariantSelection(productList: List<ProductUiModel>, selectedProductIds: List<ProductId>, isViewing: Boolean): MutableList<ProductUiModel> {
        val mutableProductList = productList.toMutableList()
        selectedProductIds.forEach { productId ->
            val productUiModel = mutableProductList.firstOrNull() { productUiModel ->
                productUiModel.id == productId.parentProductId.toString()
            }
            val mutableVariantList = productUiModel?.variants
            mutableVariantList?.run {
                productId.childProductId.forEach { variantId ->
                    val variantUiModel = mutableVariantList.firstOrNull { variantUiModel ->
                        variantUiModel.variantId == variantId.toString()
                    }
                    variantUiModel?.isSelected = true
                }
            }
            val selectedVariants = mutableVariantList?.filter { it.isSelected }
            selectedVariants?.forEach { it.isSelected  = false }
            productUiModel?.variants = selectedVariants ?: listOf()
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

    fun setWarehouseLocationId(warehouseLocationId: String) {
        this.warehouseLocationId = warehouseLocationId
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

    fun getWarehouseLocationId(): String {
        return warehouseLocationId
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

    fun resetProductUiModelState(selectedProducts: List<ProductUiModel>): List<ProductUiModel> {
        val mutableSelectedProducts = selectedProducts.toMutableList()
        mutableSelectedProducts.forEach {
            it.isSelected = false
            it.isVariantHeaderExpanded = false
            it.isEditing = true
            it.isViewing = false
            it.variants.forEach { variantUiModel ->
                variantUiModel.isSelected = false
            }
        }
        return mutableSelectedProducts.toList()
    }

    private fun getFormattedSku(sku: String): String {
        val skuTemplate = resourceProvider.getFormattedSku()
        return skuTemplate.format(sku)
    }

    private fun getFormattedStatisticText(sold: Int, stock: Int): String {
        val formattedSoldCount = sold.thousandFormatted(PRODUCT_SOLD_COUNT_LAST_DIGIT_TO_DISPLAY)
        val statisticTemplate = resourceProvider.getFormattedProductStatistic()
        return statisticTemplate.format(formattedSoldCount, stock.splitByThousand(Locale.ENGLISH))
    }

    private fun getFormattedProductPrice(productPrice: Long): String {
        return String.format(resourceProvider.getProductPrice(), productPrice.splitByThousand())
    }
}