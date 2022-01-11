package com.tokopedia.topchat.chattemplate.data.repository

import com.tokopedia.topchat.chattemplate.data.source.CloudEditTemplateChatDataSourceKt
import com.tokopedia.topchat.chattemplate.domain.pojo.TemplateData
import com.tokopedia.topchat.chattemplate.view.viewmodel.EditTemplateUiModel
import java.util.HashMap
import javax.inject.Inject

class EditTemplateRepositoryImplKt @Inject constructor(
    private val cloudEditTemplateChatDataSourceKt: CloudEditTemplateChatDataSourceKt
): EditTemplateRepositoryKt {

    override suspend fun editTemplate(
        index: Int,
        map: Map<String, Any>,
        isSeller: Boolean
    ): TemplateData {
        return cloudEditTemplateChatDataSourceKt.editTemplate(index, map, isSeller)
    }

    override suspend fun createTemplate(map: Map<String, Any>): TemplateData {
        return cloudEditTemplateChatDataSourceKt.createTemplate(map)
    }

    override suspend fun deleteTemplate(index: Int, isSeller: Boolean): TemplateData {
        return cloudEditTemplateChatDataSourceKt.deleteTemplate(index, isSeller)
    }
}