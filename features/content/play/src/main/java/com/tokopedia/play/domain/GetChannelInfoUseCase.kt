package com.tokopedia.play.domain

import com.tokopedia.play.data.Channel
import com.tokopedia.play.data.network.ChannelApi
import com.tokopedia.usecase.coroutines.UseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * Created by mzennis on 2019-12-03.
 */

class GetChannelInfoUseCase @Inject constructor(private val channelApi: ChannelApi) : UseCase<Channel>() {

    var channelId = ""

    override suspend fun executeOnBackground(): Channel {
        return withContext(Dispatchers.Default) {
            var result = Channel()
            try {
                val response = channelApi.getChannelInfoV3(channelId).await()
                response.data?.channel?.let {
                    result = it
                }
            } catch (e: Throwable) {
                throw e
            }
            result
        }
    }
}
