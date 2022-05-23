package com.tokopedia.reviewcommon.feature.media.gallery.detailed.util

import android.content.Context
import android.content.Intent
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.cachemanager.SaveInstanceCacheManager
import com.tokopedia.reviewcommon.feature.media.gallery.detailed.domain.model.ProductrevGetReviewMedia

object ReviewMediaGalleryRouter {

    const val EXTRAS_PAGE_SOURCE = "extrasPageSource"
    const val EXTRAS_CACHE_MANAGER_ID = "extrasCacheManagerId"
    const val EXTRAS_PRODUCT_ID = "extrasProductId"
    const val EXTRAS_SHOP_ID = "extrasShopId"
    const val EXTRAS_IS_PRODUCT_REVIEW = "extrasIsProductReview"
    const val EXTRAS_IS_FROM_GALLERY = "extrasIsFromGallery"
    const val EXTRAS_TARGET_MEDIA_NUMBER = "extrasTargetMediaNumber"
    const val EXTRAS_SHOW_SEE_MORE = "extrasShowSeeMore"
    const val EXTRAS_PRELOADED_DETAILED_REVIEW_MEDIA_RESULT = "extrasPreloadedReviewMediaResult"

    fun routeToReviewMediaGallery(
        context: Context,
        pageSource: PageSource,
        productID: String,
        shopID: String,
        isProductReview: Boolean,
        isFromGallery: Boolean,
        mediaPosition: Int = 1,
        showSeeMore: Boolean = false,
        preloadedDetailedReviewMediaResult: ProductrevGetReviewMedia? = null
    ): Intent {
        val cacheManager = SaveInstanceCacheManager(context, true)
        cacheManager.put(EXTRAS_PAGE_SOURCE, pageSource)
        cacheManager.put(EXTRAS_PRODUCT_ID, productID)
        cacheManager.put(EXTRAS_SHOP_ID, shopID)
        cacheManager.put(EXTRAS_IS_PRODUCT_REVIEW, isProductReview)
        cacheManager.put(EXTRAS_IS_FROM_GALLERY, isFromGallery)
        cacheManager.put(EXTRAS_PRODUCT_ID, productID)
        cacheManager.put(EXTRAS_TARGET_MEDIA_NUMBER, mediaPosition)
        cacheManager.put(EXTRAS_SHOW_SEE_MORE, showSeeMore)
        cacheManager.put(EXTRAS_PRELOADED_DETAILED_REVIEW_MEDIA_RESULT, preloadedDetailedReviewMediaResult)
        return RouteManager.getIntent(context, ApplinkConstInternalMarketplace.REVIEW_MEDIA_GALLERY).apply {
            putExtra(EXTRAS_CACHE_MANAGER_ID, cacheManager.id.orEmpty())
        }
    }

    enum class PageSource {
        PDP, REVIEW
    }
}