package com.tokopedia.play.domain

import com.tokopedia.play.data.network.PlayApi
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject


/**
 * Created by mzennis on 2019-12-05.
 */

// Please use this usecase to post like
class PostLikeUseCase @Inject constructor(private val playApi: PlayApi) : UseCase<Boolean>() {

    var channelId = ""

    override suspend fun executeOnBackground(): Boolean {
        val response = playApi.postLike(channelId, "1")
        return response.header.messages.size > 0
                && response.header.messages[0].contains("success", true)

    }

}
