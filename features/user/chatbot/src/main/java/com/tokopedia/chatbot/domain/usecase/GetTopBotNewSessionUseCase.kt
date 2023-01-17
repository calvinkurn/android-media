package com.tokopedia.chatbot.domain.usecase

import com.tokopedia.basemvvm.repository.BaseRepository
import com.tokopedia.chatbot.data.newsession.TopBotNewSessionResponse
import com.tokopedia.chatbot.domain.gqlqueries.GQL_GET_TOP_BOT_NEW_SESSION
import com.tokopedia.chatbot.domain.gqlqueries.GetTopBotNewSessionQuery
import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

@GqlQuery("GetNewSessionQuery", GQL_GET_TOP_BOT_NEW_SESSION)
class GetTopBotNewSessionUseCase @Inject constructor(
    private val userSession: UserSessionInterface,
    graphqlRepository: GraphqlRepository
) : GraphqlUseCase<TopBotNewSessionResponse>(graphqlRepository) {
    @Inject
    lateinit var baseRepository: BaseRepository

    fun getTopBotUserSession(
        onSuccess: (TopBotNewSessionResponse) -> Unit,
        onError: kotlin.reflect.KFunction2<Throwable, String, Unit>,
        msgId: String
    ) {
        try {
            this.setTypeClass(TopBotNewSessionResponse::class.java)
            this.setRequestParams(createParams(msgId))
            this.setGraphqlQuery(GetTopBotNewSessionQuery())

            this.execute(
                { result ->
                    onSuccess(result)
                }, { error ->
                    onError(error, msgId)
                }
            )
        } catch (throwable: Throwable) {
            onError(throwable, msgId)
        }
    }

    private fun createParams(msgId: String): Map<String, Any> {
        return mapOf(
            MSG_ID to msgId,
            USER_ID to userSession.userId
        )

    }

    companion object {
        private const val USER_ID = "userId"
        private const val MSG_ID = "msgId"
    }

}
