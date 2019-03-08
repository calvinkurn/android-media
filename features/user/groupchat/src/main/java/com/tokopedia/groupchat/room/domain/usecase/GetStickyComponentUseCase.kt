package com.tokopedia.groupchat.room.domain.usecase

import com.tokopedia.groupchat.chatroom.domain.source.ChannelInfoSource
import com.tokopedia.groupchat.room.view.viewmodel.pinned.StickyComponentViewModel
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase
import rx.Observable
import javax.inject.Inject

/**
 * @author by nisie on 25/02/19.
 */

class GetStickyComponentUseCase @Inject constructor(private val channelInfoSource:
                                                    ChannelInfoSource) :
        UseCase<StickyComponentViewModel>() {

    override fun createObservable(requestParams: RequestParams): Observable<StickyComponentViewModel> {
        return channelInfoSource.getStickyComponent(requestParams.getString(
                GetStickyComponentUseCase.PARAM_CHANNEL_UUID, ""),
                getRequestParamsWithoutPath(requestParams))
    }

    private fun getRequestParamsWithoutPath(requestParams: RequestParams): HashMap<String, Any> {
        val params = requestParams.parameters
        params.remove(PARAM_CHANNEL_UUID)
        return params
    }

    companion object {

        val PARAM_CHANNEL_UUID = "channel_uuid"

        fun createParams(channelUuid: String?): RequestParams {
            val params = RequestParams.create()
            params.putString(PARAM_CHANNEL_UUID, channelUuid?:"")
            return params
        }
    }

}