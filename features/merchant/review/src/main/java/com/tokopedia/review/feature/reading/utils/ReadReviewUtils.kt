package com.tokopedia.review.feature.reading.utils

object ReadReviewUtils {

    const val LIKED = 1
    const val NEUTRAL = 3

    fun invertLikeStatus(likeStatus: Int): Int {
        return if(likeStatus == LIKED) {
            NEUTRAL
        } else {
            LIKED
        }
    }
}