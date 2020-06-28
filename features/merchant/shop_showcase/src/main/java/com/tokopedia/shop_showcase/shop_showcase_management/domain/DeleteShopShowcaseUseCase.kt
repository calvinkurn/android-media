package com.tokopedia.shop_showcase.shop_showcase_management.domain

import com.tokopedia.abstraction.common.network.exception.MessageErrorException
import com.tokopedia.graphql.coroutines.domain.interactor.MultiRequestGraphqlUseCase
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.shop_showcase.shop_showcase_management.data.model.DeleteShopShowcaseResponse
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject
import javax.inject.Named

class DeleteShopShowcaseUseCase @Inject constructor(private val graphqlUseCase: MultiRequestGraphqlUseCase): UseCase<DeleteShopShowcaseResponse>() {

    var params: RequestParams = RequestParams.EMPTY

    companion object {
        private const val SHOWCASE_ID = "id"
        private const val MUTATION = "mutation deleteShopShowcase(\$id: String!) {\n" +
                "  deleteShopShowcase(input: {\n" +
                "    id: \$id\n" +
                "  }) {\n" +
                "    success\n" +
                "    message\n" +
                "  }\n" +
                "}"

        fun createRequestParam(showcaseId: String): RequestParams = RequestParams.create().apply {
            putString(SHOWCASE_ID, showcaseId)
        }
    }

    override suspend fun executeOnBackground(): DeleteShopShowcaseResponse {
        val deleteShowcase = GraphqlRequest(MUTATION, DeleteShopShowcaseResponse::class.java, params.parameters)
        graphqlUseCase.clearRequest()
        graphqlUseCase.addRequest(deleteShowcase)
        val graphqlResponse = graphqlUseCase.executeOnBackground()
        val error = graphqlResponse.getError(DeleteShopShowcaseResponse::class.java) ?: listOf()
        if (error == null || error.isEmpty()) {
            return graphqlResponse.run {
                getData<DeleteShopShowcaseResponse>(DeleteShopShowcaseResponse::class.java)
            }
        } else {
            throw MessageErrorException(error.mapNotNull {
                it.message
            }.joinToString(separator = ", "))
        }
    }
}