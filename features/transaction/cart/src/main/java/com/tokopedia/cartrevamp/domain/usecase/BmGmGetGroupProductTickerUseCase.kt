package com.tokopedia.cartrevamp.domain.usecase

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.cartrevamp.domain.model.bmgm.request.BmGmGetGroupProductTickerParams
import com.tokopedia.cartrevamp.domain.model.bmgm.response.BmGmGetGroupProductTickerResponse
import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import javax.inject.Inject

@GqlQuery("GetGroupProductTickerQuery", BmGmGetGroupProductTickerUseCase.query)
class BmGmGetGroupProductTickerUseCase @Inject constructor(
    @ApplicationContext private val graphqlRepository: GraphqlRepository,
    dispatchers: CoroutineDispatchers
) :
    CoroutineUseCase<BmGmGetGroupProductTickerParams, BmGmGetGroupProductTickerResponse>(dispatchers.io) {

    override fun graphqlQuery(): String = query

    override suspend fun execute(params: BmGmGetGroupProductTickerParams): BmGmGetGroupProductTickerResponse {
        return graphqlRepository.request(GetGroupProductTickerQuery(), createVariables(params))
    }

    private fun createVariables(params: BmGmGetGroupProductTickerParams): Map<String, Any> {
        val variables = mutableMapOf<String, Any>()
        variables[KEY_PARAMS] = params
        return variables
    }

    companion object {
        private const val KEY_PARAMS = "params"
        const val query = """
            query getGroupProductTicker(${'$'}params: GetGroupProductTickerParams) {
                get_group_product_ticker(params:${'$'}params) {
                    error_message
                    status
                    data {
                        type
                        action
                        icon {
                            url
                        }
                        message {
                            text
                            url
                        }
                        discount_amount
                    }
                }
            }
            """
    }
}
