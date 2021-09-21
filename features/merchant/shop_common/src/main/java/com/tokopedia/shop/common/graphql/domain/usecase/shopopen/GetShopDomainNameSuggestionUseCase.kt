package com.tokopedia.shop.common.graphql.domain.usecase.shopopen

import com.tokopedia.graphql.coroutines.domain.interactor.MultiRequestGraphqlUseCase
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.shop.common.graphql.data.shopopen.ShopDomainSuggestionData
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject

class GetShopDomainNameSuggestionUseCase @Inject constructor(
        private val graphqlUseCase: MultiRequestGraphqlUseCase
): UseCase<ShopDomainSuggestionData>() {
    var params: RequestParams = RequestParams.EMPTY

    override suspend fun executeOnBackground(): ShopDomainSuggestionData {
        val shopDomainNameSuggestion = GraphqlRequest(QUERY, ShopDomainSuggestionData::class.java, params.parameters)
        graphqlUseCase.clearRequest()
        graphqlUseCase.addRequest(shopDomainNameSuggestion)
        val gqlResponse = graphqlUseCase.executeOnBackground()
        val error = gqlResponse.getError(ShopDomainSuggestionData::class.java) ?: listOf()
        if (error == null || error.isEmpty()) {
            return gqlResponse.run {
                getData<ShopDomainSuggestionData>(ShopDomainSuggestionData::class.java)
            }
        } else {
            throw MessageErrorException(error.mapNotNull {
                it.message
            }.joinToString(separator = ", "))
        }

    }

    companion object {
        private const val SHOP_NAME = "shopName"
        private const val QUERY = "query shopDomainSuggestion(\$shopName:String!){\n" +
                "   shopDomainSuggestion(input:{shopName :\$shopName}){\n" +
                "   result{\n" +
                "    shopDomain\n" +
                "    shopDomains\n" +
                "  }\n" +
                "  error{\n" +
                "      message\n" +
                "    }\n" +
                "  }\n" +
                "}\n"

        fun createRequestParams(shopName: String): RequestParams = RequestParams.create().apply {
            putString(SHOP_NAME, shopName)
        }
    }
}

