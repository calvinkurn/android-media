package com.tokopedia.stickylogin.domain.usecase.coroutine

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.stickylogin.domain.data.StickyLoginTickerDataModel
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.UseCase

class StickyLoginUseCase(
        private val graphqlUseCase: GraphqlUseCase<StickyLoginTickerDataModel.TickerResponse>
) : UseCase<StickyLoginTickerDataModel.TickerResponse>() {
    private val params: MutableMap<String, Any> = mutableMapOf()

    init {
        graphqlUseCase.setGraphqlQuery(query)
        graphqlUseCase.setCacheStrategy(GraphqlCacheStrategy.Builder(CacheType.ALWAYS_CLOUD).build())
        graphqlUseCase.setTypeClass(StickyLoginTickerDataModel.TickerResponse::class.java)
    }

    override suspend fun executeOnBackground(): StickyLoginTickerDataModel.TickerResponse {
        graphqlUseCase.clearCache()
        graphqlUseCase.setRequestParams(params)
        return graphqlUseCase.executeOnBackground()
    }

    fun setParam(params: RequestParams) {
        this.params.run {
            clear()
            putAll(params.parameters)
        }
    }

    companion object {
        private val query = """
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