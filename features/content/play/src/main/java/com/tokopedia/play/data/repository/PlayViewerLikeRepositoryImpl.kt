package com.tokopedia.play.data.repository

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.play.domain.GetIsLikeUseCase
import com.tokopedia.play.domain.PostLikeUseCase
import com.tokopedia.play.domain.repository.PlayViewerLikeRepository
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * Created by jegul on 12/07/21
 */
class PlayViewerLikeRepositoryImpl @Inject constructor(
        private val getIsLikeUseCase: GetIsLikeUseCase,
        private val postLikeUseCase: PostLikeUseCase,
        private val dispatchers: CoroutineDispatchers,
) : PlayViewerLikeRepository {

    override suspend fun getIsLiked(contentId: Long, contentType: Int): Boolean = withContext(dispatchers.io) {
        return@withContext getIsLikeUseCase.apply {
            params = GetIsLikeUseCase.createParam(
                    contentId = contentId,
                    contentType = contentType
            )
        }.executeOnBackground()
    }

    override suspend fun postLike(contentId: Long, contentType: Int, likeType: Int, shouldLike: Boolean): Boolean = withContext(dispatchers.io) {
        return@withContext postLikeUseCase.apply {
            params = PostLikeUseCase.createParam(
                    contentId = contentId,
                    contentType = contentType,
                    likeType = likeType,
                    action = shouldLike
            )
        }.executeOnBackground()
    }
}