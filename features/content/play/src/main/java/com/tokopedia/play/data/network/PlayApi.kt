package com.tokopedia.play.data.network

import com.tokopedia.abstraction.common.data.model.response.DataResponse
import com.tokopedia.play.*
import com.tokopedia.play.data.Channel
import com.tokopedia.play.data.TotalLike
import com.tokopedia.play.data.StickyComponent
import com.tokopedia.play.data.VideoStream
import retrofit2.http.Field
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

/**
 * Created by mzennis on 2019-12-03.
 */

interface PlayApi {

    @GET(PLAY_GET_CHANNEL_INFO_V3)
    suspend fun getChannelInfoV3(
            @Path(PLAY_KEY_CHANNEL_ID) channelId: String
    ): DataResponse<Channel.Response>

    @GET(PLAY_GET_VIDEO_STREAM)
    suspend fun getVideoStream(
            @Path(PLAY_KEY_CHANNEL_ID) channelId: String
    ): DataResponse<VideoStream.Response>

    @GET(PLAY_GET_STICKY_COMPONENTS)
    suspend fun getStickyComponents(
            @Path(PLAY_KEY_CHANNEL_ID) channelId: String
    ): DataResponse<StickyComponent.Response>

    @GET(PLAY_GET_TOTAL_LIKES)
    suspend fun getTotalLike(
            @Path(PLAY_KEY_CHANNEL_ID) channelId: String
    ): DataResponse<TotalLike>

    @POST(PLAY_POST_LIKE)
    suspend fun postLike(
            @Path(PLAY_KEY_CHANNEL_ID) channelId: String,
            @Field("click") click: String
    ): DataResponse<Any>

}