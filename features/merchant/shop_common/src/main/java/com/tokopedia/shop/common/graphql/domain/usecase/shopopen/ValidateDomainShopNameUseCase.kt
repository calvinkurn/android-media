package com.tokopedia.shop.common.graphql.domain.usecase.shopopen

import com.tokopedia.graphql.coroutines.domain.interactor.MultiRequestGraphqlUseCase
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.shop.common.graphql.data.shopopen.ValidateShopDomainNameResult
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject

class ValidateDomainShopNameUseCase @Inject constructor(
        private val graphqlUseCase: MultiRequestGraphqlUseCase
): UseCase<ValidateShopDomainNameResult>() {

    var params: RequestParams = RequestParams.EMPTY

    override suspend fun executeOnBackground(): ValidateShopDomainNameResult {
        val validateShopDomainNameRequest = GraphqlRequest(QUERY, ValidateShopDomainNameResult::class.java, params.parameters)
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
        const val QUERY = "query validateDomainShopName(\$domain:String, \$shopName:String){\n" +
                "  validateDomainShopName(domain:\$domain, shopName:\$shopName){\n" +
                "   isValid\n" +
                "   error{\n" +
                "      message\n" +
                "    }\n" +
                " }\n" +
                "}"

        fun createRequestParam(domainName: String): RequestParams = RequestParams.create().apply {
            putString(DOMAIN_NAME, domainName)
        }

        fun createRequestParams(shopName: String): RequestParams = RequestParams.create().apply {
            putString(SHOP_NAME, shopName)
        }
    }
}
