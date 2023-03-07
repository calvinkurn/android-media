package com.tokopedia.usercomponents.tokopediaplus.domain

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import javax.inject.Inject


/**
 * Get Tokopedia Plus content data & attributes
 *
 * ### See this code example
 * val response = tokopediaPlusUseCase(mapOf(TokopediaPlusUseCase.PARAM_SOURCE to source))
 */

class TokopediaPlusUseCase @Inject constructor(
    @ApplicationContext private val repository: GraphqlRepository,
    dispatchers: CoroutineDispatchers
) : CoroutineUseCase<Map<String, String>, TokopediaPlusResponseDataModel>(dispatchers.io) {

    override suspend fun execute(params: Map<String, String>): TokopediaPlusResponseDataModel {
        return repository.request(graphqlQuery(), params)
    }

    override fun graphqlQuery(): String {
        return """
            query TokopediaPlusWidget(${'$'}source: String) {
              tokopediaPlusWidget(source: ${'$'}source ) {
                isShown
                iconImageURL
                title
                subtitle
                url
                applink
                isSubscriber
              }
            }
        """.trimIndent()
    }

    companion object {
        const val PARAM_SOURCE = "source"
    }
}