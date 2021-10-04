package com.tokopedia.topchat.chatroom.domain.usecase

import androidx.collection.ArrayMap
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.topchat.chatroom.domain.pojo.GetExistingMessageIdPojo
import javax.inject.Inject

class GetExistingMessageIdUseCaseNew @Inject constructor(
    private val repository: GraphqlRepository,
    dispatcher: CoroutineDispatchers
): CoroutineUseCase<Map<String, Any>, GetExistingMessageIdPojo>(dispatcher.io) {

    override fun graphqlQuery(): String = query

    override suspend fun execute(params: Map<String, Any>): GetExistingMessageIdPojo {
        return repository.request(graphqlQuery(), params)
    }

    fun generateParam(toShopId: String, toUserId: String, source: String): Map<String, Any> {
        val requestParams = ArrayMap<String, Any>()
        requestParams[PARAM_TO_SHOP_ID] = if (toShopId.isNotBlank()) toShopId.toLongOrZero() else 0
        requestParams[PARAM_TO_USER_ID] = if (toUserId.isNotBlank()) toUserId.toLongOrZero() else 0
        requestParams[PARAM_SOURCE] = source
        return requestParams
    }

    companion object {
        private const val PARAM_TO_SHOP_ID: String = "toShopId"
        private const val PARAM_TO_USER_ID: String = "toUserId"
        private const val PARAM_SOURCE: String = "source"

        private val query = """
            query get_existing_message_id($$PARAM_TO_SHOP_ID: Int!, $$PARAM_TO_USER_ID: Int!, $$PARAM_SOURCE: String!) {
              chatExistingChat(toShopId: $$PARAM_TO_SHOP_ID, toUserId: $$PARAM_TO_USER_ID, source: $$PARAM_SOURCE) {
                messageId
              }
            }
        """
    }
}