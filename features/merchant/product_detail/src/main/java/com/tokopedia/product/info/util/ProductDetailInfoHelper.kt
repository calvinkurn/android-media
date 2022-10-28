package com.tokopedia.product.info.util

import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import com.tokopedia.cachemanager.SaveInstanceCacheManager
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.product.detail.common.data.model.pdplayout.DynamicProductInfoP1
import com.tokopedia.product.detail.common.showImmediately
import com.tokopedia.product.detail.data.model.datamodel.product_detail_info.ProductDetailInfoDataModel
import com.tokopedia.product.detail.data.model.productinfo.ProductInfoParcelData
import com.tokopedia.product.detail.di.ProductDetailComponent
import com.tokopedia.product.info.view.bottomsheet.ProductDetailBottomSheetListener
import com.tokopedia.product.info.view.bottomsheet.ProductDetailInfoBottomSheet

object ProductDetailInfoHelper {

    fun showBottomSheetInfo(
        fragmentActivity: FragmentActivity,
        daggerComponent: ProductDetailComponent?,
        listener: ProductDetailBottomSheetListener,
        p1Data: DynamicProductInfoP1?,
        sizeChartImageUrl: String?,
        infoData: ProductDetailInfoDataModel,
        forceRefresh: Boolean,
        isOpenSpecification: Boolean
    ) {
        val cacheManager = SaveInstanceCacheManager(fragmentActivity, true)
        val parcelData = generateProductInfoParcel(
            productInfoP1 = p1Data,
            variantGuideLine = sizeChartImageUrl.orEmpty(),
            productInfo = infoData,
            forceRefresh = forceRefresh,
            isOpenSpecification = isOpenSpecification
        )
        cacheManager.put(ProductDetailInfoBottomSheet::class.java.simpleName, parcelData)

        showImmediately(
            fragmentManager = fragmentActivity.supportFragmentManager,
            tag = ProductDetailInfoBottomSheet.PRODUCT_DETAIL_BOTTOM_SHEET_KEY
        ) {
            ProductDetailInfoBottomSheet().apply {
                setup(daggerProductDetailComponent = daggerComponent, listener = listener)
                arguments = Bundle().apply {
                    putString(
                        ProductDetailInfoBottomSheet.PRODUCT_DETAIL_INFO_PARCEL_KEY,
                        cacheManager.id
                    )
                }
            }
        }
    }

    private fun generateProductInfoParcel(
        productInfoP1: DynamicProductInfoP1?,
        variantGuideLine: String,
        productInfo: ProductDetailInfoDataModel,
        forceRefresh: Boolean,
        isOpenSpecification: Boolean
    ): ProductInfoParcelData {

        productInfoP1?.let {
            val data = it.data
            val basic = it.basic
            val parentId = it.parentProductId

            return ProductInfoParcelData(
                productId = basic.productID,
                shopId = basic.shopID,
                productTitle = data.parentName,
                productImageUrl = data.getProductImageUrl().orEmpty(),
                variantGuideline = variantGuideLine,
                discussionCount = productInfoP1.basic.stats.countTalk.toIntOrZero(),
                listOfYoutubeVideo = data.youtubeVideos,
                productInfo = productInfo,
                forceRefresh = forceRefresh,
                isTokoNow = productInfoP1.basic.isTokoNow,
                isGiftable = basic.isGiftable,
                parentId = parentId,
                catalogId = basic.catalogID,
                isOpenSpecification = isOpenSpecification
            )
        } ?: return ProductInfoParcelData()
    }
}