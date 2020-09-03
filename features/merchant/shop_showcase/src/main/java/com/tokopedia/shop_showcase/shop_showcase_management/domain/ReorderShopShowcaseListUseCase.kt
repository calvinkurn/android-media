package com.tokopedia.shop_showcase.shop_showcase_management.domain

import com.tokopedia.abstraction.common.network.exception.MessageErrorException
import com.tokopedia.graphql.coroutines.domain.interactor.MultiRequestGraphqlUseCase
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.shop_showcase.shop_showcase_management.data.model.ReorderShopShowcaseResponse
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject
import javax.inject.Named

class ReorderShopShowcaseListUseCase @Inject constructor(private val graphqlUseCase: MultiRequestGraphqlUseCase): UseCase<ReorderShopShowcaseResponse>() {

    var params: RequestParams = RequestParams.EMPTY

    companion object {
        private const val IDS = "ids"
        private const val MUTATION = "mutation reorderShopShowcase(\$ids: [String!]!) {\n" +
                "    reorderShopShowcase(input: {\n" +
                "        ids: \$ids\n" +
                "    }) {\n" +
                "        success\n" +
                "        message\n" +
                "    }\n" +
                "}"

        fun createRequestParam(ids: List<String>): RequestParams = RequestParams.create().apply {
            putObject(IDS, ids)
        }
    }

    override suspend fun executeOnBackground(): ReorderShopShowcaseResponse {
        val reorderShowcase = GraphqlRequest(MUTATION, ReorderShopShowcaseResponse::class.java, params.parameters)
        graphqlUseCase.clearRequest()
        graphqlUseCase.addRequest(reorderShowcase)
        val gqlResponse = graphqlUseCase.executeOnBackground()
        val error = gqlResponse.getError(ReorderShopShowcaseResponse::class.java) ?: listOf()
        if (error == null || error.isEmpty()) {
            return gqlResponse.run {
                getData<ReorderShopShowcaseResponse>(ReorderShopShowcaseResponse::class.java)
            }
        } else {
            throw MessageErrorException(error.mapNotNull {
                it.message
            }.joinToString(separator = ", "))
        }
    }
}