package com.tokopedia.topchat.chattemplate.domain.usecase

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.topchat.chattemplate.data.repository.EditTemplateRepository
import com.tokopedia.topchat.chattemplate.domain.pojo.TemplateData
import kotlinx.coroutines.withContext
import javax.inject.Inject

open class CreateTemplateUseCase @Inject constructor(
    private val editTemplateRepository: EditTemplateRepository,
    private val dispatcher: CoroutineDispatchers
) {
    suspend fun createTemplate(value: String, isSeller: Boolean): TemplateData {
        return withContext(dispatcher.io) {
            val params = generateParam(value, isSeller)
            editTemplateRepository.createTemplate(params)
        }
    }

    private fun generateParam(value: String, isSeller: Boolean): Map<String, Any> {
        return mapOf(
            PARAM_IS_SELLER to isSeller,
            PARAM_VALUE to value
        )
    }

    companion object {
        private const val PARAM_IS_SELLER = "is_seller"
        private const val PARAM_VALUE = "value"
    }
}