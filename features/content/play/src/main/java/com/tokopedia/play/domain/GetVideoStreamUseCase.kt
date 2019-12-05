package com.tokopedia.play.domain

import com.tokopedia.play.data.VideoStream
import com.tokopedia.play.data.network.PlayApi
import com.tokopedia.usecase.coroutines.UseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * Created by mzennis on 2019-12-03.
 */

// Please use this usecase to get wether video is vod or stream, the `is_live=true` means stream
class GetVideoStreamUseCase @Inject constructor(private val playApi: PlayApi) : UseCase<VideoStream>() {

    var channelId = ""

    override suspend fun executeOnBackground(): VideoStream {
        return withContext(Dispatchers.Default) {
            var result = VideoStream()
            try {
                val response = playApi.getVideoStream(channelId).await()
                response.data?.videoStream?.let {
                    result = it
                }
            } catch (e: Throwable) {
                throw e
            }
            result
        }
    }
}
