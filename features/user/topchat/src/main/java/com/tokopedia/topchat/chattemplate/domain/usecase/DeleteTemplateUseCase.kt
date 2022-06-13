package com.tokopedia.topchat.chattemplate.domain.usecase

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.topchat.chattemplate.data.repository.EditTemplateRepository
import com.tokopedia.topchat.chattemplate.domain.pojo.TemplateData
import kotlinx.coroutines.withContext
import javax.inject.Inject

open class DeleteTemplateUseCase @Inject constructor(
    private val editTemplateRepository: EditTemplateRepository,
    private val dispatcher: CoroutineDispatchers
) {
    suspend fun deleteTemplate(index: Int, isSeller: Boolean): TemplateData {
        return withContext(dispatcher.io) {
            editTemplateRepository.deleteTemplate(index, isSeller)
        }
    }
}