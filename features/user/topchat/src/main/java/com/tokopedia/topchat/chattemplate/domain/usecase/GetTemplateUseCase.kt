package com.tokopedia.topchat.chattemplate.domain.usecase

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.topchat.chattemplate.data.repository.TemplateRepository
import com.tokopedia.topchat.chattemplate.domain.pojo.TemplateData
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GetTemplateUseCase @Inject constructor(
    private val templateRepository: TemplateRepository,
    private val dispatcher: CoroutineDispatchers
) {
    suspend fun getTemplate(isSeller: Boolean): TemplateData {
        return withContext(dispatcher.io) {
            val params = generateParam(isSeller)
            templateRepository.getTemplateSuspend(params)
        }
    }

    private fun generateParam(isSeller: Boolean): Map<String, Boolean> {
        return mapOf(PARAM_IS_SELLER to isSeller)
    }

    companion object {
        private const val PARAM_IS_SELLER = "is_seller"
    }
}