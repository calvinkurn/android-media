package com.tokopedia.cart.domain.usecase

import com.tokopedia.cart.data.model.request.AddCartToWishlistRequest
import com.tokopedia.cart.data.model.response.addcarttowishlist.AddCartToWishlistGqlResponse
import com.tokopedia.cart.domain.model.cartlist.AddCartToWishlistData
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.purchase_platform.common.schedulers.ExecutorSchedulers
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase
import rx.Observable
import javax.inject.Inject

class AddCartToWishlistUseCase @Inject constructor(
    private val graphqlUseCase: GraphqlUseCase,
    private val schedulers: ExecutorSchedulers
) : UseCase<AddCartToWishlistData>() {

    companion object {
        const val PARAM_ADD_CART_TO_WISHLIST_REQUEST = "PARAM_ADD_CART_TO_WISHLIST_REQUEST"

        private const val PARAM = "params"
        private const val PARAM_KEY_CART_IDS = "cart_ids"
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
                    val response = addCartToWishlistGqlResponse.addCartToWishlistDataResponse
                    addCartToWishlistData.status = response.status
                    addCartToWishlistData.success = response.data.success

                    if (response.data.message.isNotEmpty()) {
                        addCartToWishlistData.message = response.data.message[0]
                    }
                }
                addCartToWishlistData
            }
            .subscribeOn(schedulers.io)
            .observeOn(schedulers.main)
    }
}
