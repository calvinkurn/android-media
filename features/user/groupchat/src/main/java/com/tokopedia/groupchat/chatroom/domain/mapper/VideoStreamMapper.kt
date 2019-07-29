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
        val body = response.body()
        if ((body != null) && (body.header == null ||
                        (body.header != null && body.header.messages.isEmpty()) ||
                        (body.header != null && body.header.messages[0].isBlank()))) {
            val pojo: VideoStreamParent = body.data
            return mapToViewModel(pojo)
        } else {
            if(body != null) {
                throw MessageErrorException(body.header.messages[0])
            } else {
                throw MessageErrorException()
            }
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