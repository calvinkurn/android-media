package com.tokopedia.shop_showcase.shop_showcase_management.domain


import com.tokopedia.abstraction.common.network.exception.MessageErrorException
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.interactor.MultiRequestGraphqlUseCase
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.shop_showcase.common.GQLQueryConstant
import com.tokopedia.shop_showcase.shop_showcase_management.data.model.GetShopProductsResponse
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject
import javax.inject.Named


class GetShopShowcaseTotalProductUseCase @Inject constructor(
        private val graphqlUseCase: MultiRequestGraphqlUseCase,
        @Named(GQLQueryConstant.QUERY_SHOP_SHOWCASE_GET_TOTAL_PRODUCTS) val queryGetTotalProducts: String
): UseCase<GetShopProductsResponse>() {

    var params: RequestParams = RequestParams.EMPTY

    companion object{
        private const val PARAM_SHOP_ID = "shopId"
        private const val PARAM_FILTER = "filter"

        fun createRequestParam(shopId: String, filter: Map<String, Any>): RequestParams = RequestParams.create().apply {
            putString(PARAM_SHOP_ID, shopId)
            putObject(PARAM_FILTER, filter)
        }
    }

    override suspend fun executeOnBackground(): GetShopProductsResponse {
        val shopProductQuery = GraphqlRequest(queryGetTotalProducts, GetShopProductsResponse::class.java, params.parameters)
        graphqlUseCase.clearRequest()
        graphqlUseCase.addRequest(shopProductQuery)
        val gqlShopProductResponse = graphqlUseCase.executeOnBackground()
        val error = gqlShopProductResponse.getError(GetShopProductsResponse::class.java) ?: listOf()
        if (error == null || error.isEmpty()) {
            return gqlShopProductResponse.run {
                getData<GetShopProductsResponse>(GetShopProductsResponse::class.java)
            }
        } else {
            throw MessageErrorException(error.mapNotNull {
                it.message
            }.joinToString(separator = ", "))
        }
    }

}