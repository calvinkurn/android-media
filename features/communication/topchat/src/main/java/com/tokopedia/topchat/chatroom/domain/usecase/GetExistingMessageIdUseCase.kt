package com.tokopedia.topchat.chatroom.domain.usecase

import androidx.collection.ArrayMap
import com.google.gson.annotations.SerializedName
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.topchat.chatroom.domain.pojo.GetExistingMessageIdPojo
import javax.inject.Inject

open class GetExistingMessageIdUseCase @Inject constructor(
    private val repository: GraphqlRepository,
    dispatcher: CoroutineDispatchers
): CoroutineUseCase<GetExistingMessageIdUseCase.Param, GetExistingMessageIdPojo>(dispatcher.io) {

    override suspend fun execute(params: Param): GetExistingMessageIdPojo {
        val param = generateParam(params)
        return repository.request(graphqlQuery(), param)
    }

    private fun generateParam(existingMessageIdParam: Param): Map<String, Any> {
        val requestParams = ArrayMap<String, Any>()
        requestParams[PARAM_TO_SHOP_ID] = if (existingMessageIdParam.toShopId.isNotBlank()) {
            existingMessageIdParam.toShopId.toLongOrZero()
        } else 0
        requestParams[PARAM_TO_USER_ID] = if (existingMessageIdParam.toUserId.isNotBlank()) {
            existingMessageIdParam.toUserId.toLongOrZero()
        } else 0
        requestParams[PARAM_SOURCE] = existingMessageIdParam.source
        return requestParams
    }

    override fun graphqlQuery(): String = """
            query get_existing_message_id($$PARAM_TO_SHOP_ID: Int!, $$PARAM_TO_USER_ID: Int!, $$PARAM_SOURCE: String!) {
              chatExistingChat(toShopId: $$PARAM_TO_SHOP_ID, toUserId: $$PARAM_TO_USER_ID, source: $$PARAM_SOURCE) {
                messageId
              }
            }
        """

    data class Param (
        @SerializedName(PARAM_TO_SHOP_ID)
        var toShopId: String = "",

        @SerializedName(PARAM_TO_USER_ID)
        var toUserId: String = "",

        @SerializedName(PARAM_SOURCE)
        var source: String = ""
    )

    companion object {
        private const val PARAM_TO_SHOP_ID: String = "toShopId"
        private const val PARAM_TO_USER_ID: String = "toUserId"
        private const val PARAM_SOURCE: String = "source"
    }
}