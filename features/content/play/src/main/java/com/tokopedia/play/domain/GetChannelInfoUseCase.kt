package com.tokopedia.play.domain

import com.tokopedia.kotlin.extensions.view.toEmptyStringIfNull
import com.tokopedia.play.PLAY_KEY_CHANNEL_ID
import com.tokopedia.play.data.Channel
import com.tokopedia.play.data.network.ChannelApi
import com.tokopedia.play.data.network.ChannelMapper
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase
import rx.Observable
import javax.inject.Inject

/**
 * Created by mzennis on 2019-12-03.
 */

class GetChannelInfoUseCase @Inject constructor(val channelApi: ChannelApi,
                                               val channelMapper: ChannelMapper) : UseCase<Channel>() {

    override fun createObservable(requestParams: RequestParams?): Observable<Channel> {
        return channelApi
                .getChannelInfoV3(getChannelId(requestParams))
                .map(channelMapper)
    }

    fun getChannelId(requestParams: RequestParams?): String {
        return requestParams?.
                getString(PLAY_KEY_CHANNEL_ID, "")
                .toEmptyStringIfNull()
    }

    fun createParams(channelId: String): RequestParams? {
        val params = RequestParams.create()
        params.putString(PLAY_KEY_CHANNEL_ID, channelId)
        return params
    }
}
