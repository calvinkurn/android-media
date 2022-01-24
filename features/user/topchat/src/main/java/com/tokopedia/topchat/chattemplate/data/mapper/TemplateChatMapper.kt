package com.tokopedia.topchat.chattemplate.data.mapper

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.topchat.chattemplate.domain.pojo.TemplateData
import com.tokopedia.topchat.chattemplate.view.uimodel.EditTemplateResultModel
import com.tokopedia.topchat.chattemplate.view.uimodel.GetTemplateResultModel
import com.tokopedia.topchat.chattemplate.view.uimodel.TemplateChatUiModel

object TemplateChatMapper {
    fun TemplateData.mapToTemplateUiModel(): GetTemplateResultModel {
        val result =
            GetTemplateResultModel()
        val list: ArrayList<Visitable<*>> = arrayListOf()
        for (i in this.templates.indices) {
            if (this.templates[i] != "_") {
                val templateChatModel = TemplateChatUiModel()
                templateChatModel.message = this.templates[i]
                list.add(templateChatModel)
            }
        }
        result.isSuccess = this.isSuccess
        result.isEnabled = this.isIsEnable
        result.listTemplate = list
        return result
    }

    fun TemplateData.mapToEditTemplateUiModel(): EditTemplateResultModel {
        val model = EditTemplateResultModel()
        model.isSuccess = this.isSuccess
        model.isEnabled = this.isIsEnable
        return model
    }
}