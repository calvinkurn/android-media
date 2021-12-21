package com.tokopedia.topchat.chattemplate.data.repository

import com.tokopedia.topchat.chattemplate.domain.pojo.TemplateData

interface TemplateRepositoryKt {
    suspend fun getTemplateSuspend(parameters: Map<String, Boolean>): TemplateData
}