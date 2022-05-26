package com.tokopedia.chatbot.domain.usecase

import com.tokopedia.chatbot.data.inboxTicketList.InboxTicketListResponse
import com.tokopedia.chatbot.domain.gqlqueries.GQL_INBOX_LIST
import com.tokopedia.chatbot.domain.gqlqueries.InboxTicketListQuery
import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.interactor.MultiRequestGraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

@GqlQuery("TicketListQuery", GQL_INBOX_LIST)
class TicketListContactUsUsecase @Inject constructor(
    private val userSession: UserSessionInterface,
    graphqlRepository: GraphqlRepository
)  : GraphqlUseCase<InboxTicketListResponse>(graphqlRepository) {

    suspend fun getTicketList(): GraphqlResponse {
        val gql = MultiRequestGraphqlUseCase()
        val request = GraphqlRequest(
            requestQuery,
            InboxTicketListResponse::class.java,
            getParams()
        )
        gql.addRequest(request)
        return gql.executeOnBackground()
    }

    fun getTicketList(onSuccess : (InboxTicketListResponse) -> Unit,
                      onError : (Throwable) -> Unit
    ) {
        try {
            this.setTypeClass(InboxTicketListResponse::class.java)
            this.setRequestParams(getParams())
            this.setGraphqlQuery(InboxTicketListQuery())

            this.execute(
                { result ->
                    onSuccess(result)
                }, { error ->
                    onError(error)
                }
            )

        } catch (throwable : Throwable) {
            onError(throwable)
        }
    }


    private fun getParams(): Map<String, Any?> {
        return mapOf(
            USERID to userSession.userId,
            PAGE to pageNumber,
            STATUS to status
        )
    }

    companion object {
        private const val STATUS = "status"
        private const val USERID = "userID"
        private const val PAGE = "page"
        private const val pageNumber = 1
        private const val status = 0
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
