package com.tokopedia.groupchat.chatroom.domain.mapper

import com.tokopedia.abstraction.common.network.exception.MessageErrorException
import com.tokopedia.groupchat.chatroom.domain.pojo.VideoStreamParent
import com.tokopedia.groupchat.room.view.viewmodel.VideoStreamViewModel
import com.tokopedia.network.data.model.response.DataResponse
import retrofit2.Response
import rx.functions.Func1
import javax.inject.Inject

/**
 * @author by stevenfredian on 19/06/19.
 */
class VideoStreamMapper @Inject constructor() : Func1<Response<DataResponse<VideoStreamParent>>,
        VideoStreamViewModel> {

    override fun call(response: Response<DataResponse<VideoStreamParent>>): VideoStreamViewModel {
        if ((response.body() != null) && (response.body().header == null ||
                        (response.body().header != null && response.body().header.messages.isEmpty()) ||
                        (response.body().header != null && response.body().header.messages[0].isBlank()))) {
            val pojo: VideoStreamParent = response.body().data
            return mapToViewModel(pojo)
        } else {
            throw MessageErrorException(response.body().header.messages[0])
        }
    }

    private fun mapToViewModel(pojo: VideoStreamParent): VideoStreamViewModel {
        return VideoStreamViewModel(
                pojo.videoStreamData.isActive,
                pojo.videoStreamData.isLive,
                pojo.videoStreamData.androidStreamHD,
                pojo.videoStreamData.androidStreamSD,
                pojo.videoStreamData.iosStreamHD,
                pojo.videoStreamData.iosStreamSD,
                pojo.videoStreamData.orientation
        )
    }

}