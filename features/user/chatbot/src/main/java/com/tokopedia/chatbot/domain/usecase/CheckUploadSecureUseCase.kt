package com.tokopedia.chatbot.domain.usecase

import com.tokopedia.basemvvm.repository.BaseRepository
import com.tokopedia.chatbot.data.uploadsecure.CheckUploadSecureResponse
import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.usecase.RequestParams
import javax.inject.Inject

const val UPLOAD_SECURE_QUERY = """query topbotUploadSecureAvailability(${'$'}msgId: String!, ${'$'}deviceID: String!) {
  topbotUploadSecureAvailability(msgID: ${'$'}msgId, deviceID: ${'$'}deviceID) {
    UploadSecureAvailabilityData {
      IsUsingUploadSecure
    }
  }
}"""

private const val DEVICE_ID = "deviceID"
private const val MSG_ID = "msgId"

@GqlQuery("UploadSecureQuery", UPLOAD_SECURE_QUERY)
class CheckUploadSecureUseCase @Inject constructor() {
    @Inject
    lateinit var baseRepository: BaseRepository

    suspend fun checkUploadSecure(params: RequestParams): CheckUploadSecureResponse {
        return baseRepository.getGQLData(
            UploadSecureQuery.GQL_QUERY,
            CheckUploadSecureResponse::class.java,
            params.parameters
        )
    }

    fun createRequestParams(msgId: String): RequestParams {
        val requestParams = RequestParams.create()
        requestParams.putString(MSG_ID, msgId)
        requestParams.putString(DEVICE_ID, "chatbot")
        return requestParams
    }
}