package com.tokopedia.contactus.inboxticket2.domain.usecase

import com.tokopedia.contactus.inboxticket2.data.ContactUsRepository
import com.tokopedia.contactus.inboxticket2.data.model.InboxTicketListResponse
import com.tokopedia.usecase.RequestParams
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject
import javax.inject.Named

const val STATUS = "status"
const val READ = "read"
const val RATING = "rating"
private const val USERID = "userID"
private const val PAGE = "page"
class GetTicketListUseCase @Inject constructor(@Named("ticket_list_query") val ticketListQuery: String,
                                               private val userSession: UserSessionInterface,
                                               private val repository: ContactUsRepository) {
    suspend fun getTicketListResponse(requestParams: RequestParams): InboxTicketListResponse {
        return repository.getGQLData(ticketListQuery, InboxTicketListResponse::class.java, requestParams.parameters)
    }

    fun getRequestParams(page: Int, status: Int, rating: Int = 0): RequestParams {
        val requestParams = RequestParams.create()
        requestParams.putString(USERID, userSession.userId)
        requestParams.putInt(PAGE, page)
        requestParams.putInt(STATUS, status)
        if (rating != 0) requestParams.putInt(RATING, rating)
        return requestParams
    }
}