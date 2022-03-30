package com.tokopedia.reviewcommon.feature.media.gallery.detailed.util

import android.content.Context
import android.content.Intent
import com.tokopedia.cachemanager.SaveInstanceCacheManager
import com.tokopedia.reviewcommon.feature.media.gallery.detailed.domain.model.ProductrevGetReviewMedia
import com.tokopedia.reviewcommon.feature.media.gallery.detailed.presentation.activity.DetailedReviewMediaGalleryActivity
import com.tokopedia.reviewcommon.feature.media.gallery.detailed.presentation.viewmodel.SharedReviewMediaGalleryViewModel

object ReviewMediaGalleryRouter {
    fun routeToReviewMediaGallery(
        context: Context,
        productID: String,
        mediaPosition: Int = 1,
        showSeeMore: Boolean = false,
        preloadedDetailedReviewMediaResult: ProductrevGetReviewMedia? = null
    ): Intent {
        val cacheManager = SaveInstanceCacheManager(context, true)
        cacheManager.put(SharedReviewMediaGalleryViewModel.EXTRAS_PRODUCT_ID, productID)
        cacheManager.put(SharedReviewMediaGalleryViewModel.EXTRAS_TARGET_MEDIA_NUMBER, mediaPosition)
        cacheManager.put(SharedReviewMediaGalleryViewModel.EXTRAS_SHOW_SEE_MORE, showSeeMore)
        cacheManager.put(SharedReviewMediaGalleryViewModel.EXTRAS_PRELOADED_DETAILED_REVIEW_MEDIA_RESULT, preloadedDetailedReviewMediaResult)
        return Intent(context, DetailedReviewMediaGalleryActivity::class.java).apply {
            putExtra(DetailedReviewMediaGalleryActivity.EXTRAS_CACHE_MANAGER_ID, cacheManager.id.orEmpty())
        }
    }
}