package com.tokopedia.groupchat.chatroom.domain.usecase

import com.tokopedia.groupchat.chatroom.domain.source.ChannelInfoSource
import com.tokopedia.groupchat.room.view.viewmodel.DynamicButtonsViewModel
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase
import rx.Observable
import java.util.*
import javax.inject.Inject

/**
 * @author by nisie on 22/02/19.
 */
class GetDynamicButtonsUseCase @Inject constructor(private val channelInfoSource:
                                                   ChannelInfoSource) : UseCase<DynamicButtonsViewModel>() {
    val PARAM_CHANNEL_UUID = "channel_uuid"

    override fun createObservable(requestParams: RequestParams): Observable<DynamicButtonsViewModel> {
        return channelInfoSource.getDynamicButtons(requestParams.getString(
                GetChannelInfoUseCase.PARAM_CHANNEL_UUID, ""),
                getRequestParamsWithoutPath(requestParams))
    }

    private fun getRequestParamsWithoutPath(requestParams: RequestParams): HashMap<String, Any> {
        val params = requestParams.parameters
        params.remove(PARAM_CHANNEL_UUID)
        return params
    }
}