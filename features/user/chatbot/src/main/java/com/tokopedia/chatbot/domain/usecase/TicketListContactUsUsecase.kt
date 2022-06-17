package com.tokopedia.chatbot.domain.usecase

import com.tokopedia.chatbot.data.inboxTicketList.InboxTicketListResponse
import com.tokopedia.chatbot.domain.gqlqueries.queries.GQL_INBOX_LIST
import com.tokopedia.chatbot.domain.gqlqueries.InboxTicketListQuery
import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

@GqlQuery("TicketListQuery", GQL_INBOX_LIST)
class TicketListContactUsUsecase @Inject constructor(
    private val userSession: UserSessionInterface,
    graphqlRepository: GraphqlRepository
)  : GraphqlUseCase<InboxTicketListResponse>(graphqlRepository) {

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
