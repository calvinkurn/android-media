package com.tokopedia.shop_showcase.shop_showcase_management.domain

import com.tokopedia.abstraction.common.network.exception.MessageErrorException
import com.tokopedia.graphql.coroutines.domain.interactor.MultiRequestGraphqlUseCase
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.shop_showcase.shop_showcase_management.data.model.ShowcaseList.ShowcaseListSeller.ShopShowcaseListSellerResponse
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject
import javax.inject.Named


class GetShopShowcaseListSellerUseCase @Inject constructor(private val graphqlUseCase: MultiRequestGraphqlUseCase): UseCase<ShopShowcaseListSellerResponse>() {

    companion object {
        private const val QUERY = "query shopShowcases {\n" +
                "  shopShowcases(withDefault: true) {\n" +
                "    result {\n" +
                "      id\n" +
                "      name\n" +
                "      count\n" +
                "      type\n" +
                "      highlighted\n" +
                "      alias\n" +
                "      uri\n" +
                "      useAce\n" +
                "      badge\n" +
                "      aceDefaultSort\n" +
                "    }\n" +
                "    error {\n" +
                "      message\n" +
                "    }\n" +
                "  }\n" +
                "}"
    }

    override suspend fun executeOnBackground(): ShopShowcaseListSellerResponse {
        val listShowcase = GraphqlRequest(QUERY, ShopShowcaseListSellerResponse::class.java)
        graphqlUseCase.clearRequest()
        graphqlUseCase.addRequest(listShowcase)
        val gqlResponse = graphqlUseCase.executeOnBackground()
        val error = gqlResponse.getError(ShopShowcaseListSellerResponse::class.java) ?: listOf()
        if (error == null || error.isEmpty()) {
            return gqlResponse.run {
                getData<ShopShowcaseListSellerResponse>(ShopShowcaseListSellerResponse::class.java)
            }
        } else {
            throw MessageErrorException(error.mapNotNull {
                it.message
            }.joinToString(separator = ", "))
        }
    }

}