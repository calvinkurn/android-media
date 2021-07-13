package com.tokopedia.play.domain.repository

/**
 * Created by jegul on 12/07/21
 */
interface PlayViewerLikeRepository {

    suspend fun getIsLiked(contentId: Long, contentType: Int): Boolean

    suspend fun postLike(contentId: Long, contentType: Int, likeType: Int, shouldLike: Boolean): Boolean
}