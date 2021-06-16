package com.tokopedia.review.feature.reading.utils

object ReadReviewUtils {

    const val LIKED = 1
    const val UNLIKED = 0

    fun invertLikeStatus(likeStatus: Int): Int {
        return if(likeStatus == UNLIKED) {
            LIKED
        } else {
            UNLIKED
        }
    }
}