package com.tokopedia.contactus.inboxtickets.domain.usecase

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.contactus.inboxtickets.data.gqlqueries.TICKET_LIST_QUERY
import com.tokopedia.contactus.inboxtickets.data.model.InboxTicketListResponse
import com.tokopedia.contactus.inboxtickets.domain.usecase.param.GetTicketListParam
import com.tokopedia.contactus.utils.CommonConstant.FIRST_INITIALIZE_ZERO
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.usecase.RequestParams
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

open class GetTicketListUseCase @Inject constructor(
    private val userSession: UserSessionInterface,
    @ApplicationContext private val repository: GraphqlRepository,
    dispatchers: CoroutineDispatchers
): CoroutineUseCase<GetTicketListParam, InboxTicketListResponse>(dispatchers.io)  {

    companion object {
        private const val INITIAL_VALUE = 0L
    }

    fun getRequestParams(page: Int, status: Int, rating: Int = 0): GetTicketListParam {
        return GetTicketListParam(
            userID = userSession.userId,
            page = page,
            status = status,
            if (rating != FIRST_INITIALIZE_ZERO) rating else null
        )
    }

    override fun graphqlQuery(): String {
        return TICKET_LIST_QUERY
    }

    override suspend fun execute(params: GetTicketListParam): InboxTicketListResponse {
        return repository.request(graphqlQuery(), params)
    }
}
