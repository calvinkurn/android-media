package com.tokopedia.contactus.inboxdetail.di

import android.content.Context
import com.google.gson.Gson
import com.tokopedia.contactus.inboxtickets.data.model.ChipInboxDetails
import com.tokopedia.contactus.inboxtickets.data.model.TicketReplyResponse
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.test.application.graphql.GqlMockUtil
import com.tokopedia.test.application.util.InstrumentationMockHelper
import com.tokopedia.contactus.test.R
import java.net.UnknownHostException

class FakeGraphqlRepositoryInboxDetail(private val context: Context, private val gson: Gson) :
    GraphqlRepository {

    private var isSetNotHappy = false
    private var typeOfStatusTicket: TypeOfStatusTicket = TypeOfStatusTicket.ON_PROGRESS

    fun setResponseTypeNotHappy(type: Boolean) {
        isSetNotHappy = type
    }

    fun setTypeOfTicket(typeOfStatusTicket: TypeOfStatusTicket) {
        this.typeOfStatusTicket = typeOfStatusTicket
    }

    private val inboxDetailOnProgress: ChipInboxDetails by lazy {
        gson.fromJson(
            InstrumentationMockHelper.getRawString(
                context,
                R.raw.detail_inbox_response_onprogres_ticket
            ),
            ChipInboxDetails::class.java
        )
    }

    private val inboxDetailOnProgressAfterAgentReplay: ChipInboxDetails by lazy {
        gson.fromJson(
            InstrumentationMockHelper.getRawString(
                context,
                R.raw.detail_inbox_response_on_progress_after_replay_agent
            ),
            ChipInboxDetails::class.java
        )
    }

    private val inboxDetailNeedRating: ChipInboxDetails by lazy {
        gson.fromJson(
            InstrumentationMockHelper.getRawString(
                context,
                R.raw.detail_inbox_response_need_rating_ticket
            ),
            ChipInboxDetails::class.java
        )
    }

    private val inboxDetailClosed: ChipInboxDetails by lazy {
        gson.fromJson(
            InstrumentationMockHelper.getRawString(
                context,
                R.raw.detail_inbox_response_close_ticket
            ),
            ChipInboxDetails::class.java
        )
    }

    private val inboxDetailFailed: ChipInboxDetails by lazy {
        gson.fromJson(
            InstrumentationMockHelper.getRawString(context, R.raw.detail_inbox_response_failed),
            ChipInboxDetails::class.java
        )
    }

    private val replayTicket: TicketReplyResponse by lazy {
        gson.fromJson(
            InstrumentationMockHelper.getRawString(context, R.raw.ticket_replay_response_success),
            TicketReplyResponse::class.java
        )
    }

    private val sendRating: ChipInboxDetails by lazy {
        gson.fromJson(
            InstrumentationMockHelper.getRawString(context, R.raw.submit_csat_ticket_response),
            ChipInboxDetails::class.java
        )
    }

    private val sendRatingFailed: ChipInboxDetails by lazy {
        gson.fromJson(
            InstrumentationMockHelper.getRawString(context, R.raw.submit_response_failed),
            ChipInboxDetails::class.java
        )
    }

    private val closeTicket: ChipInboxDetails by lazy {
        gson.fromJson(
            InstrumentationMockHelper.getRawString(context, R.raw.close_ticket_response),
            ChipInboxDetails::class.java
        )
    }

    private val closeTicketFailed: ChipInboxDetails by lazy {
        gson.fromJson(
            InstrumentationMockHelper.getRawString(context, R.raw.close_ticket_response_failed),
            ChipInboxDetails::class.java
        )
    }

    override suspend fun response(
        requests: List<GraphqlRequest>,
        cacheStrategy: GraphqlCacheStrategy
    ): GraphqlResponse {
        val query = requests.firstOrNull()?.query.orEmpty()
        return when {
            query.isQueryContains(DETAIL_INBOX) && !isSetNotHappy -> settingResponseOfDetailTicket(
                typeOfStatusTicket
            )
            query.isQueryContains(DETAIL_INBOX) && isSetNotHappy -> GqlMockUtil.createSuccessResponse(
                inboxDetailFailed
            )
            query.isQueryContains(REPLAY_TICKET) && !isSetNotHappy -> GqlMockUtil.createSuccessResponse(
                replayTicket
            )
            query.isQueryContains(REPLAY_TICKET) && isSetNotHappy -> throw UnknownHostException("bad Request")
            isRatingQuery(requests) && !isSetNotHappy -> GqlMockUtil.createSuccessResponse(
                sendRating
            )
            isRatingQuery(requests) && isSetNotHappy -> GqlMockUtil.createSuccessResponse(
                sendRatingFailed
            )
            query.isQueryContains(CLOSE_TICKET) && !isSetNotHappy -> GqlMockUtil.createSuccessResponse(
                closeTicket
            )
            query.isQueryContains(CLOSE_TICKET) && isSetNotHappy -> GqlMockUtil.createSuccessResponse(
                closeTicketFailed
            )
            else -> throw UnknownHostException("bad request")
        }
    }

    private fun settingResponseOfDetailTicket(typeOfStatusTicket: TypeOfStatusTicket): GraphqlResponse {
        return when (typeOfStatusTicket) {
            TypeOfStatusTicket.ON_PROGRESS -> GqlMockUtil.createSuccessResponse(
                inboxDetailOnProgress
            )
            TypeOfStatusTicket.NEED_RATING -> GqlMockUtil.createSuccessResponse(
                inboxDetailNeedRating
            )
            TypeOfStatusTicket.CLOSE -> GqlMockUtil.createSuccessResponse(
                inboxDetailClosed
            )
            TypeOfStatusTicket.ON_PROGRESS_NEED_TO_REPLAY -> GqlMockUtil.createSuccessResponse(
                inboxDetailOnProgressAfterAgentReplay
            )
        }

    }

    private fun isRatingQuery(requests: List<GraphqlRequest>): Boolean {
        val query = requests.firstOrNull()?.query.orEmpty()
        return query.isQueryContains(SEND_RATING) && query.isQueryContains(PARAMS_RATING)
    }

    private fun String.isQueryContains(key: String): Boolean {
        return this.contains(key)
    }

    companion object {
        const val DETAIL_INBOX = "chipGetInboxDetailQuery"
        const val SEND_RATING = "SubmitRatingCSAT"
        const val PARAMS_RATING = "rating"
        const val CLOSE_TICKET = "chipCloseTicketByUser"
        const val REPLAY_TICKET = "replyTicket"
    }

    enum class TypeOfStatusTicket {
        ON_PROGRESS,ON_PROGRESS_NEED_TO_REPLAY, NEED_RATING, CLOSE
    }
}
