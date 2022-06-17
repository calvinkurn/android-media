package com.tokopedia.chatbot.domain.usecase

import com.tokopedia.basemvvm.repository.BaseRepository
import com.tokopedia.chatbot.data.newsession.TopBotNewSessionResponse
import com.tokopedia.chatbot.domain.gqlqueries.GetTopBotNewSessionQuery
import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.usecase.RequestParams
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

const val GET_NEW_SESSION_QUERY = """query topbotGetNewSession(${'$'}msgId :String!, ${'$'}userId :String!){
    topbotGetNewSession(msgID: ${'$'}msgId, userID: ${'$'}userId) {
    isNewSession
    isTypingBlocked
  }
  }"""

private const val USER_ID = "userId"
private const val MSG_ID = "msgId"

@GqlQuery("GetNewSessionQuery", GET_NEW_SESSION_QUERY)
class GetTopBotNewSessionUseCase @Inject constructor(
    private val userSession: UserSessionInterface,
    graphqlRepository: GraphqlRepository
) : GraphqlUseCase<TopBotNewSessionResponse>(graphqlRepository)
{
    @Inject
    lateinit var baseRepository: BaseRepository

    fun getTopBotUserSession(
        onSuccess : (TopBotNewSessionResponse) -> Unit,
        onError : (Throwable) -> Unit,
        msgId : String
    ) {
        try {
            this.setTypeClass(TopBotNewSessionResponse::class.java)
            this.setRequestParams(createParams(msgId))
            this.setGraphqlQuery(GetTopBotNewSessionQuery())

            this.execute(
                {
                    result ->
                        onSuccess(result)
                }, {
                    error ->
                        onError(error)
                }
            )
        } catch (throwable : Throwable) {
            onError(throwable)
        }
    }


    suspend fun getTobBotUserSession(params: RequestParams): TopBotNewSessionResponse {
        return baseRepository.getGQLData(
            GetNewSessionQuery.GQL_QUERY,
            TopBotNewSessionResponse::class.java,
            params.parameters
        )
    }

    private fun createParams(msgId : String) : Map<String,Any> {
        return mapOf(
            MSG_ID to msgId,
            USER_ID to userSession.userId
        )

    }

    fun createRequestParams(msgId: String): RequestParams {
        val requestParams = RequestParams.create()
        requestParams.putString(MSG_ID, msgId)
        requestParams.putString(USER_ID, userSession.userId)
        return requestParams
    }
}