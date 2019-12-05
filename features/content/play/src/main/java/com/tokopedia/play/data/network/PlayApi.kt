package com.tokopedia.play.data.network

import com.tokopedia.abstraction.common.data.model.response.DataResponse
import com.tokopedia.play.PLAY_GET_CHANNEL_INFO_V3
import com.tokopedia.play.PLAY_GET_STICKY_COMPONENTS
import com.tokopedia.play.PLAY_KEY_CHANNEL_ID
import com.tokopedia.play.data.Channel
import com.tokopedia.play.data.StickyComponent
import kotlinx.coroutines.Deferred
import retrofit2.http.GET
import retrofit2.http.Path

/**
 * Created by mzennis on 2019-12-03.
 */

interface PlayApi {

    @GET(PLAY_GET_CHANNEL_INFO_V3)
    fun getChannelInfoV3(
            @Path(PLAY_KEY_CHANNEL_ID) channelId: String
    ): Deferred<DataResponse<Channel.Response>>


    @GET(PLAY_GET_STICKY_COMPONENTS)
    fun getStickyComponents(
            @Path(PLAY_KEY_CHANNEL_ID) channelId: String
    ): Deferred<DataResponse<StickyComponent.Response>>

}