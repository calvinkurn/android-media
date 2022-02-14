package com.tokopedia.stickylogin.domain.usecase

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.stickylogin.domain.data.StickyLoginTickerDataModel
import javax.inject.Inject

class StickyLoginUseCase @Inject constructor(
    @ApplicationContext private val repository: GraphqlRepository,
    dispatchers: CoroutineDispatchers
) : CoroutineUseCase<MutableMap<String, Any>, StickyLoginTickerDataModel.TickerResponse>(dispatchers.io) {

    override suspend fun execute(params: MutableMap<String, Any>): StickyLoginTickerDataModel.TickerResponse {
        return repository.request(graphqlQuery(), params)
    }

    override fun graphqlQuery(): String {
        return """
            query get_ticker(${'$'}page: String!) {
              ticker {
                tickers(page: ${'$'}page) {
                  message
                  layout
                }
              }
            }
        """.trimIndent()
    }
}