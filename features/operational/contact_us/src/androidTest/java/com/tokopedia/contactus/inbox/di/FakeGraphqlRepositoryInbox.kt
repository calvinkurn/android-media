package com.tokopedia.contactus.inbox.di

import android.content.Context
import com.google.gson.Gson
import com.tokopedia.contactus.inboxtickets.data.model.ChipInboxDetails
import com.tokopedia.contactus.inboxtickets.data.model.ChipTopBotStatusResponse
import com.tokopedia.contactus.inboxtickets.data.model.InboxTicketListResponse
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.test.application.graphql.GqlMockUtil
import com.tokopedia.test.application.util.InstrumentationMockHelper
import com.tokopedia.contactus.test.R
import java.net.UnknownHostException

class FakeGraphqlRepositoryInbox(private val context: Context, private val gson: Gson) :
    GraphqlRepository {

    private var isError = false
    fun setResponseTypeForError(type: Boolean) {
        isError = type
    }

    private val listTicketResponse: InboxTicketListResponse by lazy {
        gson.fromJson(
            InstrumentationMockHelper.getRawString(context, R.raw.ticket_list_querry),
            InboxTicketListResponse::class.java
        )
    }

    private val topChatStatus: ChipTopBotStatusResponse by lazy {
        gson.fromJson(
            InstrumentationMockHelper.getRawString(context, R.raw.chip_top_bot_status),
            ChipTopBotStatusResponse::class.java
        )
    }

    private val inboxDetail: ChipInboxDetails by lazy {
        gson.fromJson(
            InstrumentationMockHelper.getRawString(context, R.raw.detail_inbox_response_onprogres_ticket),
            ChipInboxDetails::class.java
        )
    }

    override suspend fun response(
        requests: List<GraphqlRequest>,
        cacheStrategy: GraphqlCacheStrategy
    ): GraphqlResponse {
        val query = requests.firstOrNull()?.query.orEmpty()
        return when {
            query.isQueryContains(TICKET_REQ_LIST) && !isError -> GqlMockUtil.createSuccessResponse(listTicketResponse)
            query.isQueryContains(TICKET_REQ_LIST) && isError -> throw UnknownHostException("bad Request")
            query.isQueryContains(TOP_CHAT) && !isError -> GqlMockUtil.createSuccessResponse(topChatStatus)
            query.isQueryContains(TOP_CHAT) && isError-> throw UnknownHostException("bad Request")
            query.isQueryContains(DETAIL_INBOX) && !isError -> GqlMockUtil.createSuccessResponse(inboxDetail)
            query.isQueryContains(DETAIL_INBOX) && isError-> throw UnknownHostException("bad Request")
            else -> throw UnknownHostException("bad request")
        }
    }

    private fun String.isQueryContains(key : String) : Boolean{
        return this.contains(key)
    }

    companion object {
        const val TICKET_REQ_LIST = "ticket"
        const val TOP_CHAT = "chipTopBotStatusInbox"
        const val DETAIL_INBOX = "chipGetInboxDetailQuery"

    }
}
