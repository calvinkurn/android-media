package com.tokopedia.contactus.inboxticket2.domain.usecase

import com.tokopedia.basemvvm.repository.BaseRepository
import com.tokopedia.contactus.inboxticket2.data.model.ChipTopBotStatusResponseModel
import com.tokopedia.gql_query_annotation.GqlQuery
import javax.inject.Inject

const val CHIP_TOP_BOT_STATUS_QUERY = """
{
  chipTopBotStatus() {
    status
    server_process_time
    data {
      is_active
      is_success
      message_id
      welcome_message
	  unread_notif
    }
    message_error
  }
}
"""

@GqlQuery("ChipTopBotStatusQuery", CHIP_TOP_BOT_STATUS_QUERY)
class ChipTopBotStatusUseCase @Inject constructor() : BaseRepository() {

    suspend fun getChipTopBotStatus(): ChipTopBotStatusResponseModel.ChipTopBotStatusResponse? {
        return getGQLData(ChipTopBotStatusQuery.GQL_QUERY, ChipTopBotStatusResponseModel::class.java, emptyMap()).chipTopBotStatusResponse
    }
}