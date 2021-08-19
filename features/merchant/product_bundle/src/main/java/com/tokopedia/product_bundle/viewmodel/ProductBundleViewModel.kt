package com.tokopedia.product_bundle.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.product_bundle.common.data.model.request.ProductData
import com.tokopedia.product_bundle.common.data.model.request.RequestData
import com.tokopedia.product_bundle.common.data.model.response.BundleInfo
import com.tokopedia.product_bundle.common.data.model.response.BundleItem
import com.tokopedia.product_bundle.common.data.model.response.GetBundleInfoResponse
import com.tokopedia.product_bundle.common.usecase.GetBundleInfoConstant
import com.tokopedia.product_bundle.common.usecase.GetBundleInfoUseCase
import com.tokopedia.product_bundle.common.util.DiscountUtil
import com.tokopedia.product_bundle.multiple.presentation.model.ProductBundleDetail
import com.tokopedia.product_bundle.multiple.presentation.model.ProductBundleMaster
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ProductBundleViewModel @Inject constructor(
    private val dispatchers: CoroutineDispatchers,
    private val getBundleInfoUseCase: GetBundleInfoUseCase
) : BaseViewModel(dispatchers.main) {

    companion object {
        private const val SINGLE_PRODUCT_BUNDLE_ITEM_SIZE = 1
        private const val PRODUCT_BUNDLE_STATUS_ACTIVE = 1
        private const val PRODUCT_BUNDLE_STATUS_INACTIVE = -1
        private const val PRODUCT_BUNDLE_STATUS_UPCOMING = 2
        private const val PRODUCT_BUNDLE_STATUS_EXPIRED = -2
        private const val PRODUCT_BUNDLE_STATUS_OUT_OF_STOCK = -3
        private const val PREORDER_TYPE_DAY: Int = 1
        private const val PREORDER_TYPE_MONTH: Int = 2
    }

    private var productBundleMap: HashMap<ProductBundleMaster, List<ProductBundleDetail>> = HashMap()

    private val getBundleInfoResultLiveData = MutableLiveData<Result<GetBundleInfoResponse>>()
    val getBundleInfoResult: LiveData<Result<GetBundleInfoResponse>> get() = getBundleInfoResultLiveData

    private val selectedProductBundleMasterLiveData = MutableLiveData<ProductBundleMaster>()
    val selectedProductBundleMaster: LiveData<ProductBundleMaster> get() = selectedProductBundleMasterLiveData

    private val isErrorLiveData = MutableLiveData<Boolean>()
    val isError: LiveData<Boolean> get() = isErrorLiveData

    fun getBundleInfo(productId: Long) {
        launchCatchError(block = {
            val result = withContext(dispatchers.io) {
                getBundleInfoUseCase.setParams(
                    squad = GetBundleInfoConstant.SQUAD_VALUE,
                    usecase = GetBundleInfoConstant.USECASE_VALUE,
                    requestData = RequestData(
                        variantDetail = true,
                        CheckCampaign = true,
                        BundleGroup = true,
                        Preorder = true
                    ),
                    productData = ProductData(productID = productId.toString())
                )
                getBundleInfoUseCase.executeOnBackground()
            }
            getBundleInfoResultLiveData.value = Success(result)
        }, onError = {
            getBundleInfoResultLiveData.value = Fail(it)
        })
    }

    fun getDefaultProductBundleSelection(productBundleMasters: List<ProductBundleMaster>): ProductBundleMaster? {
        return productBundleMasters.firstOrNull()
    }

    fun setSelectedProductBundleMaster(productBundleMaster: ProductBundleMaster) {
        this.selectedProductBundleMasterLiveData.value = productBundleMaster
    }

    fun getPreOrderProcessDay(): Long {
        return this.selectedProductBundleMasterLiveData.value?.processDay ?: 0L
    }

    fun getTimeUnitWording(processTypeNum: Int): String {
        return ""
    }

    fun addProductBundleToCart() {

    }

    fun mapBundleInfoToBundleMaster(bundleInfo: BundleInfo): ProductBundleMaster {
        return ProductBundleMaster(
            bundleId = bundleInfo.bundleID,
            bundleName = bundleInfo.name,
            processDay = bundleInfo.preorder.processDay
        )
    }

    fun mapBundleItemsToBundleDetail(bundleItems: List<BundleItem>): List<ProductBundleDetail> {
        return bundleItems.map { bundleItem ->
            ProductBundleDetail(
                productImageUrl = bundleItem.picURL,
                productName = bundleItem.name,
                originalPrice = bundleItem.originalPrice,
                bundlePrice = bundleItem.bundlePrice,
                discountAmount = calculateDiscountPercentage(
                    originalPrice = bundleItem.originalPrice,
                    bundlePrice = bundleItem.bundlePrice
                )
            )
        }
    }

    fun updateProductBundleMap(bundleMaster: ProductBundleMaster, bundleDetail: List<ProductBundleDetail>) {
        productBundleMap[bundleMaster] = bundleDetail
    }

    fun getProductBundleMasters(): List<ProductBundleMaster> {
        return productBundleMap.keys.toList()
    }

    fun getProductBundleDetail(productBundleMaster: ProductBundleMaster): List<ProductBundleDetail>? {
        return productBundleMap[productBundleMaster]
    }

    fun isSingleProductBundle(bundleInfo: List<BundleInfo>): Boolean {
        if (bundleInfo.isEmpty()) return false
        val bundleItems = bundleInfo.first().bundleItems
        return bundleItems.size == SINGLE_PRODUCT_BUNDLE_ITEM_SIZE
    }

    private fun calculateSoldProductBundle(originalQuota: Int, quota: Int): Int {
        return originalQuota - quota
    }

    fun calculateDiscountPercentage(originalPrice: Double, bundlePrice: Double): Int {
        return DiscountUtil.getDiscountPercentage(originalPrice, bundlePrice)
    }

    fun calculateTotalPrice(productBundleItems: List<BundleItem>): Double {
        return productBundleItems.map { it.originalPrice }.sum()
    }

    fun calculateTotalBundlePrice(productBundleItems: List<BundleItem>): Double {
        return productBundleItems.map { it.bundlePrice }.sum()
    }

    fun calculateTotalSaving(originalPrice: Double, bundlePrice: Double): Double {
        return originalPrice - bundlePrice
    }
}