package com.tokopedia.topchat.chattemplate.domain.mapper

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.topchat.chattemplate.domain.pojo.GetChatTemplateResponse
import com.tokopedia.topchat.chattemplate.view.uimodel.GetTemplateResultModel
import com.tokopedia.topchat.chattemplate.view.uimodel.TemplateChatUiModel

object TemplateChatMapper {
    private const val TOPCHAT_TEMPLATE_IDENTIFIER = "_"

    fun GetChatTemplateResponse.mapToGetTemplateUiModel(
        isSeller: Boolean
    ): GetTemplateResultModel {
        val result = GetTemplateResultModel()
        val list: ArrayList<Visitable<*>> = arrayListOf()
        when (isSeller) {
            true -> {
                this.chatTemplatesAll.sellerTemplate.templates.forEach {
                    addTemplateToList(it, list)
                }
                result.isEnabled = this.chatTemplatesAll.sellerTemplate.isEnable
            }
            false -> {
                this.chatTemplatesAll.buyerTemplate.templates.forEach {
                    addTemplateToList(it, list)
                }
                result.isEnabled = this.chatTemplatesAll.buyerTemplate.isEnable
            }
        }
        result.listTemplate = list
        return result
    }

    private fun addTemplateToList(
        template: String,
        list: ArrayList<Visitable<*>>
    ) {
        if (template != TOPCHAT_TEMPLATE_IDENTIFIER) {
            val templateChatModel = TemplateChatUiModel()
            templateChatModel.message = template
            list.add(templateChatModel)
        }
    }
}
