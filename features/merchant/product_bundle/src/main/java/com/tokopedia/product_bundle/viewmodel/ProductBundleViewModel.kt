package com.tokopedia.product_bundle.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.product_bundle.common.data.model.response.BundleInfo
import com.tokopedia.product_bundle.common.data.model.response.BundleItem
import com.tokopedia.product_bundle.common.usecase.GetBundleInfoUseCase
import com.tokopedia.product_bundle.multiple.presentation.model.ProductBundleDetail
import com.tokopedia.product_bundle.multiple.presentation.model.ProductBundleMaster
import javax.inject.Inject

class ProductBundleViewModel @Inject constructor(
    private val dispatcher: CoroutineDispatchers,
    private val getBundleInfoUseCase: GetBundleInfoUseCase
) : BaseViewModel(dispatcher.main) {

    companion object {
        private const val PRODUCT_BUNDLE_STATUS_ACTIVE = 1
        private const val PRODUCT_BUNDLE_STATUS_INACTIVE = -1
        private const val PRODUCT_BUNDLE_STATUS_UPCOMING = 2
        private const val PRODUCT_BUNDLE_STATUS_EXPIRED = -2
        private const val PRODUCT_BUNDLE_STATUS_OUT_OF_STOCK = -3
    }

    private var productBundleMasters = listOf<ProductBundleMaster>()

    private val getBundleInfoResultLiveData = MutableLiveData<BundleInfo>()
    val getBundleInfoResult: LiveData<BundleInfo> get() = getBundleInfoResultLiveData

    private val selectedProductBundleMasterLiveData = MutableLiveData<ProductBundleMaster>()
    val selectedProductBundleMaster: LiveData<ProductBundleMaster> get() = selectedProductBundleMasterLiveData

    private val isErrorLiveData = MutableLiveData<Boolean>()
    val isError: LiveData<Boolean> get() = isErrorLiveData

    fun getProductBundleMasters(): List<ProductBundleMaster> {
//        // will be replaced with product info models
//        val productBundles = generateDummyBundles()
//        val activeProductBundles = getActiveProductBundles(productBundles)
//        productBundleMasters = productBundles.mapIndexed { index, bundleInfo ->
//            val isRecommendation = index == 0
//            ProductBundleMaster(
//                isRecommendation = isRecommendation,
//                bundleId = bundleInfo.bundleID,
//                bundleName = bundleInfo.name,
//                soldProductBundle = calculateSoldProductBundle(
//                    bundleInfo.originalQuota,
//                    bundleInfo.quota
//                )
//            )
//        }
        return productBundleMasters
    }

    fun getBundleInfo(productId: Long) {
        var bundleInfo = BundleInfo()
        getBundleInfoResultLiveData.value = bundleInfo
    }

    fun getRecommendedProductBundleId(productBundleMasters: List<ProductBundleMaster>): Long {
        return productBundleMasters.first().bundleId
    }

    fun setSelectedProductBundleMaster(productBundleMaster: ProductBundleMaster) {
        this.selectedProductBundleMasterLiveData.value = productBundleMaster
    }

    fun getSoldProductBundle(): Int {
        return this.selectedProductBundleMasterLiveData.value?.soldProductBundle ?: 0
    }
    fun addProductBundleToCart() {
        // simulate error response from the API
        isErrorLiveData.value = true
    }

    fun mapProductBundleItemsToProductBundleDetail(bundleItems: List<BundleItem>): List<ProductBundleDetail> {
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

    private fun getActiveProductBundles(productBundles: List<BundleInfo>): List<BundleInfo> {
        return productBundles.filter { bundleInfo ->
            bundleInfo.status.toIntOrZero() == PRODUCT_BUNDLE_STATUS_ACTIVE
        }
    }

    private fun calculateSoldProductBundle(originalQuota: Int, quota: Int): Int {
        return originalQuota - quota
    }

    fun calculateDiscountPercentage(originalPrice: Double, bundlePrice: Double): Double {
        return ((originalPrice - bundlePrice) * 100 / originalPrice)
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

    fun isProductBundleSoldOut() {

    }
}