package com.tokopedia.topchat.chattemplate.data.mapper

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.topchat.chattemplate.domain.pojo.TemplateData
import com.tokopedia.topchat.chattemplate.view.uimodel.EditTemplateUiModel
import com.tokopedia.topchat.chattemplate.view.uimodel.GetTemplateUiModel
import com.tokopedia.topchat.chattemplate.view.uimodel.TemplateChatModel

object TemplateChatMapperKt {
    fun TemplateData.mapToTemplateUiModel(): GetTemplateUiModel {
        val result =
            GetTemplateUiModel()
        val list: ArrayList<Visitable<*>> = arrayListOf()
        for (i in this.templates.indices) {
            if (this.templates[i] != "_") {
                val templateChatModel =
                    TemplateChatModel()
                templateChatModel.message = this.templates[i]
                list.add(templateChatModel)
            }
        }
        result.isSuccess = this.isSuccess
        result.isEnabled = this.isIsEnable
        result.listTemplate = list
        return result
    }

    fun TemplateData.mapToEditTemplateUiModel(): EditTemplateUiModel {
        val model =
            EditTemplateUiModel()
        model.isSuccess = this.isSuccess
        model.isEnabled = this.isIsEnable
        return model
    }
}