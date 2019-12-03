package com.tokopedia.play.data.network

import com.tokopedia.abstraction.common.data.model.response.DataResponse
import com.tokopedia.play.data.Channel
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import rx.Observable


/**
 * Created by mzennis on 2019-12-03.
 */

interface ChannelApi {

    @GET("https://chat.tokopedia.com/gcn/api/v3/channel/{channelId}")
    fun getChannelInfoV3(
            @Path("channelId") channelId: String
    ): Observable<Response<DataResponse<Channel.ChannelResponse>>>

}