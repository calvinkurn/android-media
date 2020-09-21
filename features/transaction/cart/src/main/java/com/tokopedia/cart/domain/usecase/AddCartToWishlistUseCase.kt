package com.tokopedia.cart.domain.usecase

import com.tokopedia.cart.data.model.request.AddCartToWishlistRequest
import com.tokopedia.cart.data.model.response.addcarttowishlist.AddCartToWishlistDataResponse
import com.tokopedia.cart.data.model.response.addcarttowishlist.AddCartToWishlistGqlResponse
import com.tokopedia.cart.domain.model.cartlist.AddCartToWishlistData
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.purchase_platform.common.schedulers.ExecutorSchedulers
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase
import rx.Observable
import javax.inject.Inject

class AddCartToWishlistUseCase @Inject constructor(private val graphqlUseCase: GraphqlUseCase,
                                                   private val schedulers: ExecutorSchedulers) : UseCase<AddCartToWishlistData>() {

    companion object {
        const val PARAM_ADD_CART_TO_WISHLIST_REQUEST = "PARAM_ADD_CART_TO_WISHLIST_REQUEST"

        private const val PARAM = "params"
        private const val PARAM_KEY_CART_IDS = "cart_ids"
        private const val STATUS_OK = "OK"
    }

    override fun createObservable(requestParams: RequestParams?): Observable<AddCartToWishlistData> {
        val paramDelete = requestParams?.getObject(PARAM_ADD_CART_TO_WISHLIST_REQUEST) as AddCartToWishlistRequest

        val variables = mapOf(PARAM to mapOf(PARAM_KEY_CART_IDS to paramDelete.cartIds))

        val mutation = getAddToWishlistMutation()
        val graphqlRequest = GraphqlRequest(mutation, AddCartToWishlistGqlResponse::class.java, variables)
        graphqlUseCase.clearRequest()
        graphqlUseCase.addRequest(graphqlRequest)

        return graphqlUseCase.createObservable(RequestParams.EMPTY)
                .map {
                    val addCartToWishlistGqlResponse = it.getData<AddCartToWishlistGqlResponse>(AddCartToWishlistGqlResponse::class.java)
                    val addCartToWishlistData = AddCartToWishlistData()
                    if (addCartToWishlistGqlResponse != null) {
                        addCartToWishlistData.isSuccess = addCartToWishlistGqlResponse.addCartToWishlistDataResponse.status == STATUS_OK
                        addCartToWishlistData.message = if (addCartToWishlistGqlResponse.addCartToWishlistDataResponse.status == STATUS_OK) {
                            if (addCartToWishlistGqlResponse.addCartToWishlistDataResponse.data.message.isNotEmpty()) {
                                addCartToWishlistGqlResponse.addCartToWishlistDataResponse.data.message[0]
                            } else {
                                ""
                            }
                        } else {
                            if (addCartToWishlistGqlResponse.addCartToWishlistDataResponse.errorMessage.isNotEmpty()) {
                                addCartToWishlistGqlResponse.addCartToWishlistDataResponse.errorMessage[0]
                            } else {
                                ""
                            }
                        }
                    }

                    addCartToWishlistData
                }
                .subscribeOn(schedulers.io)
                .observeOn(schedulers.main)
    }

}