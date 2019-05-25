package com.tokopedia.shop.open.domain.interactor

import com.tokopedia.abstraction.common.network.exception.MessageErrorException
import com.tokopedia.graphql.data.model.GraphqlError
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.shop.open.data.model.response.CreateShop
import com.tokopedia.shop.open.data.model.response.ValidateShopDomainNameResult
import com.tokopedia.shop.open.view.fragment.ShopOpenReserveDomainFragment
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase
import rx.Observable
import javax.inject.Inject
import javax.inject.Named

class ShopOpenCheckDomainNameUseCase @Inject constructor(@Named(ShopOpenReserveDomainFragment.VALIDATE_DOMAIN_NAME_SHOP) private val validateShopRaw: String,
                                                         private val graphqlUseCase: GraphqlUseCase) : UseCase<ValidateShopDomainNameResult>() {

    companion object {
        private const val SHOP_NAME = "shopName"
        private const val DOMAIN_NAME = "domain"

        fun createRequestParams(domainName: String, shopName: String): RequestParams {
            val requestParams = RequestParams.create()

            requestParams.putString(SHOP_NAME, shopName)
            requestParams.putString(DOMAIN_NAME, domainName)
            return requestParams
        }
    }

    override fun createObservable(requestParams: RequestParams?): Observable<ValidateShopDomainNameResult> {
        val graphqlRequest = GraphqlRequest(validateShopRaw, ValidateShopDomainNameResult::class.java, requestParams?.parameters)

        graphqlUseCase.clearRequest()
        graphqlUseCase.addRequest(graphqlRequest)

        return graphqlUseCase.createObservable(requestParams).map {
            val data: ValidateShopDomainNameResult? = it.getData(ValidateShopDomainNameResult::class.java)
            val error: MutableList<GraphqlError> = it.getError(GraphqlError::class.java)
                    ?: mutableListOf()

            if (data == null) {
                throw RuntimeException()
            } else if (error.isNotEmpty() && error[0].message.isNotEmpty()) {
                throw MessageErrorException(error[0].message)
            }
            data
        }
    }
}