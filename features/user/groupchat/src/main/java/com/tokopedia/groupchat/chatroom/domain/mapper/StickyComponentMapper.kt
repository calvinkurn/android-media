package com.tokopedia.groupchat.chatroom.domain.mapper

import com.tokopedia.abstraction.common.network.exception.MessageErrorException
import com.tokopedia.groupchat.chatroom.domain.pojo.StickyComponentData
import com.tokopedia.groupchat.chatroom.domain.pojo.StickyComponentPojo
import com.tokopedia.groupchat.room.view.viewmodel.pinned.StickyComponentViewModel
import com.tokopedia.groupchat.room.view.viewmodel.pinned.StickyComponentsViewModel
import com.tokopedia.network.data.model.response.DataResponse
import retrofit2.Response
import rx.functions.Func1
import javax.inject.Inject

/**
 * @author by nisie on 22/02/19.
 */
class StickyComponentMapper @Inject constructor() : Func1<Response<DataResponse<StickyComponentPojo>>,
        StickyComponentsViewModel> {

    override fun call(response: Response<DataResponse<StickyComponentPojo>>): StickyComponentsViewModel {
        val body = response.body()
        if (body != null) {
            if ((body.header == null ||
                            (body.header != null && body.header.messages.isEmpty()) ||
                            (body.header != null && body.header.messages[0].isBlank()))) {
                val pojo: StickyComponentPojo = body.data
                return mapToViewModel(pojo)
            } else {
                throw MessageErrorException(body.header.messages[0])
            }
        } else {
            throw MessageErrorException("")
        }
    }

    fun mapToViewModel(pojo: StickyComponentPojo): StickyComponentsViewModel {

        val list = arrayListOf<StickyComponentViewModel>()
        for (i in 0 until pojo.stickyComponents.size) {
            val stickyComp = pojo.stickyComponents[i]
            val model = StickyComponentViewModel(
                    stickyComp.componentId,
                    stickyComp.componentType,
                    stickyComp.imageUrl,
                    stickyComp.primaryText,
                    stickyComp.secondaryText,
                    stickyComp.linkUrl,
                    stickyComp.stickyTime,
                    stickyComp.relatedButton,
                    stickyComp.attributeData.toString())
            list.add(model)
        }

        return StickyComponentsViewModel(list)
    }
}