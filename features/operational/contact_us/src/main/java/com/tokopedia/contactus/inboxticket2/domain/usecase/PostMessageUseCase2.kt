package com.tokopedia.contactus.inboxticket2.domain.usecase

import com.tokopedia.contactus.inboxticket2.data.ContactUsRepository
import com.tokopedia.contactus.inboxticket2.domain.StepTwoResponse
import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.usecase.RequestParams
import javax.inject.Inject

const val FILE_UPLOADED = "fileUploaded"
const val POST_KEY = "postKey"
const val TICKETID = "ticketID"

private const val TICKET_REPLY_ATTACH: String = """
    mutation ticketReplyAttach(${'$'}ticketID: String, ${'$'}userID: String!, ${'$'}postKey: String, ${'$'}fileUploaded: String) {
  ticket_reply_attach(ticketID: ${'$'}ticketID, userID: ${'$'}userID, postKey: ${'$'}postKey, fileUploaded: ${'$'}fileUploaded) {
    data {
      status
      is_success
    }
  }
}"""

@GqlQuery("TicketReplyAttach", TICKET_REPLY_ATTACH)
class PostMessageUseCase2 @Inject internal constructor(private val repository: ContactUsRepository) {

    suspend fun getInboxDataResponse(requestParams: RequestParams): StepTwoResponse? {
        return repository.getGQLData(TicketReplyAttach.GQL_QUERY, StepTwoResponse::class.java, requestParams.parameters)

    }

    fun createRequestParams(ticketId: String, userId: String, fileUploaded: String, postKey: String): RequestParams {
        val requestParams = RequestParams.create()
        requestParams.putString(USER_ID,userId)
        requestParams.putString(FILE_UPLOADED, fileUploaded)
        requestParams.putString(POST_KEY, postKey)
        requestParams.putString(TICKETID, ticketId)
        return requestParams
    }


}