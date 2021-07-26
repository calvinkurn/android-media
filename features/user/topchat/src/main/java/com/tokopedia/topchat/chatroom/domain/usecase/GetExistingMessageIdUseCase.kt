package com.tokopedia.topchat.chatroom.domain.usecase

import androidx.collection.ArrayMap
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.topchat.chatroom.domain.pojo.GetExistingMessageIdPojo
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

/**
 * @author by nisie on 09/01/19.
 */

class GetExistingMessageIdUseCase @Inject constructor(
        private val gqlUseCase: GraphqlUseCase<GetExistingMessageIdPojo>,
        private var dispatchers: CoroutineDispatchers
) : CoroutineScope {

    override val coroutineContext: CoroutineContext get() = dispatchers.main + SupervisorJob()

    fun getMessageId(
            toShopId: String,
            toUserId: String,
            source: String,
            onSuccessGetMessageId: (String) -> Unit,
            onError: (Throwable) -> Unit
    ) {
        launchCatchError(
                dispatchers.io,
                {
                    val params = generateParam(toShopId, toUserId, source)
                    val response = gqlUseCase.apply {
                        setTypeClass(GetExistingMessageIdPojo::class.java)
                        setRequestParams(params)
                        setGraphqlQuery(query)
                    }.executeOnBackground()
                    withContext(dispatchers.main) {
                        onSuccessGetMessageId(response.chatExistingChat.messageId)
                    }
                },
                {
                    withContext(dispatchers.main) {
                        onError(it)
                    }
                }
        )
    }

    fun generateParam(toShopId: String, toUserId: String, source: String): Map<String, Any> {
        val requestParams = ArrayMap<String, Any>()
        requestParams[PARAM_TO_SHOP_ID] = if (toShopId.isNotBlank()) toShopId.toLongOrZero() else 0
        requestParams[PARAM_TO_USER_ID] = if (toUserId.isNotBlank()) toUserId.toLongOrZero() else 0
        requestParams[PARAM_SOURCE] = source
        return requestParams
    }

    companion object {
        private val PARAM_TO_SHOP_ID: String = "toShopId"
        private val PARAM_TO_USER_ID: String = "toUserId"
        private val PARAM_SOURCE: String = "source"

        private val query = """
            query get_existing_message_id($$PARAM_TO_SHOP_ID: Int!, $$PARAM_TO_USER_ID: Int!, $$PARAM_SOURCE: String!) {
              chatExistingChat(toShopId: $$PARAM_TO_SHOP_ID, toUserId: $$PARAM_TO_USER_ID, source: $$PARAM_SOURCE) {
                messageId
              }
            }
        """
    }

}