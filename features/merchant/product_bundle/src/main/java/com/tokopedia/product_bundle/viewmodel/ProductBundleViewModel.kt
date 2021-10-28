package com.tokopedia.product_bundle.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.atc_common.data.model.request.AddToCartBundleRequestParams
import com.tokopedia.atc_common.data.model.request.ProductDetail
import com.tokopedia.atc_common.domain.model.response.AddToCartBundleDataModel
import com.tokopedia.atc_common.domain.usecase.coroutine.AddToCartBundleUseCase
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.localizationchooseaddress.common.ChosenAddressRequestHelper
import com.tokopedia.product.detail.common.data.model.variant.ProductVariant
import com.tokopedia.product_bundle.common.data.constant.ProductBundleConstants.PAGE_SOURCE_CART
import com.tokopedia.product_bundle.common.data.model.request.InventoryDetail
import com.tokopedia.product_bundle.common.data.model.request.ProductData
import com.tokopedia.product_bundle.common.data.model.request.RequestData
import com.tokopedia.product_bundle.common.data.model.request.UserLocation
import com.tokopedia.product_bundle.common.data.model.response.BundleInfo
import com.tokopedia.product_bundle.common.data.model.response.BundleItem
import com.tokopedia.product_bundle.common.data.model.response.GetBundleInfoResponse
import com.tokopedia.product_bundle.common.data.model.uimodel.AddToCartDataResult
import com.tokopedia.product_bundle.common.data.model.uimodel.ProductBundleState
import com.tokopedia.product_bundle.common.usecase.GetBundleInfoConstant
import com.tokopedia.product_bundle.common.usecase.GetBundleInfoUseCase
import com.tokopedia.product_bundle.common.util.AtcVariantMapper
import com.tokopedia.product_bundle.common.util.DiscountUtil
import com.tokopedia.product_bundle.common.util.ResourceProvider
import com.tokopedia.product_bundle.multiple.presentation.model.ProductBundleDetail
import com.tokopedia.product_bundle.multiple.presentation.model.ProductBundleMaster
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.utils.lifecycle.SingleLiveEvent
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ProductBundleViewModel @Inject constructor(
    private val rscProvider: ResourceProvider,
    private val dispatchers: CoroutineDispatchers,
    private val getBundleInfoUseCase: GetBundleInfoUseCase,
    private val addToCartBundleUseCase: AddToCartBundleUseCase,
    private val chosenAddressRequestHelper: ChosenAddressRequestHelper,
    private val userSession: UserSessionInterface
) : BaseViewModel(dispatchers.main) {

    companion object {
        private const val COMMA_DELIMITER = ","
        private const val ATC_BUNDLE_QUANTITY = 1
        private const val SINGLE_PRODUCT_BUNDLE_ITEM_SIZE = 1
        private const val PRODUCT_BUNDLE_STATUS_ACTIVE = "1"
        private const val PREORDER_STATUS_ACTIVE: String = "ACTIVE"
        private const val PREORDER_TYPE_DAY: Int = 1
        private const val PREORDER_TYPE_MONTH: Int = 2
    }

    var parentProductID: Long = 0L
    var selectedBundleId: Long = 0L
    var selectedProductIds: List<String> = emptyList()
    var pageSource: String = ""

    private var productBundleMap: HashMap<ProductBundleMaster, List<ProductBundleDetail>> = HashMap()

    private val getBundleInfoResultLiveData = MutableLiveData<Result<GetBundleInfoResponse>>()
    val getBundleInfoResult: LiveData<Result<GetBundleInfoResponse>> get() = getBundleInfoResultLiveData

    private val addToCartResultLiveData = MutableLiveData<AddToCartDataResult>()
    val addToCartResult: LiveData<AddToCartDataResult> get() = addToCartResultLiveData

    private val selectedProductBundleMasterLiveData = MutableLiveData<ProductBundleMaster>()
    val selectedProductBundleMaster: LiveData<ProductBundleMaster> get() = selectedProductBundleMasterLiveData

    private val mPageState = MutableLiveData<ProductBundleState>()
    val pageState: LiveData<ProductBundleState> get() = mPageState

    private val errorMessageLiveData: SingleLiveEvent<String> = SingleLiveEvent()
    val errorMessage: LiveData<String> get() = errorMessageLiveData

    private val atcDialogMessagesLiveData: SingleLiveEvent<Pair<String,String>> = SingleLiveEvent()
    val atcDialogMessages: LiveData<Pair<String,String>> get() = atcDialogMessagesLiveData

    fun getUserId(): String {
        return userSession.userId
    }

    fun getSelectedBundle(selectedBundleId: Long?, productBundleMasters: List<ProductBundleMaster>): ProductBundleMaster? {
        var selectedProductBundleMaster: ProductBundleMaster? = null
        selectedBundleId?.let { selectedId ->
            selectedProductBundleMaster = productBundleMasters.firstOrNull { productBundleMaster ->
                productBundleMaster.bundleId == selectedId
            }
        }
        return selectedProductBundleMaster ?: productBundleMasters.firstOrNull()
    }

    fun getSelectedProductIds(productBundleDetails: List<ProductBundleDetail>): String {
        return productBundleDetails.joinToString { it.productId.toString() }
    }

    fun getSelectedProductBundleMaster(): ProductBundleMaster {
        return selectedProductBundleMaster.value ?: ProductBundleMaster()
    }

    fun getSelectedProductBundleDetails(): List<ProductBundleDetail> {
        return productBundleMap[selectedProductBundleMaster.value] ?: listOf()
    }

    fun getSelectedProductIdFromBundleDetail(productBundleDetail: ProductBundleDetail): String {
        return productBundleDetail.selectedVariantId?: productBundleDetail.productId.toString()
    }

    fun getVariantLevel(selectedProductVariant: ProductVariant): Int {
        return selectedProductVariant.variants.size
    }

    fun getVariantTitle(selectedProductVariant: ProductVariant): String {
        return selectedProductVariant.variants.joinToString { variant ->
            variant.name?:""
        }
    }

    fun isPreOrderActive(preOrderStatus: String): Boolean {
        return preOrderStatus == PREORDER_STATUS_ACTIVE
    }

    fun getPreOrderTimeUnitWording(processTypeNum: Int): String {
        return when (processTypeNum) {
            PREORDER_TYPE_DAY -> rscProvider.getPreOrderTimeUnitDay() ?: ""
            PREORDER_TYPE_MONTH -> rscProvider.getPreOrderTimeUnitMonth() ?: ""
            else -> ""
        }
    }

    fun setSelectedProductBundleMaster(productBundleMaster: ProductBundleMaster) {
        this.selectedProductBundleMasterLiveData.value = productBundleMaster
    }

    fun setSelectedVariants(selectedVariantIds: List<String>, selectedBundleMaster: ProductBundleMaster) {
        if (selectedVariantIds.isEmpty()) return
        val bundleDetails = getProductBundleDetails(selectedBundleMaster)
        bundleDetails?.forEach { bundleDetail ->
            selectedVariantIds.forEach { selectedVariantId ->
                bundleDetail.productVariant?.getChildByProductId(selectedVariantId)?.run {
                    updateProductBundleDetail(
                        selectedBundleMaster = selectedBundleMaster,
                        parentProductId = bundleDetail.productId,
                        selectedVariantId = selectedVariantId.toLongOrZero()
                    )
                }
            }
        }
    }

    fun getBundleInfo(productId: Long) {
        val chosenAddress = chosenAddressRequestHelper.getChosenAddress()
        mPageState.value = ProductBundleState.LOADING
        launchCatchError(block = {
            val result = withContext(dispatchers.io) {
                getBundleInfoUseCase.setParams(
                    squad = GetBundleInfoConstant.SQUAD_VALUE,
                    usecase = GetBundleInfoConstant.USECASE_VALUE,
                    requestData = RequestData(
                        variantDetail = true,
                        CheckCampaign = true,
                        BundleGroup = true,
                        Preorder = true,
                        inventoryDetail = InventoryDetail(
                            required = true,
                            userLocation = UserLocation(
                                addressId = chosenAddress?.addressId.orEmpty(),
                                districtID = chosenAddress?.districtId.orEmpty(),
                                postalCode = chosenAddress?.postalCode.orEmpty(),
                                latlon = chosenAddress?.geolocation.orEmpty()
                            )
                        )
                    ),
                    productData = ProductData(
                        productID = productId.toString()
                    )
                )
                getBundleInfoUseCase.executeOnBackground()
            }
            getBundleInfoResultLiveData.value = Success(result)
            mPageState.value = ProductBundleState.SUCCESS
        }, onError = {
            getBundleInfoResultLiveData.value = Fail(it)
            mPageState.value = ProductBundleState.ERROR
        })
    }

    fun addProductBundleToCart(parentProductId: Long, bundleId: Long, shopId: Long, productDetails: List<ProductDetail>) {
        launchCatchError(block = {
            val atcParams = AddToCartBundleRequestParams(
                shopId = shopId.toString(),
                bundleId = bundleId.toString(),
                bundleQty = ATC_BUNDLE_QUANTITY,
                selectedProductPdp = parentProductId.toString(),
                productDetails = productDetails
            )
            val result = withContext(dispatchers.io) {
                addToCartBundleUseCase.setParams(atcParams)
                addToCartBundleUseCase.executeOnBackground()
            }

            result.validateResponse(
                    onSuccess = {
                        addToCartResultLiveData.value = AddToCartDataResult(
                                requestParams = atcParams,
                                responseResult = result.addToCartBundleDataModel
                        )
                    },
                    onFailedWithMessages = {
                        atcDialogMessagesLiveData.value = Pair(first = it.firstOrNull() ?: "", it.lastOrNull() ?: "")
                    },
                    onFailedWithException = {
                        errorMessageLiveData.value = it.localizedMessage
                    }
            )
        }, onError = {
            // TODO: log error
            errorMessageLiveData.value = rscProvider.getErrorMessage(it)
        })
    }

    fun isProductBundleAvailable(bundleInfo: BundleInfo): Boolean {
        return bundleInfo.status == PRODUCT_BUNDLE_STATUS_ACTIVE
    }

    fun mapBundleInfoToBundleMaster(bundleInfo: BundleInfo): ProductBundleMaster {
        return ProductBundleMaster(
            shopId = bundleInfo.shopID,
            bundleId = bundleInfo.bundleID,
            bundleName = bundleInfo.name,
            quota = bundleInfo.quota,
            preOrderStatus = bundleInfo.preorder.status,
            processDay = bundleInfo.preorder.processDay,
            processTypeNum = bundleInfo.preorder.processTypeNum
        )
    }

    fun mapBundleItemsToBundleDetails(warehouseId:String, bundleItems: List<BundleItem>): List<ProductBundleDetail> {
        return bundleItems.map { bundleItem ->
            val productVariant = AtcVariantMapper.mapToProductVariant(bundleItem)
            ProductBundleDetail(
                productId = bundleItem.productID,
                productName = bundleItem.name,
                productImageUrl = bundleItem.picURL,
                productQuantity = bundleItem.quantity,
                originalPrice = bundleItem.getPreviewOriginalPrice(),
                bundlePrice = bundleItem.getPreviewBundlePrice(),
                warehouseId = warehouseId,
                discountAmount = calculateDiscountPercentage(
                    originalPrice = bundleItem.getPreviewOriginalPrice(),
                    bundlePrice = bundleItem.getPreviewBundlePrice()
                ),
                productVariant = if (productVariant.hasVariant) productVariant else null
            )
        }
    }

    fun mapBundleDetailsToProductDetails(userId: String, shopId: Int, productBundleDetails: List<ProductBundleDetail>): List<ProductDetail> {
        return productBundleDetails.map { productBundleDetail ->
            ProductDetail(
                productId = productBundleDetail.selectedVariantId?: productBundleDetail.productId.toString(),
                quantity = ATC_BUNDLE_QUANTITY,
                shopId = shopId.toString(),
                isProductParent = parentProductID == productBundleDetail.productId,
                customerId = userId,
                warehouseId = productBundleDetail.warehouseId
            )
        }
    }

    fun updateProductBundleMap(bundleMaster: ProductBundleMaster, bundleDetail: List<ProductBundleDetail>) {
        productBundleMap[bundleMaster] = bundleDetail
    }

    fun updateProductBundleDetail(selectedBundleMaster: ProductBundleMaster, parentProductId: Long, selectedVariantId: Long): ProductBundleDetail? {
        val productBundleDetails = productBundleMap[selectedBundleMaster]
        var target: ProductBundleDetail? = null
        productBundleDetails?.let { bundleDetails ->
            target = bundleDetails.firstOrNull { productBundleDetail -> productBundleDetail.productId == parentProductId }
            target?.apply {
                val selectedProductVariant = productVariant?.getChildByProductId(selectedVariantId.toString())
                this.selectedVariantId = selectedProductVariant?.productId
                this.originalPrice = selectedProductVariant?.finalMainPrice.orZero()
                this.bundlePrice = selectedProductVariant?.finalPrice.orZero()
                this.discountAmount = calculateDiscountPercentage(originalPrice, bundlePrice)
                this.selectedVariantText = getSelectedVariantText(productVariant, this.selectedVariantId ?: "")
            }
        }
        return target
    }

    fun resetBundleMap() {
        productBundleMap = HashMap()
    }

    private fun getSelectedVariantText(selectedProductVariant: ProductVariant?, selectedVariantId: String): String {
        val selectedVariantTexts = mutableListOf<String?>()
        val variantOptions = selectedProductVariant?.getOptionListString(selectedVariantId)
        variantOptions?.forEach { optionName ->
            selectedVariantTexts.add(optionName)
        }
        return selectedVariantTexts.joinToString()
    }

    fun getProductBundleMasters(): List<ProductBundleMaster> {
        return productBundleMap.keys.toList()
    }

    fun getProductBundleDetails(productBundleMaster: ProductBundleMaster): List<ProductBundleDetail>? {
        return productBundleMap[productBundleMaster]
    }

    fun isSingleProductBundle(bundleInfo: List<BundleInfo>): Boolean {
        if (bundleInfo.isEmpty()) return false
        val bundleItems = bundleInfo.first().bundleItems
        return bundleItems.size == SINGLE_PRODUCT_BUNDLE_ITEM_SIZE
    }

    fun calculateDiscountPercentage(originalPrice: Double, bundlePrice: Double): Int {
        return DiscountUtil.getDiscountPercentage(originalPrice, bundlePrice)
    }

    fun calculateTotalPrice(productBundleDetails: List<ProductBundleDetail>): Double {
        return productBundleDetails.map { it.originalPrice }.sum()
    }

    fun calculateTotalBundlePrice(productBundleDetails: List<ProductBundleDetail>): Double {
        return productBundleDetails.map { it.bundlePrice }.sum()
    }

    fun calculateTotalSaving(originalPrice: Double, bundlePrice: Double): Double {
        return originalPrice - bundlePrice
    }

    fun isUserLoggedIn(): Boolean {
        return userSession.userId.isNotBlank()
    }

    fun validateAddToCartInput(
        selectedProductBundleMaster: ProductBundleMaster,
        productBundleDetails: List<ProductBundleDetail>
    ): Boolean {
        var isAddToCartInputValid = true
        if (!isProductVariantSelectionComplete(productBundleDetails)) {
            isAddToCartInputValid = false
            errorMessageLiveData.value = rscProvider.getProductVariantNotSelected()
        } else if (
            pageSource == PAGE_SOURCE_CART &&
            selectedProductBundleMaster.bundleId == selectedBundleId &&
            variantProductNotChanged(productBundleDetails)) {
                isAddToCartInputValid = false
                addToCartResultLiveData.value = AddToCartDataResult(
                    requestParams = AddToCartBundleRequestParams(
                        bundleId = selectedBundleId.toString()
                    ),
                    responseResult = AddToCartBundleDataModel()
                )
        }
        return isAddToCartInputValid
    }

    private fun variantProductNotChanged(productBundleDetails: List<ProductBundleDetail>) =
        productBundleDetails
            .filter { it.hasVariant }
            .all { bundleDetail ->
                selectedProductIds.any {
                    bundleDetail.selectedVariantId == it
                }
            }

    private fun isProductVariantSelectionComplete(productBundleDetails: List<ProductBundleDetail>): Boolean {
        val invalidInput = productBundleDetails.find { productBundleDetail ->
            val hasVariant = productBundleDetail.productVariant?.hasVariant?: false
            hasVariant && productBundleDetail.selectedVariantId == null
        }
        return invalidInput == null
    }
}