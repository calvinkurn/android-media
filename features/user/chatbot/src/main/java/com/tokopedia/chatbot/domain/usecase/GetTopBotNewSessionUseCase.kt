package com.tokopedia.chatbot.domain.usecase

import com.tokopedia.basemvvm.repository.BaseRepository
import com.tokopedia.chatbot.data.newsession.TopBotNewSessionResponse
import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.usecase.RequestParams
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

const val GET_NEW_SESSION_QUERY = """query topbotGetNewSession(${'$'}msgId :String!, ${'$'}userId :String!){
    topbotGetNewSession(msgID: ${'$'}msgId, userID: ${'$'}userId) {
    isNewSession
  }
  }"""

private const val USER_ID = "userId"
private const val MSG_ID = "msgId"

@GqlQuery("GetNewSessionQuery", GET_NEW_SESSION_QUERY)
class GetTopBotNewSessionUseCase @Inject constructor(private val userSession: UserSessionInterface) {
    @Inject
    lateinit var baseRepository: BaseRepository

    suspend fun getTobBotUserSession(params: RequestParams): TopBotNewSessionResponse {
        return baseRepository.getGQLData(
            GetNewSessionQuery.GQL_QUERY,
            TopBotNewSessionResponse::class.java,
            params.parameters
        )
    }

    fun createRequestParams(msgId: String): RequestParams {
        val requestParams = RequestParams.create()
        requestParams.putString(MSG_ID, msgId)
        requestParams.putString(USER_ID, userSession.userId)
        return requestParams
    }
}