package com.tokopedia.groupchat.chatroom.domain.mapper

import com.tokopedia.abstraction.common.network.exception.MessageErrorException
import com.tokopedia.groupchat.chatroom.domain.pojo.ButtonsPojo
import com.tokopedia.groupchat.room.view.viewmodel.DynamicButton
import com.tokopedia.groupchat.room.view.viewmodel.DynamicButtonsViewModel
import com.tokopedia.groupchat.room.view.viewmodel.InteractiveButton
import com.tokopedia.network.data.model.response.DataResponse
import retrofit2.Response
import rx.functions.Func1
import javax.inject.Inject

/**
 * @author by nisie on 22/02/19.
 */
class DynamicButtonsMapper @Inject constructor() : Func1<Response<DataResponse<ButtonsPojo>>,
        DynamicButtonsViewModel> {

    override fun call(response: Response<DataResponse<ButtonsPojo>>): DynamicButtonsViewModel {
        val body = response.body()
        if(body != null) {
            if (body.header == null ||
                    (body.header != null && body.header.messages.isEmpty()) ||
                    (body.header != null && body.header.messages[0].isBlank())) {
                val pojo: ButtonsPojo = body.data
                return mapToViewModel(pojo)
            } else {
                throw MessageErrorException(body.header.messages[0])
            }
        } else {
            throw MessageErrorException("")
        }
    }

    private fun mapToViewModel(pojo: ButtonsPojo): DynamicButtonsViewModel {
        return DynamicButtonsViewModel(
                convertToFloatingModel(pojo.floatingButton),
                convertToListDynamicButtons(pojo.listDynamicButton),
                convertToInteractiveButton(pojo.interactiveButton)
        )
    }

    private fun convertToListDynamicButtons(listDynamicButton: List<ButtonsPojo.Button>?): ArrayList<DynamicButton> {
        val buttonList = ArrayList<DynamicButton>()
        listDynamicButton?.let {
            for (pojo in listDynamicButton) {
                buttonList.add(DynamicButton(
                        pojo.buttonId,
                        pojo.imageUrl,
                        pojo.linkUrl,
                        pojo.contentType,
                        pojo.contentText,
                        pojo.contentButtonText,
                        pojo.contentLinkUrl,
                        pojo.contentImageUrl,
                        pojo.redDot,
                        pojo.tooltip,
                        pojo.tooltipDuration,
                        pojo.priority))
            }
        }
        return buttonList
    }

    private fun convertToFloatingModel(floatingButton: ButtonsPojo.Button?): DynamicButton {
        val button = DynamicButton()
        floatingButton?.let {
            button.contentType = it.contentType
            button.imageUrl = it.imageUrl
            button.linkUrl = it.linkUrl
            button.contentText = it.contentText
            button.contentButtonText = it.contentButtonText
            button.contentImageUrl = it.contentImageUrl
            button.contentLinkUrl = it.contentLinkUrl
        }
        return button
    }

    private fun convertToInteractiveButton(interactiveButton: ButtonsPojo.InteractiveButton)
            : InteractiveButton {
        val button = InteractiveButton()
        interactiveButton?.let {
            button.isEnabled = it.isEnabled
            button.balloonList = it.listBalloon
        }
        return button
    }

}