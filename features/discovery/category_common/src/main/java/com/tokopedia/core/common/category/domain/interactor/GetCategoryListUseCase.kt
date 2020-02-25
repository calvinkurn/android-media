package com.tokopedia.core.common.category.domain.interactor

import com.tokopedia.core.common.category.domain.model.CategoriesResponse
import com.tokopedia.graphql.coroutines.domain.interactor.MultiRequestGraphqlUseCase
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject

class GetCategoryListUseCase @Inject constructor(
        private val graphqlUseCase: MultiRequestGraphqlUseCase
) : UseCase<CategoriesResponse>() {

    companion object {

        const val PARAM_FILTER = "filter"

        @JvmStatic
        fun createRequestParams(filter: String = "seller"): RequestParams {
            val requestParams = RequestParams.create()
            requestParams.putString(PARAM_FILTER, filter)
            return requestParams
        }

        private val query = """
            query getCategoryListLite(${'$'}filter: String!){
                getCategoryListLite(filter:${'$'}filter){
                    categories {
                        name
                        children {
                            name
                            children {
                                name
                            }
                        }
                    }
                }
            }
        """.trimIndent()
    }

    override suspend fun executeOnBackground(): CategoriesResponse {
        val gqlRequest = GraphqlRequest(query, CategoriesResponse::class.java)
        graphqlUseCase.clearRequest()
        graphqlUseCase.addRequest(gqlRequest)
        val graphqlResponse = graphqlUseCase.executeOnBackground()
        return graphqlResponse.run {
            getData<CategoriesResponse>(CategoriesResponse::class.java)
        }
    }
}