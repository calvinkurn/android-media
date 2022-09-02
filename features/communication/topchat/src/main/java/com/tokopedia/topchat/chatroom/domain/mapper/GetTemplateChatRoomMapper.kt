package com.tokopedia.topchat.chatroom.domain.mapper

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.network.data.model.response.DataResponse
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.topchat.chattemplate.domain.pojo.TemplateData
import com.tokopedia.topchat.chattemplate.view.uimodel.GetTemplateResultModel
import com.tokopedia.topchat.chattemplate.view.uimodel.TemplateChatUiModel
import retrofit2.Response
import java.util.ArrayList
import javax.inject.Inject

class GetTemplateChatRoomMapper  @Inject constructor() {

    fun map(response: Response<DataResponse<TemplateData>>): GetTemplateResultModel {
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

    private fun convertToDomain(data: TemplateData): GetTemplateResultModel {
        val model = GetTemplateResultModel()
        val list = ArrayList<Visitable<*>>()
        data.templates.let {
            for (i in it.indices) {
                if (it[i] != "_") {
                    val templateChatModel = TemplateChatUiModel()
                    templateChatModel.message = it[i]
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