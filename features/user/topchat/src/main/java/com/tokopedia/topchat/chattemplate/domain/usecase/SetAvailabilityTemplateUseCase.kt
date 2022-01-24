package com.tokopedia.topchat.chattemplate.domain.usecase

import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.topchat.chattemplate.data.repository.TemplateRepositoryKt
import com.tokopedia.topchat.chattemplate.domain.pojo.TemplateData
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SetAvailabilityTemplateUseCase @Inject constructor(
    private val templateRepository: TemplateRepositoryKt,
    private val dispatcher: CoroutineDispatchers
) {
    suspend fun setAvailability(params: JsonObject): TemplateData {
        return withContext(dispatcher.io) {
            templateRepository.setAvailabilityTemplate(params, params[PARAM_IS_SELLER].asBoolean)
        }
    }

    companion object {
        private const val PARAM_POSITION = "position"
        private const val PARAM_IS_SELLER = "is_seller"
        private const val PARAM_IS_ENABLE = "is_enable"

        fun getAvailabilityJson(
            jsonArray: JsonArray?,
            isSeller: Boolean,
            isEnabled: Boolean
        ): JsonObject {
            return JsonObject().apply {
                if (jsonArray != null) {
                    add(PARAM_POSITION, jsonArray)
                }
                addProperty(PARAM_IS_SELLER, isSeller)
                addProperty(PARAM_IS_ENABLE, isEnabled)
            }
        }

        fun toJsonArray(list: List<Int>): JsonArray {
            val array = JsonArray()
            for (o in list) {
                array.add(o)
            }
            return array
        }
    }
}