package com.tokopedia.groupchat.chatroom.domain.mapper

import com.tokopedia.abstraction.common.network.exception.MessageErrorException
import com.tokopedia.groupchat.chatroom.domain.pojo.ButtonsPojo
import com.tokopedia.groupchat.chatroom.domain.pojo.VideoStreamPojo
import com.tokopedia.groupchat.room.view.viewmodel.DynamicButton
import com.tokopedia.groupchat.room.view.viewmodel.DynamicButtonsViewModel
import com.tokopedia.groupchat.room.view.viewmodel.InteractiveButton
import com.tokopedia.groupchat.room.view.viewmodel.VideoStreamViewModel
import com.tokopedia.network.data.model.response.DataResponse
import retrofit2.Response
import rx.functions.Func1
import javax.inject.Inject

/**
 * @author by stevenfredian on 19/06/19.
 */
class VideoStreamMapper @Inject constructor() : Func1<Response<DataResponse<VideoStreamPojo>>,
        VideoStreamViewModel> {

    override fun call(response: Response<DataResponse<VideoStreamPojo>>): VideoStreamViewModel {
        if ((response.body() != null) && (response.body().header == null ||
                        (response.body().header != null && response.body().header.messages.isEmpty()) ||
                        (response.body().header != null && response.body().header.messages[0].isBlank()))) {
            val pojo: VideoStreamPojo = response.body().data
            return mapToViewModel(pojo)
        } else {
            throw MessageErrorException(response.body().header.messages[0])
        }
    }

    private fun mapToViewModel(pojo: VideoStreamPojo): VideoStreamViewModel {
        return VideoStreamViewModel(
                pojo.isActive,
                pojo.isLive,
                pojo.streamRtmpStandard,
                pojo.streamRtmpHigh,
                pojo.streamHlsStandard,
                pojo.streamHlsHigh,
                pojo.orientation
        )
    }

}