package com.tokopedia.play.data.network

import com.tokopedia.network.data.model.response.DataResponse
import com.tokopedia.play.PLAY_GET_CHANNEL_INFO_V5
import com.tokopedia.play.PLAY_KEY_CHANNEL_ID
import com.tokopedia.play.data.Channel
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

/**
 * Created by mzennis on 2019-12-03.
 */

interface PlayApi {

    @GET(PLAY_GET_CHANNEL_INFO_V5)
    suspend fun getChannelInfoV5(
            @Path(PLAY_KEY_CHANNEL_ID) channelId: String
    ): Response<DataResponse<Channel.Response>>
}