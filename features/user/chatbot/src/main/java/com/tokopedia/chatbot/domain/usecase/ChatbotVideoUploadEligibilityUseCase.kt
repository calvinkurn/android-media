package com.tokopedia.chatbot.domain.usecase

import com.tokopedia.chatbot.data.uploadeligibility.UploadVideoEligibilityResponse
import com.tokopedia.graphql.coroutines.domain.interactor.MultiRequestGraphqlUseCase
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.graphql.domain.GraphqlUseCase
import javax.inject.Inject

class ChatbotVideoUploadEligibilityUseCase @Inject constructor(private val graphqlUseCase: GraphqlUseCase) {

    suspend fun getUserVideoUploadEligibility(msgID: String): GraphqlResponse {

        val gql = MultiRequestGraphqlUseCase()
        val request = GraphqlRequest(query, UploadVideoEligibilityResponse::class.java, getParams(msgID))
        gql.addRequest(request)
        return gql.executeOnBackground()
    }

    private fun getParams(msgID: String) : Map<String,Any>? {
        return mapOf(
            "msgID" to msgID
        )
    }
}

const val query = """
    query topbotUploadVideoEligibility(${'$'}msgID: String!){
 topbotUploadVideoEligibility(msgID: ${'$'}msgID) {
    header {
        is_success
        reason
        messages
        error_code
    }
    data {
        is_eligible
    }
  }
}  
"""