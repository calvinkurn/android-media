package com.tokopedia.topchat.chattemplate.domain.usecase

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.topchat.chattemplate.data.repository.EditTemplateRepository
import com.tokopedia.topchat.chattemplate.domain.pojo.TemplateData
import kotlinx.coroutines.withContext
import javax.inject.Inject

open class EditTemplateUseCase @Inject constructor(
    private val editTemplateRepository: EditTemplateRepository,
    private val dispatcher: CoroutineDispatchers
) {

    suspend fun editTemplate(index: Int, value: String, isSeller: Boolean): TemplateData {
        return withContext(dispatcher.io) {
            val params = generateParam(index, value, isSeller)
            editTemplateRepository.editTemplate(index, params, isSeller)
        }
    }

    private fun generateParam(index: Int, value: String, isSeller: Boolean): Map<String, Any> {
        return mapOf(
            PARAM_VALUE to value,
            PARAM_INDEX to index,
            PARAM_IS_SELLER to isSeller
        )
    }

    companion object {
        private const val PARAM_VALUE = "value"
        private const val PARAM_INDEX = "index"
        private const val PARAM_IS_SELLER = "is_seller"
    }
}