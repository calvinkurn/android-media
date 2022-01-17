package com.tokopedia.topchat.chatroom.domain.mapper

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.network.data.model.response.DataResponse
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.topchat.chattemplate.domain.pojo.TemplateData
import com.tokopedia.topchat.chattemplate.view.viewmodel.GetTemplateUiModel
import com.tokopedia.topchat.chattemplate.view.viewmodel.TemplateChatModel
import retrofit2.Response
import java.util.ArrayList
import javax.inject.Inject

class GetTemplateChatRoomMapper  @Inject constructor() {

    fun map(response: Response<DataResponse<TemplateData>>): GetTemplateUiModel {
        val body = response.body()
        if(body != null) {
            if (body.header == null ||
                (body.header != null && body.header.messages.isEmpty()) ||
                (body.header != null && body.header.messages[0].isBlank())) {
                val pojo: TemplateData = body.data
                return convertToDomain(pojo)
            } else {
                throw MessageErrorException(body.header.messages[0])
            }
        } else {
            throw MessageErrorException("")
        }
    }

    private fun convertToDomain(data: TemplateData): GetTemplateUiModel {
        val model = GetTemplateUiModel()
        val list = ArrayList<Visitable<*>>()
        if (data.templates != null) {
            for (i in 0 until data.templates.size) {
                if (data.templates[i] != "_") {
                    val templateChatModel = TemplateChatModel()
                    templateChatModel.message = data.templates[i]
                    list.add(templateChatModel)
                }
            }
        }
        model.isSuccess = data.isSuccess
        model.isEnabled = data.isIsEnable
        model.listTemplate = list
        return model
    }
}