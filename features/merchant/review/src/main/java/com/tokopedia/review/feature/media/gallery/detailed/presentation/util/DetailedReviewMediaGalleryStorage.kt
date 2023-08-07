package com.tokopedia.review.feature.media.gallery.detailed.presentation.util

import com.tokopedia.reviewcommon.feature.media.gallery.detailed.util.ReviewMediaGalleryRouter

/**
 * Created By : Jonathan Darwin on May 29, 2023
 */
object DetailedReviewMediaGalleryStorage {

    private val map = mutableMapOf<String, Any>()

    @ReviewMediaGalleryRouter.PageSource
    var pageSource: Int
        get() {
            return (map[ReviewMediaGalleryRouter.EXTRAS_PAGE_SOURCE] as? Int) ?: ReviewMediaGalleryRouter.PageSource.REVIEW
        }
        set(value) {
            map[ReviewMediaGalleryRouter.EXTRAS_PAGE_SOURCE] = value
        }

    fun clear() {
        map.clear()
    }
}
