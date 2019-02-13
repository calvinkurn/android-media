package com.tokopedia.groupchat.room.domain.usecase

import com.tokopedia.groupchat.chatroom.domain.source.ChannelInfoSource
import com.tokopedia.groupchat.chatroom.view.viewmodel.ChannelInfoViewModel
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase
import rx.Observable
import java.util.*
import javax.inject.Inject

/**
 * @author : Steven 13/02/19
 */
class GetPlayInfoUseCase @Inject constructor(private val channelInfoSource: ChannelInfoSource): UseCase<ChannelInfoViewModel>() {

    override fun createObservable(requestParams: RequestParams): Observable<ChannelInfoViewModel> {
        return channelInfoSource.getChannelInfo(requestParams.getString(
                com.tokopedia.groupchat.chatroom.domain.usecase.GetChannelInfoUseCase.PARAM_CHANNEL_UUID, ""),
                getRequestParamsWithoutPath(requestParams))
    }

    companion object {

        val PARAM_CHANNEL_UUID = "channel_uuid"
        private val PARAM_IS_REFRESH = "is_refresh"
        private val TRUE = 1
        private val FALSE = 0

        fun createParams(channelUuid: String?): RequestParams {
            val params = RequestParams.create()
            params.putString(PARAM_CHANNEL_UUID, channelUuid?:"")
            return params
        }

        fun createParams(channelUuid: String, isRefresh: Boolean): RequestParams {
            val params = RequestParams.create()
            params.putAll(createParams(channelUuid).parameters)
            params.putInt(PARAM_IS_REFRESH, if (isRefresh) TRUE else FALSE)
            return params
        }
    }

    private fun getRequestParamsWithoutPath(requestParams: RequestParams): HashMap<String, Any> {
        val params = requestParams.parameters
        params.remove(PARAM_CHANNEL_UUID)
        return params
    }

}
