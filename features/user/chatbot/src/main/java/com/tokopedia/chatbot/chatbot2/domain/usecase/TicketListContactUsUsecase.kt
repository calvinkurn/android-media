package com.tokopedia.chatbot.chatbot2.domain.usecase

import com.tokopedia.chatbot.chatbot2.data.inboxTicketList.InboxTicketListResponse
import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

@GqlQuery("TicketListQuery", com.tokopedia.chatbot.chatbot2.domain.gqlqueries.queries.GQL_INBOX_LIST)
class TicketListContactUsUsecase @Inject constructor(
    private val userSession: UserSessionInterface,
    graphqlRepository: GraphqlRepository
) : GraphqlUseCase<InboxTicketListResponse>(graphqlRepository) {

    fun getTicketList(
        onSuccess: (InboxTicketListResponse) -> Unit,
        onError: (Throwable) -> Unit
    ) {
        try {
            this.setTypeClass(InboxTicketListResponse::class.java)
            this.setRequestParams(getParams())
            this.setGraphqlQuery(com.tokopedia.chatbot.chatbot2.domain.gqlqueries.InboxTicketListQuery())

            this.execute(
                { result ->
                    onSuccess(result)
                },
                { error ->
                    onError(error)
                }
            )
        } catch (throwable: Throwable) {
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
