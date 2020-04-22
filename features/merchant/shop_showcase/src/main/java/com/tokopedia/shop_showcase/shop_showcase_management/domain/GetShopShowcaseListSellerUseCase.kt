package com.tokopedia.shop_showcase.shop_showcase_management.domain

import com.tokopedia.abstraction.common.network.exception.MessageErrorException
import com.tokopedia.graphql.coroutines.domain.interactor.MultiRequestGraphqlUseCase
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.shop_showcase.common.GQLQueryConstant
import com.tokopedia.shop_showcase.shop_showcase_management.data.model.ShowcaseList.ShowcaseListSeller.ShopShowcaseListSellerResponse
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject
import javax.inject.Named


class GetShopShowcaseListSellerUseCase @Inject constructor(
        private val graphqlUseCase: MultiRequestGraphqlUseCase,
        @Named(GQLQueryConstant.QUERY_SHOP_SHOWCASE_LIST_AS_SELLER) val queryGetShowcaseList: String
): UseCase<ShopShowcaseListSellerResponse>() {

    override suspend fun executeOnBackground(): ShopShowcaseListSellerResponse {
        val listShowcase = GraphqlRequest(queryGetShowcaseList, ShopShowcaseListSellerResponse::class.java)
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