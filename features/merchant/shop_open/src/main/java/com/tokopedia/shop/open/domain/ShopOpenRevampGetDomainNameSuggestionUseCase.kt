package com.tokopedia.shop.open.domain

import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.abstraction.common.network.exception.MessageErrorException
import com.tokopedia.graphql.coroutines.domain.interactor.MultiRequestGraphqlUseCase
import com.tokopedia.shop.open.common.GQLQueryConstant
import com.tokopedia.shop.open.data.model.ShopDomainSuggestionResult
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.UseCase

import javax.inject.Inject
import javax.inject.Named

class ShopOpenRevampGetDomainNameSuggestionUseCase @Inject constructor(
        private val graphqlUseCase: MultiRequestGraphqlUseCase,
        @Named(GQLQueryConstant.QUERY_SHOP_OPEN_REVAMP_DOMAIN_SHOP_SUGGESTION) val queryGetShopNameSuggestions: String
        ): UseCase<ShopDomainSuggestionResult>() {

    var params: RequestParams = RequestParams.EMPTY

    override suspend fun executeOnBackground(): ShopDomainSuggestionResult {
        val shopDomainNameSuggestion = GraphqlRequest(queryGetShopNameSuggestions, ShopDomainSuggestionResult::class.java, params.parameters)
        graphqlUseCase.clearRequest()
        graphqlUseCase.addRequest(shopDomainNameSuggestion)
        val gqlResponse = graphqlUseCase.executeOnBackground()
        val error = gqlResponse.getError(ShopDomainSuggestionResult::class.java) ?: listOf()
        if (error == null || error.isEmpty()) {
            return gqlResponse.run {
                getData<ShopDomainSuggestionResult>(ShopDomainSuggestionResult::class.java)
            }
        } else {
            throw MessageErrorException(error.mapNotNull {
                it.message
            }.joinToString(separator = ", "))
        }

    }

    companion object {
        private const val SHOP_NAME = "shopName"

        fun createRequestParams(shopName: String): RequestParams = RequestParams.create().apply {
            putString(SHOP_NAME, shopName)
        }
    }
}