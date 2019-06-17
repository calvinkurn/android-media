package com.tokopedia.shop.open.domain.interactor

import com.tokopedia.abstraction.common.network.exception.MessageErrorException
import com.tokopedia.graphql.data.model.GraphqlError
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.shop.open.data.model.response.CreateShop
import com.tokopedia.shop.open.view.fragment.ShopOpenReserveDomainFragment
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase
import rx.Observable
import javax.inject.Inject
import javax.inject.Named

class ShopOpenSubmitUseCase @Inject constructor(@Named(ShopOpenReserveDomainFragment.OPEN_SHOP_SUBMIT_RAW) private val openShopRaw:String,
                                                private val graphqlUseCase: GraphqlUseCase) : UseCase<CreateShop>() {

    companion object {
        private const val SHOP_NAME = "name"
        private const val DOMAIN_NAME = "domain"
        private const val DISTRICT_ID = "districtID"
        private const val POSTAL_CODE = "postalCode"

        fun createRequestParams(shopName:String , domainName:String , districtId:Int , postalCodeId:Int) : RequestParams{
            val requestParams = RequestParams.create()

            requestParams.putString(SHOP_NAME, shopName)
            requestParams.putString(DOMAIN_NAME, domainName)
            requestParams.putInt(DISTRICT_ID, districtId)
            requestParams.putInt(POSTAL_CODE, postalCodeId)
            return requestParams
        }
    }

    override fun createObservable(requestParams: RequestParams?): Observable<CreateShop> {
        val graphqlRequest = GraphqlRequest(openShopRaw, CreateShop::class.java, requestParams?.parameters)

        graphqlUseCase.clearRequest()
        graphqlUseCase.addRequest(graphqlRequest)

        return graphqlUseCase.createObservable(requestParams).map{
            val data: CreateShop? = it.getData(CreateShop::class.java)
            val error: MutableList<GraphqlError> = it.getError(GraphqlError::class.java) ?: mutableListOf()

            if (data == null) {
                throw RuntimeException()
            } else if (error.isNotEmpty() && error[0].message.isNotEmpty()) {
                throw MessageErrorException(error[0].message)
            }
            data
        }

    }

}