package com.tokopedia.shop_showcase.shop_showcase_management.domain

import com.tokopedia.abstraction.common.network.exception.MessageErrorException
import com.tokopedia.graphql.coroutines.domain.interactor.MultiRequestGraphqlUseCase
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.shop_showcase.common.GQLQueryConstant
import com.tokopedia.shop_showcase.shop_showcase_management.data.model.ReorderShopShowcaseResponse
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject
import javax.inject.Named

class ReorderShopShowcaseListUseCase @Inject constructor(
        private val graphqlUseCase: MultiRequestGraphqlUseCase,
        @Named(GQLQueryConstant.QUERY_REORDER_SHOP_SHOWCASE) val queryReorderShopShowcase: String
): UseCase<ReorderShopShowcaseResponse>() {

    var params: RequestParams = RequestParams.EMPTY

    companion object {
        private const val IDS = "ids"

        fun createRequestParam(ids: List<String>): RequestParams = RequestParams.create().apply {
            putObject(IDS, ids)
        }
    }

    override suspend fun executeOnBackground(): ReorderShopShowcaseResponse {
        val reorderShowcase = GraphqlRequest(queryReorderShopShowcase, ReorderShopShowcaseResponse::class.java, params.parameters)
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