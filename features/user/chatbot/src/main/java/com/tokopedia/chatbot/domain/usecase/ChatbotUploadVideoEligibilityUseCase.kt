package com.tokopedia.chatbot.domain.usecase

import com.tokopedia.chatbot.data.uploadEligibility.ChatbotUploadVideoEligibilityResponse
import com.tokopedia.chatbot.domain.gqlqueries.ChatbotUploadVideoEligibilityQuery
import com.tokopedia.chatbot.domain.gqlqueries.GQL_UPLOAD_VIDEO_ELIGIBILITY
import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import javax.inject.Inject

@GqlQuery("topbotUploadVideoEligibility", GQL_UPLOAD_VIDEO_ELIGIBILITY)
class ChatbotUploadVideoEligibilityUseCase @Inject constructor(
    graphqlRepository: GraphqlRepository
) : GraphqlUseCase<ChatbotUploadVideoEligibilityResponse>(graphqlRepository) {

    fun getVideoUploadEligibility(
        onSuccess: (ChatbotUploadVideoEligibilityResponse) -> Unit,
        onError: (Throwable) -> Unit,
        messageId: String
    ) {
        try {
            this.setTypeClass(ChatbotUploadVideoEligibilityResponse::class.java)
            this.setRequestParams(getParams(messageId))
            this.setGraphqlQuery(ChatbotUploadVideoEligibilityQuery())

            this.execute({ result ->
                onSuccess(result)
            }, { error ->
                onError(error)
            })
        } catch (throwable: Throwable) {
            onError(throwable)
        }
    }

    private fun getParams(messageId: String): Map<String, Any?> {
        return mapOf(
            msgId to messageId
        )
    }

    companion object {
        private const val msgId = "msgID"
    }

}