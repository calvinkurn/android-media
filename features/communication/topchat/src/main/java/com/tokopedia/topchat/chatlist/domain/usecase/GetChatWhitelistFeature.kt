package com.tokopedia.topchat.chatlist.domain.usecase

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.flow.FlowUseCase
import com.tokopedia.topchat.chatlist.domain.pojo.whitelist.ChatWhitelistFeatureResponse
import com.tokopedia.topchat.common.data.TopChatResult
import com.tokopedia.topchat.common.data.asFlowResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

open class GetChatWhitelistFeature @Inject constructor(
    @ApplicationContext private val repository: GraphqlRepository,
    dispatchers: CoroutineDispatchers
) : FlowUseCase<String, TopChatResult<ChatWhitelistFeatureResponse>>(dispatchers.io) {

    private val paramFeature = "feature"

    override fun graphqlQuery(): String = query

    override suspend fun execute(
        params: String
    ): Flow<TopChatResult<ChatWhitelistFeatureResponse>> {
        return flow {
            val mapParams = generateParams(params)
            val result = repository.request<Map<String, Any>, ChatWhitelistFeatureResponse>(
                graphqlQuery(),
                mapParams
            )
            emit(result)
        }.asFlowResult()
    }

    private fun generateParams(feature: String): Map<String, Any> {
        return mapOf(
            paramFeature to feature
        )
    }

    private val query = """
        query chatWhitelistFeature($$paramFeature: String){
          chatWhitelistFeature(feature: $$paramFeature){
            isWhitelist
          }
        }
    """.trimIndent()

    companion object {
        const val PARAM_VALUE_FEATURE_TOPBOT = "topbot"
    }
}
