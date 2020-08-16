package com.tokopedia.shop.common.graphql.domain.usecase.shopbasicdata

import com.tokopedia.graphql.coroutines.domain.interactor.MultiRequestGraphqlUseCase
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.shop.common.graphql.data.shopbasicdata.gql.ShopBasicDataQuery
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject

class GetShopBasicDataUseCaseCoroutine @Inject constructor(
        private val graphqlUseCase: MultiRequestGraphqlUseCase
): UseCase<ShopBasicDataQuery>() {

    override suspend fun executeOnBackground(): ShopBasicDataQuery {
        val validateShopDomainNameRequest = GraphqlRequest(QUERY, ShopBasicDataQuery::class.java)
        graphqlUseCase.clearRequest()
        graphqlUseCase.addRequest(validateShopDomainNameRequest)
        val gqlResponse = graphqlUseCase.executeOnBackground()
        val error = gqlResponse.getError(ShopBasicDataQuery::class.java) ?: listOf()
        if (error == null || error.isEmpty()) {
            return gqlResponse.run {
                getData<ShopBasicDataQuery>(ShopBasicDataQuery::class.java)
            }
        } else {
            throw MessageErrorException(error.mapNotNull {
                it.message
            }.joinToString(separator = ", "))
        }
    }

    companion object {
        private const val QUERY = "query ShopBasicData {\n" +
                "  shopBasicData {\n" +
                "    result {\n" +
                "      domain\n" +
                "      name\n" +
                "      status\n" +
                "      closeSchedule\n" +
                "      closeNote\n" +
                "      closeUntil\n" +
                "      openSchedule\n" +
                "      tagline\n" +
                "      description\n" +
                "      logo\n" +
                "      level\n" +
                "      expired\n" +
                "    }\n" +
                "    error {\n" +
                "      message\n" +
                "    }\n" +
                "  }\n" +
                "}"
    }
}