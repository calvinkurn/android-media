package com.tokopedia.contactus.inboxtickets.domain.usecase

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.contactus.inboxtickets.data.model.ChipInboxDetails
import com.tokopedia.contactus.inboxtickets.domain.usecase.param.CloseTicketByUserParam
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import javax.inject.Inject

const val CLOSE_TICKET_BY_USER = """
mutation chipCloseTicketByUser(${'$'}caseID: String!, ${'$'}source: String!) {
   chipCloseTicketByUser(caseID: ${'$'}caseID, source: ${'$'}source) {
     data {
       isSuccess
     }
     messageError
   }
 }
"""

class CloseTicketByUserUseCase @Inject constructor(
    @ApplicationContext private val repository: GraphqlRepository,
    dispatchers: CoroutineDispatchers
) : CoroutineUseCase<CloseTicketByUserParam, ChipInboxDetails>(dispatchers.io) {

    fun createRequestParams(caseID: String?, source: String?): CloseTicketByUserParam {
        return CloseTicketByUserParam(caseID= caseID.orEmpty(), source= source.orEmpty())
    }

    override fun graphqlQuery(): String {
        return CLOSE_TICKET_BY_USER
    }

    override suspend fun execute(params: CloseTicketByUserParam): ChipInboxDetails {
        return repository.request(graphqlQuery(), params)
    }
}
