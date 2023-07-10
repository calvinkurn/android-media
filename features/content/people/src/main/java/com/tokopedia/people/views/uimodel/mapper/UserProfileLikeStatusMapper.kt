package com.tokopedia.people.views.uimodel.mapper

/**
 * Created By : Jonathan Darwin on July 03, 2023
 */
object UserProfileLikeStatusMapper {

    fun isLike(likeStatus: Int): Boolean {
        return likeStatus == LIKE_STATUS
    }

    fun getLikeStatus(isLike: Boolean): Int {
        return if (isLike) LIKE_STATUS
        else RESET_STATUS
    }

    private const val LIKE_STATUS = 1
    private const val RESET_STATUS = 3
}
