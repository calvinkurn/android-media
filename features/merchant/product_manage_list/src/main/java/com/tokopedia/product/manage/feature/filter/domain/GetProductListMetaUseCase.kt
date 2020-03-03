package com.tokopedia.product.manage.feature.filter.domain

import com.tokopedia.graphql.coroutines.domain.interactor.MultiRequestGraphqlUseCase
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.product.manage.feature.filter.data.model.ProductListMetaResponse
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject

class GetProductListMetaUseCase @Inject constructor(
        private val graphqlUseCase: MultiRequestGraphqlUseCase) : UseCase<ProductListMetaResponse>() {

    companion object {
        const val PARAM_SHOP_ID = "shopID"

        @JvmStatic
        fun createRequestParams(shopId: String): RequestParams {
            val requestParams = RequestParams.create()
            requestParams.putString(PARAM_SHOP_ID, shopId)
            return requestParams
        }

        private val query by lazy {
            val shopID = "\$shopID"
            """
            query ProductListMeta($shopID : String!){
                ProductListMeta(shopID:$shopID){
                    header{
                        processTime
                        messages
                        reason
                      errorCode
                    }
                    data{
                      tab{
                        id
                        name
                        value
                      }
                      filter{
                        id
                        name
                        value
                      }
                      sort{
                        id
                        name
                        value
                      }
                    }
                  }
                }
        """.trimIndent()
        }
    }

    var params: RequestParams = RequestParams.EMPTY

    override suspend fun executeOnBackground(): ProductListMetaResponse {
        val gqlRequest = GraphqlRequest(query, ProductListMetaResponse::class.java, params.parameters)
        graphqlUseCase.clearRequest()
        graphqlUseCase.addRequest(gqlRequest)
        val graphqlResponse = graphqlUseCase.executeOnBackground()
        return graphqlResponse.run {
            getData<ProductListMetaResponse>(ProductListMetaResponse::class.java)
        }
    }
}