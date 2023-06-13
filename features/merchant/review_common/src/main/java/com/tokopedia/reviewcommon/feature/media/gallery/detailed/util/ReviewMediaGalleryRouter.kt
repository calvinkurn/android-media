package com.tokopedia.reviewcommon.feature.media.gallery.detailed.util

import android.content.Context
import android.content.Intent
import androidx.annotation.IntDef
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
    const val EXTRAS_IS_REVIEW_OWNER = "extrasIsReviewOwner"
    const val EXTRAS_PRELOADED_DETAILED_REVIEW_MEDIA_RESULT = "extrasPreloadedReviewMediaResult"

    private const val EXTRAS_FEEDBACK_ID_RESULT = "extrasFeedbackIdResult"
    private const val EXTRAS_LIKE_STATUS_RESULT = "extrasLikeStatusResult"

    fun routeToReviewMediaGallery(
        context: Context,
        @PageSource pageSource: Int,
        productID: String,
        shopID: String,
        isProductReview: Boolean,
        isFromGallery: Boolean,
        mediaPosition: Int = 1,
        showSeeMore: Boolean = false,
        isReviewOwner: Boolean = false,
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
        cacheManager.put(EXTRAS_IS_REVIEW_OWNER, isReviewOwner)
        cacheManager.put(EXTRAS_PRELOADED_DETAILED_REVIEW_MEDIA_RESULT, preloadedDetailedReviewMediaResult)
        return RouteManager.getIntent(context, ApplinkConstInternalMarketplace.REVIEW_MEDIA_GALLERY).apply {
            putExtra(EXTRAS_CACHE_MANAGER_ID, cacheManager.id.orEmpty())
            putExtra(EXTRAS_PAGE_SOURCE, pageSource)
        }
    }

    fun setResultData(
        feedbackId: String,
        likeStatus: Int
    ): Intent {
        return Intent().apply {
            putExtra(EXTRAS_FEEDBACK_ID_RESULT, feedbackId)
            putExtra(EXTRAS_LIKE_STATUS_RESULT, likeStatus)
        }
    }

    fun getFeedbackIdResult(intent: Intent): String {
        return intent.getStringExtra(EXTRAS_FEEDBACK_ID_RESULT).orEmpty()
    }

    fun getLikeStatusResult(intent: Intent): Int {
        return intent.getIntExtra(EXTRAS_LIKE_STATUS_RESULT, -1)
    }

    @Retention(AnnotationRetention.SOURCE)
    @Target(AnnotationTarget.PROPERTY, AnnotationTarget.FIELD, AnnotationTarget.VALUE_PARAMETER)
    @IntDef(value = [PageSource.PDP, PageSource.REVIEW, PageSource.USER_PROFILE])
    annotation class PageSource {
        companion object {
            const val PDP = 0
            const val REVIEW = 1
            const val USER_PROFILE = 2
        }
    }
}
