package com.tokopedia.play.data.network

import com.tokopedia.abstraction.common.data.model.response.DataResponse
import com.tokopedia.play.PLAY_GET_CHANNEL_INFO_V5
import com.tokopedia.play.PLAY_GET_TOTAL_LIKES
import com.tokopedia.play.PLAY_KEY_CHANNEL_ID
import com.tokopedia.play.data.Channel
import com.tokopedia.play.data.TotalLike
import retrofit2.http.GET
import retrofit2.http.Path

/**
 * Created by mzennis on 2019-12-03.Ø
 */

interface PlayApi {

    @GET(PLAY_GET_CHANNEL_INFO_V5)
    suspend fun getChannelInfoV5(
            @Path(PLAY_KEY_CHANNEL_ID) channelId: String
    ): DataResponse<Channel.Response>

    @GET(PLAY_GET_TOTAL_LIKES)
    suspend fun getTotalLike(
            @Path(PLAY_KEY_CHANNEL_ID) channelId: String
    ): DataResponse<TotalLike>
}