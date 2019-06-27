package com.tokopedia.topchat.chatroom.domain.mapper

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.network.data.model.response.DataResponse
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.topchat.chattemplate.domain.pojo.TemplateData
import com.tokopedia.topchat.chattemplate.view.viewmodel.GetTemplateViewModel
import com.tokopedia.topchat.chattemplate.view.viewmodel.TemplateChatModel
import retrofit2.Response
import rx.functions.Func1
import java.util.*
import javax.inject.Inject

/**
 * @author : Steven 03/01/19
 */
class GetTemplateChatRoomMapper @Inject constructor() : Func1<Response<DataResponse<TemplateData>>, GetTemplateViewModel> {

    override fun call(response: Response<DataResponse<TemplateData>>): GetTemplateViewModel {
        if (response.body().header == null ||
                (response.body().header != null && response.body().header.messages.isEmpty()) ||
                (response.body().header != null && response.body().header.messages[0].isBlank())) {
            val pojo: TemplateData = response.body().data
            return convertToDomain(pojo)
        } else {
            throw MessageErrorException(response.body().header.messages[0])
        }
    }

    private fun convertToDomain(data: TemplateData): GetTemplateViewModel {
        val model = GetTemplateViewModel()
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