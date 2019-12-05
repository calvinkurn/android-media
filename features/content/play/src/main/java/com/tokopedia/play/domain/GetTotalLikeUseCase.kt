package com.tokopedia.play.domain

import com.tokopedia.play.data.Like
import com.tokopedia.play.data.network.PlayApi
import com.tokopedia.usecase.coroutines.UseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * Created by mzennis on 2019-12-03.
 */

// Please use this usecase to get total likes
class GetTotalLikeUseCase @Inject constructor(private val playApi: PlayApi) : UseCase<Like>() {

    var channelId = ""

    override suspend fun executeOnBackground(): Like {
        return withContext(Dispatchers.Default) {
            var result: Like
            try {
                val response = playApi.getTotalLike(channelId).await()
                response.data.let {
                    result = it
                }
            } catch (e: Throwable) {
                throw e
            }
            result
        }
    }
}
