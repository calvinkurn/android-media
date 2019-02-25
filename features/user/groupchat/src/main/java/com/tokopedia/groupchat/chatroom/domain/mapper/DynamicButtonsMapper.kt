package com.tokopedia.groupchat.chatroom.domain.mapper

import com.tokopedia.abstraction.common.network.exception.MessageErrorException
import com.tokopedia.groupchat.chatroom.domain.pojo.ButtonsPojo
import com.tokopedia.groupchat.room.view.viewmodel.DynamicButtonsViewModel
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
        if ((response.body() != null) && (response.body().header == null ||
                        (response.body().header != null && response.body().header.messages.isEmpty()) ||
                        (response.body().header != null && response.body().header.messages[0].isBlank()))) {
            val pojo: ButtonsPojo = response.body().data
            return mapToViewModel(pojo)
        } else {
            throw MessageErrorException(response.body().header.messages[0])
        }
    }

    private fun mapToViewModel(pojo: ButtonsPojo): DynamicButtonsViewModel {
        return DynamicButtonsViewModel(
                convertToFloatingModel(pojo.floatingButton),
                convertToListDynamicButtons(pojo.listDynamicButton)
        )
    }

    private fun convertToListDynamicButtons(listDynamicButton: List<ButtonsPojo.Button>?): ArrayList<DynamicButtonsViewModel.Button> {
        val buttonList = ArrayList<DynamicButtonsViewModel.Button>()
        listDynamicButton?.let {
            for (pojo in listDynamicButton) {
                buttonList.add(DynamicButtonsViewModel.Button(
                        pojo.imageUrl,
                        pojo.linkUrl,
                        pojo.contentType,
                        pojo.contentText,
                        pojo.contentLinkUrl,
                        pojo.contentImageUrl,
                        pojo.redDot,
                        pojo.tooltip))
            }
        }
        return buttonList
    }

    private fun convertToFloatingModel(floatingButton: ButtonsPojo.Button?): DynamicButtonsViewModel.Button {
        val button = DynamicButtonsViewModel.Button()
        floatingButton?.let {
            button.contentType = it.contentType
            button.imageUrl = it.imageUrl
            button.linkUrl = it.linkUrl
            button.contentText = it.contentText
            button.contentImageUrl = it.contentImageUrl
            button.contentLinkUrl = it.contentLinkUrl
        }
        return button
    }

}