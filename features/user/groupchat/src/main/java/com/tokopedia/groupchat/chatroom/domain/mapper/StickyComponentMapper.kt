package com.tokopedia.groupchat.chatroom.domain.mapper

import com.tokopedia.abstraction.common.network.exception.MessageErrorException
import com.tokopedia.groupchat.chatroom.domain.pojo.StickyComponentData
import com.tokopedia.groupchat.chatroom.domain.pojo.StickyComponentPojo
import com.tokopedia.groupchat.room.view.viewmodel.pinned.StickyComponentViewModel
import com.tokopedia.network.data.model.response.DataResponse
import retrofit2.Response
import rx.functions.Func1
import javax.inject.Inject

/**
 * @author by nisie on 22/02/19.
 */
class StickyComponentMapper @Inject constructor() : Func1<Response<DataResponse<StickyComponentPojo>>,
        StickyComponentViewModel> {

    override fun call(response: Response<DataResponse<StickyComponentPojo>>): StickyComponentViewModel {
        if ((response.body() != null) && (response.body().header == null ||
                        (response.body().header != null && response.body().header.messages.isEmpty()) ||
                        (response.body().header != null && response.body().header.messages[0].isBlank()))) {
            val pojo: StickyComponentPojo = response.body().data
            return mapToViewModel(pojo)
        } else {
            throw MessageErrorException(response.body().header.messages[0])
        }
    }

    public fun mapToViewModel(pojo: StickyComponentPojo): StickyComponentViewModel {
        return StickyComponentViewModel(
                pojo.stickyComponent.componentId,
                pojo.stickyComponent.componentType,
                pojo.stickyComponent.imageUrl,
                pojo.stickyComponent.primaryText,
                pojo.stickyComponent.secondaryText,
                pojo.stickyComponent.linkUrl,
                pojo.stickyComponent.stickyTime
        )
    }


    public fun mapToViewModel(pojo: StickyComponentData): StickyComponentViewModel {
        return StickyComponentViewModel(
                pojo.componentId,
                pojo.componentType,
                pojo.imageUrl,
                pojo.primaryText,
                pojo.secondaryText,
                pojo.linkUrl,
                pojo.stickyTime
        )
    }
}