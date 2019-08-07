package com.tokopedia.shop.open.domain.interactor

import com.tokopedia.abstraction.common.network.exception.MessageErrorException
import com.tokopedia.graphql.data.model.GraphqlError
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.shop.open.data.model.response.*
import com.tokopedia.shop.open.view.fragment.ShopOpenReserveDomainFragment
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase
import rx.Observable
import javax.inject.Inject
import javax.inject.Named

class ShopOpenCheckDomainNameUseCase @Inject constructor(@Named(ShopOpenReserveDomainFragment.VALIDATE_DOMAIN_NAME_SHOP) private val validateShopRaw: String,
                                                         @Named(ShopOpenReserveDomainFragment.VALIDATE_DOMAIN_SUGGESTION_SHOP) private val validateSuggestionRaw: String,
                                                         private val graphqlUseCase: GraphqlUseCase) : UseCase<ValidateShopDomainSuggestionHeader>() {

    companion object {
        private const val SHOP_NAME = "shopName"
        private const val DOMAIN_NAME = "domain"

        fun createRequestParams(domainName: String, shopName: String): RequestParams {
            val requestParams = RequestParams.create()

            requestParams.putString(SHOP_NAME, shopName)
            requestParams.putString(DOMAIN_NAME, domainName)
            return requestParams
        }

        fun createRequestParam(shopName: String): RequestParams {
            val requestParams = RequestParams.create()
            requestParams.putString(SHOP_NAME, shopName)
            return requestParams
        }
    }

    override fun createObservable(requestParams: RequestParams): Observable<ValidateShopDomainSuggestionHeader> {
        val validateShopDomainNameRequest = GraphqlRequest(validateShopRaw, ValidateShopDomainNameResult::class.java, requestParams.parameters)
        val validateShopDomainSuggestionRequest = GraphqlRequest(validateSuggestionRaw, ShopDomainSuggestionHeader::class.java, requestParams.parameters)
        graphqlUseCase.clearRequest()

        if (requestParams.parameters[DOMAIN_NAME] != null) {
            graphqlUseCase.addRequest(validateShopDomainNameRequest)
        } else {
            graphqlUseCase.addRequest(validateShopDomainSuggestionRequest)
            graphqlUseCase.addRequest(validateShopDomainNameRequest)
        }

        return graphqlUseCase.createObservable(requestParams).map {
            val suggestionData: ShopDomainSuggestionHeader = it.getData(ShopDomainSuggestionHeader::class.java) ?: ShopDomainSuggestionHeader()
            val domainSuggestion: ValidateShopDomainNameResult = it.getData(ValidateShopDomainNameResult::class.java)

            val error: MutableList<GraphqlError> = it.getError(GraphqlError::class.java)
                    ?: mutableListOf()

            if (error.isNotEmpty() && error.first().message.isNotEmpty()) {
                throw MessageErrorException(error[0].message)
            }
            ValidateShopDomainSuggestionHeader(suggestionData,domainSuggestion)
        }
    }
}