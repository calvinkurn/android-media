package com.tokopedia.play.data.network

import com.tokopedia.abstraction.common.data.model.response.DataResponse
import com.tokopedia.play.PLAY_GET_CHANNEL_INFO
import com.tokopedia.play.data.Channel
import kotlinx.coroutines.Deferred
import retrofit2.http.GET
import retrofit2.http.Path

/**
 * Created by mzennis on 2019-12-03.
 */

interface ChannelApi {

    @GET(PLAY_GET_CHANNEL_INFO)
    fun getChannelInfoV3(
            @Path("channelId") channelId: String
    ): Deferred<DataResponse<Channel.ChannelResponse>>

}