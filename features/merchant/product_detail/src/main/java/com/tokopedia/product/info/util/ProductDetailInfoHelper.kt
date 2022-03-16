package com.tokopedia.product.info.util

import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import com.tokopedia.cachemanager.SaveInstanceCacheManager
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.product.detail.common.data.model.pdplayout.DynamicProductInfoP1
import com.tokopedia.product.detail.data.model.datamodel.ProductDetailInfoContent
import com.tokopedia.product.detail.data.model.productinfo.ProductInfoParcelData
import com.tokopedia.product.detail.di.ProductDetailComponent
import com.tokopedia.product.info.view.bottomsheet.ProductDetailBottomSheetListener
import com.tokopedia.product.info.view.bottomsheet.ProductDetailInfoBottomSheet

object ProductDetailInfoHelper {

    fun showBottomSheetInfo(fragmentActivity: FragmentActivity,
                            daggerComponent: ProductDetailComponent?,
                            listener: ProductDetailBottomSheetListener,
                            p1Data: DynamicProductInfoP1?,
                            sizeChartImageUrl: String?,
                            detailInfoContent: List<ProductDetailInfoContent>,
                            forceRefresh: Boolean) {

        val cacheManager = SaveInstanceCacheManager(fragmentActivity, true)
        val parcelData = generateProductInfoParcel(
                p1Data,
                sizeChartImageUrl ?: "",
                detailInfoContent,
                forceRefresh
        )
        cacheManager.put(ProductDetailInfoBottomSheet::class.java.simpleName, parcelData)

        ProductDetailInfoBottomSheet().also {
            it.arguments = Bundle().apply {
                putString(
                        ProductDetailInfoBottomSheet.PRODUCT_DETAIL_INFO_PARCEL_KEY,
                        cacheManager.id
                )
            }
        }.show(
                childFragmentManager = fragmentActivity.supportFragmentManager,
                daggerProductDetailComponent = daggerComponent,
                listener = listener
        )
    }

    private fun generateProductInfoParcel(productInfoP1: DynamicProductInfoP1?,
                                          variantGuideLine: String,
                                          productInfoContent: List<ProductDetailInfoContent>,
                                          forceRefresh: Boolean): ProductInfoParcelData {

        productInfoP1?.let {
            val data = it.data
            val basic = it.basic
            val parentId = it.parentProductId

            return ProductInfoParcelData(
                    productId = basic.productID,
                    shopId = basic.shopID,
                    productTitle = data.name,
                    productImageUrl = data.getProductImageUrl() ?: "",
                    variantGuideline = variantGuideLine,
                    discussionCount = productInfoP1.basic.stats.countTalk.toIntOrZero(),
                    listOfYoutubeVideo = data.youtubeVideos,
                    data = productInfoContent,
                    forceRefresh = forceRefresh,
                    isTokoNow = productInfoP1.basic.isTokoNow,
                    isGiftable = basic.isGiftable,
                    parentId = parentId
            )
        } ?: return ProductInfoParcelData()

    }
}