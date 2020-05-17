package com.tokopedia.shop_showcase.shop_showcase_management.domain

import com.tokopedia.abstraction.common.network.exception.MessageErrorException
import com.tokopedia.graphql.coroutines.domain.interactor.MultiRequestGraphqlUseCase
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.shop_showcase.common.GQLQueryConstant
import com.tokopedia.shop_showcase.shop_showcase_management.data.model.DeleteShopShowcaseResponse
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject
import javax.inject.Named

class DeleteShopShowcaseUseCase @Inject constructor(
        private val graphqlUseCase: MultiRequestGraphqlUseCase,
        @Named(GQLQueryConstant.QUERY_DELETE_SINGLE_SHOP_SHOWCASE) val queryDeleteShowcase: String
): UseCase<DeleteShopShowcaseResponse>() {

    var params: RequestParams = RequestParams.EMPTY

    companion object {
        private const val SHOWCASE_ID = "id"

        fun createRequestParam(showcaseId: String): RequestParams = RequestParams.create().apply {
            putString(SHOWCASE_ID, showcaseId)
        }
    }

    override suspend fun executeOnBackground(): DeleteShopShowcaseResponse {
        val deleteShowcase = GraphqlRequest(queryDeleteShowcase, DeleteShopShowcaseResponse::class.java, params.parameters)
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