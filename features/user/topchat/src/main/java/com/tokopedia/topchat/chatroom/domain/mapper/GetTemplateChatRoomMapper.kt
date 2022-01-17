package com.tokopedia.topchat.chatroom.domain.mapper

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.network.data.model.response.DataResponse
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.topchat.chattemplate.domain.pojo.TemplateData
import com.tokopedia.topchat.chattemplate.view.uimodel.GetTemplateUiModel
import com.tokopedia.topchat.chattemplate.view.uimodel.TemplateChatModel
import retrofit2.Response
import rx.functions.Func1
import java.util.*
import javax.inject.Inject

/**
 * @author : Steven 03/01/19
 */
class GetTemplateChatRoomMapper @Inject constructor() : Func1<Response<DataResponse<TemplateData>>, GetTemplateUiModel> {

    override fun call(response: Response<DataResponse<TemplateData>>): GetTemplateUiModel {
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
        data.templates.let {
            for (i in it.indices) {
                if (it[i] != "_") {
                    val templateChatModel = TemplateChatModel()
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