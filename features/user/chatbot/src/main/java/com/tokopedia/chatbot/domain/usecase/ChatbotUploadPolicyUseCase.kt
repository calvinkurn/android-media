package com.tokopedia.chatbot.domain.usecase

import com.tokopedia.chatbot.data.uploadPolicy.ChatbotVODUploadPolicyResponse
import com.tokopedia.chatbot.data.uploadeligibility.UploadVideoEligibilityResponse
import com.tokopedia.graphql.coroutines.domain.interactor.MultiRequestGraphqlUseCase
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.graphql.domain.GraphqlUseCase
import javax.inject.Inject

class ChatbotUploadPolicyUseCase @Inject constructor(private val graphqlUseCase: GraphqlUseCase) {

    suspend fun getChatbotUploadPolicy(source: String): GraphqlResponse {

        val gql = MultiRequestGraphqlUseCase()
        val request = GraphqlRequest(uploadPolicyQuery, ChatbotVODUploadPolicyResponse::class.java, getParams(source))
        gql.addRequest(request)
        return gql.executeOnBackground()
    }

    private fun getParams(source: String) : Map<String,Any>? {
        return mapOf(
            "source" to source
        )
    }
}
const val uploadPolicyQuery =
    """
query uploadpedia_policy(${'$'}source:String!)
{
  uploadpedia_policy(source: ${'$'}source) {
    source_policy {
      host
      timeout
      vod_policy {
        max_file_size
        allowed_ext
        simple_upload_size_threshold_mb
        big_upload_chunk_size_mb
        big_upload_max_concurrent
        timeout_transcode
        retry_interval
      }
    }
  }
}
"""