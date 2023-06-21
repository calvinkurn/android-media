package com.tokopedia.chatbot.chatbot2.domain.usecase

import com.tokopedia.chatbot.chatbot2.data.uploadsecure.CheckUploadSecureResponse
import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import javax.inject.Inject

@GqlQuery(
    "topbotUploadSecureAvailability",
    com.tokopedia.chatbot.chatbot2.domain.gqlqueries.queries.UPLOAD_SECURE_QUERY
)
class ChatbotCheckUploadSecureUseCase @Inject constructor(
    graphqlRepository: GraphqlRepository
) : GraphqlUseCase<CheckUploadSecureResponse>(graphqlRepository) {

    fun checkUploadSecure(
        onSuccess: (CheckUploadSecureResponse) -> Unit,
        onError: kotlin.reflect.KFunction2<Throwable, String, Unit>,
        messageId: String
    ) {
        try {
            this.setTypeClass(CheckUploadSecureResponse::class.java)
            this.setRequestParams(getParams(messageId))
            this.setGraphqlQuery(com.tokopedia.chatbot.chatbot2.domain.gqlqueries.CheckUploadSecureQuery())

            this.execute({ result ->
                onSuccess(result)
            }, { error ->
                onError(error, messageId)
            })
        } catch (throwable: Throwable) {
            onError(throwable, messageId)
        }
    }

    private fun getParams(messageId: String): Map<String, Any?> {
        return mapOf(
            MSG_ID to messageId,
            DEVICE_ID to DEVICE_ID_NAME
        )
    }

    companion object {
        private const val MSG_ID = "msgId"
        private const val DEVICE_ID = "deviceID"
        private const val DEVICE_ID_NAME = "chatbot"
    }
}
