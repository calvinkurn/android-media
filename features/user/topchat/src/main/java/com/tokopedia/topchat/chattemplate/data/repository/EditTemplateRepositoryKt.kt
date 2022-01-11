package com.tokopedia.topchat.chattemplate.data.repository

import com.tokopedia.topchat.chattemplate.domain.pojo.TemplateData
import com.tokopedia.topchat.chattemplate.view.viewmodel.EditTemplateUiModel
import java.util.*

interface EditTemplateRepositoryKt {

    suspend fun editTemplate(
        index: Int,
        map: Map<String, Any>,
        isSeller: Boolean
    ): TemplateData

    suspend fun createTemplate(map: Map<String, Any>): TemplateData

    suspend fun deleteTemplate(index: Int, isSeller: Boolean): TemplateData
}