package com.tokopedia.topchat.chattemplate.data.repository

import com.tokopedia.topchat.chattemplate.data.source.CloudEditTemplateChatDataSource
import com.tokopedia.topchat.chattemplate.domain.pojo.TemplateData
import javax.inject.Inject

class EditTemplateRepositoryImpl @Inject constructor(
    private val cloudEditTemplateChatDataSource: CloudEditTemplateChatDataSource
): EditTemplateRepository {

    override suspend fun editTemplate(
        index: Int,
        map: Map<String, Any>,
        isSeller: Boolean
    ): TemplateData {
        return cloudEditTemplateChatDataSource.editTemplate(index, map, isSeller)
    }

    override suspend fun createTemplate(map: Map<String, Any>): TemplateData {
        return cloudEditTemplateChatDataSource.createTemplate(map)
    }

    override suspend fun deleteTemplate(index: Int, isSeller: Boolean): TemplateData {
        return cloudEditTemplateChatDataSource.deleteTemplate(index, isSeller)
    }
}