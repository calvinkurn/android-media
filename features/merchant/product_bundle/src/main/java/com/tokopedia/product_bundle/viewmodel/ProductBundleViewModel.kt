package com.tokopedia.product_bundle.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.product_bundle.common.data.model.response.BundleInfo
import com.tokopedia.product_bundle.common.data.model.response.BundleItem
import com.tokopedia.product_bundle.multiple.presentation.model.ProductBundleDetail
import com.tokopedia.product_bundle.multiple.presentation.model.ProductBundleMaster
import javax.inject.Inject

class ProductBundleViewModel @Inject constructor(
        private val dispatcher: CoroutineDispatchers
) : BaseViewModel(dispatcher.main) {

    private var productBundleMasters = listOf<ProductBundleMaster>()

    private val getBundleInfoResultLiveData = MutableLiveData<BundleInfo>()
    val getBundleInfoResult: LiveData<BundleInfo> get() = getBundleInfoResultLiveData

    private val selectedProductBundleMasterLiveData = MutableLiveData<ProductBundleMaster>()
    val selectedProductBundleMaster: LiveData<ProductBundleMaster> get() = selectedProductBundleMasterLiveData

    fun getProductInfo() {

    }

    fun getProductBundleMasters(): List<ProductBundleMaster> {
        // will be replaced with product info models
        val productBundles = generateDummyBundles()
        productBundleMasters = productBundles.map {
            ProductBundleMaster(
                    bundleId = it.bundleID,
                    bundleName = it.name,
                    soldProductBundle = calculateSoldProductBundle(it.originalQuota, it.quota))
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

    fun setSelectedProductBundleMaster(productBundleMaster: ProductBundleMaster) {
        this.selectedProductBundleMasterLiveData.value = productBundleMaster
    }

    fun getSoldProductBundle(): Int {
        return this.selectedProductBundleMasterLiveData.value?.soldProductBundle ?: 0
    }

    private fun generateDummyBundles(): List<BundleInfo> {
        return listOf(BundleInfo(name = "Paket Tahun Baru", bundleID = 1L, originalQuota = 50, quota = 30),
                BundleInfo(name = "Paket Murah Banget", bundleID = 2L, originalQuota = 40, quota = 30),
                BundleInfo(name = "Paket Misqueen", bundleID = 3L, originalQuota = 30, quota = 30))
    }

    private fun getBundleInfoOne(): BundleInfo {
        val picUrl = "https://images.tokopedia.net/img/cache/300-square/VqbcmM/2020/11/5/4950a6f4-50c2-44cd-b248-0423ba4e107b.jpg"
        val bundleItemOne = listOf(
                BundleItem(name = "iPhone XR Garansi Resmi Apple Indonesia IBOX", picURL = picUrl, bundlePrice = 6240000, originalPrice = 7800000),
                BundleItem(name = "Case iPhone XR - Original Ring Fusion Kit", picURL = picUrl, bundlePrice = 95000, originalPrice = 160000),
                BundleItem(name = "Xiaomi Powerbank 10000 mAh Type-C (Garansi Resmi) Indonesia", picURL = picUrl, bundlePrice = 215000, originalPrice = 250000)
        )
        return BundleInfo(name = "Paket Tahun Baru", bundleItems = bundleItemOne)
    }

    private fun getBundleInfoTwo(): BundleInfo {
        val picUrl = "https://images.tokopedia.net/img/cache/300-square/VqbcmM/2020/11/5/4950a6f4-50c2-44cd-b248-0423ba4e107b.jpg"
        val bundleItemTwo = listOf(
                BundleItem(name = "iPhone XR Garansi Resmi Apple Indonesia IBOX", picURL = picUrl, bundlePrice = 6240000, originalPrice = 7800000),
                BundleItem(name = "Case iPhone XR - Original Ring Fusion Kit", picURL = picUrl, bundlePrice = 95000, originalPrice = 160000)
        )
        return BundleInfo(name = "Paket Murah Banget", bundleItems = bundleItemTwo)
    }

    private fun getBundleInfoThree(): BundleInfo {
        val picUrl = "https://images.tokopedia.net/img/cache/300-square/VqbcmM/2020/11/5/4950a6f4-50c2-44cd-b248-0423ba4e107b.jpg"
        val bundleItemThree = listOf(BundleItem(name = "iPhone XR Garansi Resmi Apple Indonesia IBOX", picURL = picUrl, bundlePrice = 6240000, originalPrice = 7800000))
        return BundleInfo(name = "Paket Misqueen", bundleItems = bundleItemThree)
    }

    fun mapBundleInfoListToProductBundleDetails(bundleInfo: BundleInfo): List<ProductBundleDetail> {
        TODO("add function implementation here")
    }

    fun mapProductBundleItemsToProductBundleDetail(bundleItems: List<BundleItem>): List<ProductBundleDetail> {
        return bundleItems.map { bundleItem ->
            ProductBundleDetail(
                    productImageUrl = bundleItem.picURL,
                    productName = bundleItem.name,
                    originalPrice = bundleItem.originalPrice,
                    bundlePrice = bundleItem.bundlePrice,
                    discountPercentage = calculateTotalDiscountPercentage(bundleItem.bundlePrice, bundleItem.originalPrice)
            )
        }
    }

    private fun sortActiveProductBundle(): Int {
        TODO("add function implementation here")
    }

    fun calculateSoldProductBundle(originalQuota: Int, quota: Int): Int {
        return originalQuota - quota
    }

    fun calculateTotalDiscountPercentage(originalPrice: Long, bundlePrice: Long): Int {
        return ((originalPrice - bundlePrice) / originalPrice * 100).toInt()
    }

    fun calculateTotalPrice(): Int {
        TODO("add function implementation here")
    }

    fun calculateTotalDiscountedPrice(): Int {
        TODO("add function implementation here")
    }

    fun calculateTotalSaving(): Int {
        TODO("add function implementation here")
    }
}