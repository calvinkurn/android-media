package com.tokopedia.purchase_platform.features.cart.domain.usecase

import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.network.exception.ResponseErrorException
import com.tokopedia.purchase_platform.common.domain.schedulers.ExecutorSchedulers
import com.tokopedia.purchase_platform.features.cart.data.model.response.ShopGroupSimplifiedGqlResponse
import com.tokopedia.purchase_platform.features.cart.domain.mapper.CartMapperV3
import com.tokopedia.purchase_platform.features.cart.domain.model.cartlist.CartListData
import com.tokopedia.usecase.RequestParams
import rx.Observable
import javax.inject.Inject
import javax.inject.Named

/**
 * Created by Irfan Khoirul on 2019-10-18.
 */

class GetCartListSimplifiedUseCase @Inject constructor(@Named("shopGroupSimplifiedQuery") private val queryString: String,
                                                       private val graphqlUseCase: GraphqlUseCase,
                                                       private val cartMapperV3: CartMapperV3,
                                                       private val schedulers: ExecutorSchedulers) {

    fun createObservable(): Observable<CartListData> {
        val graphqlRequest = GraphqlRequest(queryString, ShopGroupSimplifiedGqlResponse::class.java)
        graphqlUseCase.clearRequest()
        graphqlUseCase.addRequest(graphqlRequest)
        return graphqlUseCase.createObservable(RequestParams.EMPTY)
                .map {
                    val shopGroupSimplifiedGqlResponse = it.getData<ShopGroupSimplifiedGqlResponse>(ShopGroupSimplifiedGqlResponse::class.java)
                    if (shopGroupSimplifiedGqlResponse.shopGroupSimplifiedResponse.status == "OK") {
                        cartMapperV3.convertToCartItemDataList(shopGroupSimplifiedGqlResponse.shopGroupSimplifiedResponse.data)
                    } else {
                        if (shopGroupSimplifiedGqlResponse.shopGroupSimplifiedResponse.errorMessages.isNotEmpty()) {
                            throw ResponseErrorException(shopGroupSimplifiedGqlResponse.shopGroupSimplifiedResponse.errorMessages.joinToString())
                        } else {
                            throw ResponseErrorException()
                        }
                    }
                }
                .subscribeOn(schedulers.io)
                .observeOn(schedulers.main)

    }
}