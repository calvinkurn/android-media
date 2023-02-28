package com.tokopedia.topchat.chatroom.domain.mapper

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.topchat.chattemplate.domain.pojo.TopchatChatTemplates
import com.tokopedia.topchat.chattemplate.view.uimodel.GetTemplateResultModel
import com.tokopedia.topchat.chattemplate.view.uimodel.TemplateChatUiModel
import javax.inject.Inject

class GetTemplateChatRoomMapper @Inject constructor() {

    fun map(template: TopchatChatTemplates): GetTemplateResultModel {
        val model = GetTemplateResultModel()
        val list = ArrayList<Visitable<*>>()
        if (template.isEnable) {
            template.templates.forEach {
                val templateChatModel = TemplateChatUiModel()
                templateChatModel.message = it
                list.add(templateChatModel)
            }
        }
        model.isEnabled = template.isEnable
        model.listTemplate = list
        return model
    }
}
