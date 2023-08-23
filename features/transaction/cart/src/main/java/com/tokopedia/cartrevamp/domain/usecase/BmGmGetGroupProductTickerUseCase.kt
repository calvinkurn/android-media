package com.tokopedia.cartrevamp.domain.usecase

import com.google.gson.Gson
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.cartrevamp.domain.model.bmgm.request.BmGmGetGroupProductTickerParams
import com.tokopedia.cartrevamp.domain.model.bmgm.response.BmGmGetGroupProductTickerResponse
import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import javax.inject.Inject

@GqlQuery("GetGroupProductTickerQuery", BmGmGetGroupProductTickerUseCase.query)
class BmGmGetGroupProductTickerUseCase @Inject constructor(
    @ApplicationContext private val repository: GraphqlRepository,
    dispatchers: CoroutineDispatchers
) :
    CoroutineUseCase<BmGmGetGroupProductTickerParams, BmGmGetGroupProductTickerResponse>(dispatchers.io) {

    override fun graphqlQuery(): String = query

    override suspend fun execute(params: BmGmGetGroupProductTickerParams): BmGmGetGroupProductTickerResponse {
        val jsonRaw = """
            {
                "status":"OK",
                "error_message":[],
                "data":{
                    "type": "BMGM",
                    "action": "", // need action from client, if empty means success and no action required
                    "icon": {
                        "url": "http://tokopedia.com/icon.png"
                    },
                    "message":[
                        {
                            "text": "Potongan harga Rp 10Rb . Summer Sale Clearance!",
                            "url": ""
                        }
                    ],
                    "discount_amount": 10000
                }
            }
        """.trimIndent()

        val gson = Gson()

        // return repository.request(GetGroupProductTickerQuery(), createVariables(params))
        return gson.fromJson(jsonRaw, BmGmGetGroupProductTickerResponse::class.java)
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
