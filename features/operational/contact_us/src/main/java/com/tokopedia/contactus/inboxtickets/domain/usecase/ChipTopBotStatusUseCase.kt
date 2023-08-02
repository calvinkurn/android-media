package com.tokopedia.contactus.inboxtickets.domain.usecase

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.contactus.inboxtickets.data.model.ChipTopBotStatusResponse
import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import javax.inject.Inject

const val CHIP_TOP_BOT_STATUS_QUERY = """
{
  chipTopBotStatusInbox() {
    status
    data {
      is_active
      is_success
      message_id
      welcome_message
	  unread_notif
      is_chatbot_active
    }
    message_error
  }
}
"""

@GqlQuery("ChipTopBotStatusQuery", CHIP_TOP_BOT_STATUS_QUERY)
class ChipTopBotStatusUseCase @Inject constructor(
    @ApplicationContext private val repository: GraphqlRepository,
    dispatchers: CoroutineDispatchers
) : CoroutineUseCase<Unit, ChipTopBotStatusResponse>(dispatchers.io){

    override fun graphqlQuery(): String {
        return CHIP_TOP_BOT_STATUS_QUERY
    }

    override suspend fun execute(params: Unit): ChipTopBotStatusResponse {
        return repository.request(graphqlQuery(), params)
    }
}
