package com.tokopedia.play.data.network

import com.tokopedia.abstraction.common.data.model.response.DataResponse
import com.tokopedia.play.data.Channel
import com.tokopedia.play.PLAY_GET_CHANNEL_INFO
import com.tokopedia.play.PLAY_KEY_CHANNEL_ID
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import rx.Observable


/**
 * Created by mzennis on 2019-12-03.
 */

interface ChannelApi {

    @GET(PLAY_GET_CHANNEL_INFO)
    fun getChannelInfoV3(
            @Path(PLAY_KEY_CHANNEL_ID) channelId: String
    ): Observable<Response<DataResponse<Channel.ChannelResponse>>>

}