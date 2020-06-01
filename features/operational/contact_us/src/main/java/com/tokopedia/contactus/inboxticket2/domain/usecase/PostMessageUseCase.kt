package com.tokopedia.contactus.inboxticket2.domain.usecase

import com.tokopedia.contactus.inboxticket2.data.ContactUsRepository
import com.tokopedia.contactus.inboxticket2.data.model.TicketReplyResponse
import com.tokopedia.usecase.RequestParams
import javax.inject.Inject
import javax.inject.Named

const val TICKET_ID = "ticketID"
const val MESSAGE = "message"
const val IS_IMAGE = "pPhoto"
const val IMAGE_AS_STRING = "pPhotoAll"
const val USER_ID = "userID"
const val AGENT_REPLY = "agentReply"

class PostMessageUseCase @Inject constructor(@Named("reply_ticket") val replyTicketQuery: String,
                                             private val repository: ContactUsRepository) {

    suspend fun getCreateTicketResult(requestParams: RequestParams): TicketReplyResponse {
        return repository.getGQLData(replyTicketQuery,
                TicketReplyResponse::class.java,
                requestParams.parameters)
    }

    fun createRequestParams(id: String, message: String, photo: Int, photoall: String, agentReply: String, userId: String): RequestParams {
        val requestParams = RequestParams.create()
        requestParams.putString(TICKET_ID, id)
        requestParams.putString(MESSAGE, message)
        requestParams.putString(AGENT_REPLY, agentReply)
        requestParams.putString(USER_ID, userId)
        requestParams.putInt(IS_IMAGE, photo)
        requestParams.putString(IMAGE_AS_STRING, photoall)
        return requestParams
    }


}