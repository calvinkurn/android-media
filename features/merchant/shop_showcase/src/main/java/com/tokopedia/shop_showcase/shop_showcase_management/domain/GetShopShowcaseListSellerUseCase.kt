package com.tokopedia.shop_showcase.shop_showcase_management.domain

import com.tokopedia.abstraction.common.network.exception.MessageErrorException
import com.tokopedia.graphql.coroutines.domain.interactor.MultiRequestGraphqlUseCase
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.shop_showcase.shop_showcase_management.data.model.ShowcaseList.ShowcaseListSeller.ShopShowcaseListSellerResponse
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject


class GetShopShowcaseListSellerUseCase @Inject constructor(private val graphqlUseCase: MultiRequestGraphqlUseCase): UseCase<ShopShowcaseListSellerResponse>() {

    var params: RequestParams = RequestParams.EMPTY

    companion object {

        private const val WITH_DEFAULT = "withDefault"

        fun createRequestParams(
                withDefault: Boolean = false
        ): RequestParams = RequestParams.create().apply {
            putBoolean(WITH_DEFAULT, withDefault)
        }

        private const val QUERY = "query(\$withDefault: Boolean) {\n" +
                "  shopShowcases(withDefault: \$withDefault) {\n" +
                "      result{\n" +
                "        id\n" +
                "        name\n" +
                "        count\n" +
                "        type\n" +
                "        highlighted\n" +
                "        alias\n" +
                "        uri\n" +
                "        useAce\n" +
                "        badge\n" +
                "        aceDefaultSort\n" +
                "      }\n" +
                "      error{\n" +
                "        message\n" +
                "      }\n" +
                "  }\n" +
                "}"
    }

    override suspend fun executeOnBackground(): ShopShowcaseListSellerResponse {
        val listShowcase = GraphqlRequest(QUERY, ShopShowcaseListSellerResponse::class.java, params.parameters)
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