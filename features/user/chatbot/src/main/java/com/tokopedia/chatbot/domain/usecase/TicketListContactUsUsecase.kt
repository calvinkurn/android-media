package com.tokopedia.chatbot.domain.usecase

import com.tokopedia.chatbot.data.inboxTicketList.InboxTicketListResponse
import com.tokopedia.graphql.coroutines.domain.interactor.MultiRequestGraphqlUseCase
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

class TicketListContactUsUsecase @Inject constructor(
    private val userSession: UserSessionInterface
) {

    suspend fun getChatbotUploadPolicy(): GraphqlResponse {
        val gql = MultiRequestGraphqlUseCase()
        val request = GraphqlRequest(
            requestQuery,
            InboxTicketListResponse::class.java,
            getParams()
        )
        gql.addRequest(request)
        return gql.executeOnBackground()
    }

    private fun getParams(): Map<String, Any>? {
        return mapOf(
            USERID to userSession.userId,
            PAGE to page,
            STATUS to status
        )
    }

    companion object {
        const val STATUS = "status"
        private const val USERID = "userID"
        private const val PAGE = "page"
        const val page = 1
        const val status = 0
        const val rating = 0
    }

}

private val requestQuery =
    """query TicketListQuery(${'$'}userID: String!, ${'$'}page: Int!, ${'$'}status: Int!, ${'$'}rating: Int) {
  ticket(userID: ${'$'}userID, page: ${'$'}page, status: ${'$'}status, rating: ${'$'}rating) {
    status
    data {
      is_success
      next_page
      previous_page
      tickets {
        id
        subject
        last_update
        read_status_id
        status_id
        url_detail
        message
        read_status
        status
        last_message
        need_rating
        is_official_store
        case_number
      }
      notice {
        is_active
        title
        subtitle
        content
        content_list
      }
      
    }
  }
}
""".trimIndent()
