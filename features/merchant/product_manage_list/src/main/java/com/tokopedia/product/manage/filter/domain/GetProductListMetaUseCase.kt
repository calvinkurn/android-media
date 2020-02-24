package com.tokopedia.product.manage.filter.domain

import com.tokopedia.graphql.coroutines.domain.interactor.MultiRequestGraphqlUseCase
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.product.manage.common.GQLQueryConstant
import com.tokopedia.product.manage.filter.data.model.ProductListMetaResponse
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject
import javax.inject.Named

class GetProductListMetaUseCase @Inject constructor(
        private val graphqlUseCase: MultiRequestGraphqlUseCase,
        @Named(GQLQueryConstant.QUERY_PRODUCTLIST_META) val query: String) : UseCase<ProductListMetaResponse>() {

    companion object {
        private const val PARAM_SHOP_ID = "shopID"

        @JvmStatic
        fun createRequestParams(shopId: String): RequestParams {
            val requestParams = RequestParams.create()
            requestParams.putString(PARAM_SHOP_ID, shopId)
            return requestParams
        }
    }

    override suspend fun executeOnBackground(): ProductListMetaResponse {
        val gqlRequest = GraphqlRequest(query, ProductListMetaResponse::class.java)
        graphqlUseCase.clearRequest()
        graphqlUseCase.addRequest(gqlRequest)
        val graphqlResponse = graphqlUseCase.executeOnBackground()
        return graphqlResponse.run {
            getData<ProductListMetaResponse>(ProductListMetaResponse::class.java)
        }
    }
}