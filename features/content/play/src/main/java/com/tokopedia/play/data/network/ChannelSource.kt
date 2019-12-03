package com.tokopedia.play.data.network

import com.tokopedia.play.data.Channel
import rx.Observable


/**
 * Created by mzennis on 2019-12-03.
 */

class ChannelSource(
        val channelApi: ChannelApi,
        val channelMapper: ChannelMapper) {

    fun getChannelInfoV3(channelId: String): Observable<Channel> {
        return channelApi.getChannelInfoV3(channelId)
                .map(channelMapper)
    }
}