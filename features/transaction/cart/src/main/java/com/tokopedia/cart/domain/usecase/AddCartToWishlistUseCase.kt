package com.tokopedia.cart.domain.usecase

import com.tokopedia.cart.data.model.request.AddCartToWishlistRequest
import com.tokopedia.cart.data.model.response.addcarttowishlist.AddCartToWishlistDataResponse
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
        // Todo : adjust query & param
        const val PARAM_ADD_CART_TO_WISHLIST_REQUEST = "PARAM_ADD_CART_TO_WISHLIST_REQUEST"

        private const val PARAM_KEY_LANG = "lang"
        private const val PARAM_VALUE_ID = "id"
        private const val PARAM_KEY_CART_IDS = "cartIds"

        private val QUERY = """
        mutation remove_from_cart(${'$'}addWishlist: Int, ${'$'}cartIds: [String], ${'$'}lang: String){
            remove_from_cart(addWishlist: ${'$'}addWishlist, cartIds:${'$'}cartIds, lang: ${'$'}lang){
            error_message
            status
            data {
                message
                success
            }
          }
        }
        """.trimIndent()
    }

    override fun createObservable(requestParams: RequestParams?): Observable<AddCartToWishlistData> {
        val paramDelete = requestParams?.getObject(PARAM_ADD_CART_TO_WISHLIST_REQUEST) as AddCartToWishlistRequest

        val variables = mapOf(
                PARAM_KEY_LANG to PARAM_VALUE_ID,
                PARAM_KEY_CART_IDS to paramDelete.cartIds
        )

        val graphqlRequest = GraphqlRequest(QUERY, AddCartToWishlistDataResponse::class.java, variables)
        graphqlUseCase.clearRequest()
        graphqlUseCase.addRequest(graphqlRequest)

        return Observable.just(AddCartToWishlistData())
                .flatMap {
                    graphqlUseCase.createObservable(RequestParams.EMPTY)
                            .map {
                                val addCartToWishlistData = AddCartToWishlistData().apply {
                                    isSuccess = true
                                }
                                // Todo : read real API
/*
                                val undoDeleteCartGqlResponse = it.getData<UndoDeleteCartGqlResponse>(UndoDeleteCartGqlResponse::class.java)
                                val undoDeleteCartData = UndoDeleteCartData()
                                if (undoDeleteCartGqlResponse != null) {
                                    undoDeleteCartData.isSuccess = undoDeleteCartGqlResponse.undoDeleteCartDataResponse.status == "OK"
                                    undoDeleteCartData.message = if (undoDeleteCartGqlResponse.undoDeleteCartDataResponse.status == "OK") {
                                        if (undoDeleteCartGqlResponse.undoDeleteCartDataResponse.data.message.isNotEmpty()) {
                                            undoDeleteCartGqlResponse.undoDeleteCartDataResponse.data.message[0]
                                        } else {
                                            ""
                                        }
                                    } else {
                                        if (undoDeleteCartGqlResponse.undoDeleteCartDataResponse.errorMessage.isNotEmpty()) {
                                            undoDeleteCartGqlResponse.undoDeleteCartDataResponse.errorMessage[0]
                                        } else {
                                            ""
                                        }
                                    }
                                }
*/
                                addCartToWishlistData
                            }
                }
                .subscribeOn(schedulers.io)
                .observeOn(schedulers.main)
    }

}