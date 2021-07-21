package com.tokopedia.product_bundle.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.product_bundle.common.data.model.response.BundleInfo
import com.tokopedia.product_bundle.common.data.model.response.BundleItem
import com.tokopedia.product_bundle.multiple.presentation.model.ProductBundleDetail
import com.tokopedia.product_bundle.multiple.presentation.model.ProductBundleMaster
import java.util.*
import javax.inject.Inject

class ProductBundleViewModel @Inject constructor(
        private val dispatcher: CoroutineDispatchers
) : BaseViewModel(dispatcher.main) {

    companion object {

    }

    private var productBundleMasters = listOf<ProductBundleMaster>()

    private val getBundleInfoResultLiveData = MutableLiveData<BundleInfo>()
    val getBundleInfoResult: LiveData<BundleInfo> get() = getBundleInfoResultLiveData

    private val selectedProductBundleMasterLiveData = MutableLiveData<ProductBundleMaster>()
    val selectedProductBundleMaster: LiveData<ProductBundleMaster> get() = selectedProductBundleMasterLiveData

    private val isErrorLiveData = MutableLiveData<Boolean>()
    val isError: LiveData<Boolean> get() = isErrorLiveData

    fun getProductInfo() {

    }

    fun getProductBundleMasters(): List<ProductBundleMaster> {
        // will be replaced with product info models
        val productBundles = generateDummyBundles()
        val activeProductBundles = getActiveProductBundles(productBundles)
        productBundleMasters = productBundles.mapIndexed { index, bundleInfo ->
            val isRecommendation = index == 0
            ProductBundleMaster(
                    isRecommendation = isRecommendation,
                    bundleId = bundleInfo.bundleID,
                    bundleName = bundleInfo.name,
                    soldProductBundle = calculateSoldProductBundle(bundleInfo.originalQuota, bundleInfo.quota))
        }
        return productBundleMasters
    }

    fun getBundleInfo(bundleId: Long) {
        // simulate get bundle info api call
        var bundleInfo = BundleInfo()
        bundleInfo = when (bundleId) {
            1L -> getBundleInfoOne()
            2L -> getBundleInfoTwo()
            else -> getBundleInfoThree()
        }
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

    // TODO: remove all API calls simulation

    private fun generateDummyBundles(): List<BundleInfo> {
        return listOf(BundleInfo(name = "Paket Tahun Baru", bundleID = 1L, originalQuota = 50, quota = 30),
                BundleInfo(name = "Paket Murah Banget", bundleID = 2L, originalQuota = 40, quota = 30),
                BundleInfo(name = "Paket Misqueen", bundleID = 3L, originalQuota = 30, quota = 30))
    }

    private fun getBundleInfoOne(): BundleInfo {
        val picUrl = "https://images.tokopedia.net/img/cache/300-square/VqbcmM/2020/11/5/4950a6f4-50c2-44cd-b248-0423ba4e107b.jpg"
        val bundleItemOne = listOf(
                BundleItem(name = "iPhone XR Garansi Resmi Apple Indonesia IBOX", picURL = picUrl, bundlePrice = 6240000.0, originalPrice = 7800000.0),
                BundleItem(name = "Case iPhone XR - Original Ring Fusion Kit", picURL = picUrl, bundlePrice = 95000.0, originalPrice = 160000.0),
                BundleItem(name = "Xiaomi Powerbank 10000 mAh Type-C (Garansi Resmi) Indonesia", picURL = picUrl, bundlePrice = 215000.0, originalPrice = 250000.0)
        )
        return BundleInfo(name = "Paket Tahun Baru", bundleItems = bundleItemOne)
    }

    private fun getBundleInfoTwo(): BundleInfo {
        val picUrl = "https://images.tokopedia.net/img/cache/300-square/VqbcmM/2020/11/5/4950a6f4-50c2-44cd-b248-0423ba4e107b.jpg"
        val bundleItemTwo = listOf(
                BundleItem(name = "iPhone XR Garansi Resmi Apple Indonesia IBOX", picURL = picUrl, bundlePrice = 6240000.0, originalPrice = 7800000.0),
                BundleItem(name = "Case iPhone XR - Original Ring Fusion Kit", picURL = picUrl, bundlePrice = 95000.0, originalPrice = 160000.0)
        )
        return BundleInfo(name = "Paket Murah Banget", bundleItems = bundleItemTwo)
    }

    private fun getBundleInfoThree(): BundleInfo {
        val picUrl = "https://images.tokopedia.net/img/cache/300-square/VqbcmM/2020/11/5/4950a6f4-50c2-44cd-b248-0423ba4e107b.jpg"
        val bundleItemThree = listOf(BundleItem(name = "iPhone XR Garansi Resmi Apple Indonesia IBOX", picURL = picUrl, bundlePrice = 6240000.0, originalPrice = 7800000.0))
        return BundleInfo(name = "Paket Misqueen", bundleItems = bundleItemThree)
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
        val currentDate = Date()
        return productBundles.filter { bundleInfo ->
            val startDate = Date(bundleInfo.startTimeUnix * 1000)
            val endDate = Date(bundleInfo.stopTimeUnix * 1000)
            currentDate.after(startDate) && currentDate.before(endDate)
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