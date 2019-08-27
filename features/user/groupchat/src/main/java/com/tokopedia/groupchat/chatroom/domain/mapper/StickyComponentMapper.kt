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

    public fun mapToViewModel(pojo: StickyComponentPojo): StickyComponentsViewModel {
//        return StickyComponentViewModel(
//                pojo.stickyComponent.componentId,
//                pojo.stickyComponent.componentType,
//                pojo.stickyComponent.imageUrl,
//                pojo.stickyComponent.primaryText,
//                pojo.stickyComponent.secondaryText,
//                pojo.stickyComponent.linkUrl,
//                pojo.stickyComponent.stickyTime,
//                pojo.stickyComponent.relatedButton
//        )
//        for(i in 0 .. pojo.stickyComponents.size) {
//            val stickyComp = pojo.stickyComponents[i]
//            StickyComponentViewModel(
//                stickyComp.componentId,
//                stickyComp.componentType,
//                stickyComp.imageUrl,
//                stickyComp.primaryText,
//                stickyComp.secondaryText,
//                stickyComp.linkUrl,
//                stickyComp.stickyTime,
//                stickyComp.relatedButton)
//        }
        val model = StickyComponentViewModel(
                pojo.stickyComponent.componentId,
                pojo.stickyComponent.componentType,
                pojo.stickyComponent.imageUrl,
                pojo.stickyComponent.primaryText,
                pojo.stickyComponent.secondaryText,
                pojo.stickyComponent.linkUrl,
                pojo.stickyComponent.stickyTime,
                pojo.stickyComponent.relatedButton,
                pojo.stickyComponent.attributeData.toString())

        return StickyComponentsViewModel(listOf(model))
    }


    public fun mapToViewModel(pojo: StickyComponentData): StickyComponentsViewModel {
//        return StickyComponentViewModel(
//                pojo.componentId,
//                pojo.componentType,
//                pojo.imageUrl,
//                pojo.primaryText,
//                pojo.secondaryText,
//                pojo.linkUrl,
//                pojo.stickyTime,
//                pojo.relatedButton
//        )

        val model = StickyComponentViewModel(
                pojo.componentId,
                pojo.componentType,
                pojo.imageUrl,
                pojo.primaryText,
                pojo.secondaryText,
                pojo.linkUrl,
                pojo.stickyTime,
                pojo.relatedButton,
                pojo.attributeData.toString())

        return StickyComponentsViewModel(listOf(model))
    }
}