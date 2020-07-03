package com.tokopedia.shop.open.domain

import com.tokopedia.abstraction.common.network.exception.MessageErrorException
import com.tokopedia.graphql.coroutines.domain.interactor.MultiRequestGraphqlUseCase
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.shop.open.common.GQLQueryConstant
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.UseCase

import com.tokopedia.shop.open.data.model.ValidateShopDomainNameResult

import javax.inject.Inject
import javax.inject.Named

class ShopOpenRevampValidateDomainShopNameUseCase @Inject constructor(
        private val graphqlUseCase: MultiRequestGraphqlUseCase,
        @Named(GQLQueryConstant.QUERY_SHOP_OPEN_REVAMP_VALIDATE_DOMAIN_SHOP_NAME) val queryCheckDomainShopName: String
): UseCase<ValidateShopDomainNameResult>() {

    var params: RequestParams = RequestParams.EMPTY

    override suspend fun executeOnBackground(): ValidateShopDomainNameResult {
        val validateShopDomainNameRequest = GraphqlRequest(queryCheckDomainShopName, ValidateShopDomainNameResult::class.java, params.parameters)
        graphqlUseCase.clearRequest()
        graphqlUseCase.addRequest(validateShopDomainNameRequest)
        val gqlResponse = graphqlUseCase.executeOnBackground()
        val error = gqlResponse.getError(ValidateShopDomainNameResult::class.java) ?: listOf()
        if (error == null || error.isEmpty()) {
            return gqlResponse.run {
                getData<ValidateShopDomainNameResult>(ValidateShopDomainNameResult::class.java)
            }
        } else {
            throw MessageErrorException(error.mapNotNull {
                it.message
            }.joinToString(separator = ", "))
        }
    }

    companion object {
        const val SHOP_NAME = "shopName"
        const val DOMAIN_NAME = "domain"

        fun createRequestParam(domainName: String): RequestParams = RequestParams.create().apply {
            putString(DOMAIN_NAME, domainName)
        }

        fun createRequestParams(shopName: String): RequestParams = RequestParams.create().apply {
            putString(SHOP_NAME, shopName)
        }
    }
}
