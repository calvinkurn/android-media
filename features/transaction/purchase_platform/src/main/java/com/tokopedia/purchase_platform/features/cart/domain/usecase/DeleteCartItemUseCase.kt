package com.tokopedia.purchase_platform.features.cart.domain.usecase

import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.promocheckout.common.domain.ClearCacheAutoApplyStackUseCase
import com.tokopedia.purchase_platform.common.domain.schedulers.ExecutorSchedulers
import com.tokopedia.purchase_platform.features.cart.data.model.request.RemoveCartRequest
import com.tokopedia.purchase_platform.features.cart.data.model.response.deletecart.DeleteCartGqlResponse
import com.tokopedia.purchase_platform.features.cart.domain.model.cartlist.DeleteCartData
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase
import rx.Observable
import java.util.*
import javax.inject.Inject

/**
 * Created by Irfan Khoirul on 2019-12-26.
 */

class DeleteCartItemUseCase @Inject constructor(private val clearCacheAutoApplyStackUseCase: ClearCacheAutoApplyStackUseCase,
                                                private val schedulers: ExecutorSchedulers) : UseCase<DeleteCartData>() {

    companion object {
        const val PARAM_TO_BE_REMOVED_PROMO_CODES = "PARAM_TO_BE_REMOVED_PROMO_CODES"
        const val PARAM_REMOVE_CART_REQUEST = "PARAM_REMOVE_CART_REQUEST"

        private const val PARAM_KEY_LANG = "lang"
        private const val PARAM_VALUE_ID = "id"
        private const val PARAM_KEY_ADD_TO_WISHLIST = "addWishlist"
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

    override fun createObservable(requestParams: RequestParams?): Observable<DeleteCartData> {
        val paramDelete = requestParams?.getObject(PARAM_REMOVE_CART_REQUEST) as RemoveCartRequest
        val toBeDeletedPromoCode = requestParams.getObject(PARAM_TO_BE_REMOVED_PROMO_CODES) as ArrayList<String>

        val variables = mapOf(
                PARAM_KEY_LANG to PARAM_VALUE_ID,
                PARAM_KEY_ADD_TO_WISHLIST to paramDelete.addWishlist,
                PARAM_KEY_CART_IDS to paramDelete.cartIds
        )

        val graphqlRequest = GraphqlRequest(QUERY, DeleteCartGqlResponse::class.java, variables)
        val graphqlUseCase = GraphqlUseCase()
        graphqlUseCase.clearRequest()
        graphqlUseCase.addRequest(graphqlRequest)

        return Observable.just(DeleteCartData())
                .flatMap {
                    graphqlUseCase.createObservable(RequestParams.EMPTY)
                            .map {
                                val deleteCartGqlResponse = it.getData<DeleteCartGqlResponse>(DeleteCartGqlResponse::class.java)
                                val deleteCartData = DeleteCartData()
                                deleteCartData.isSuccess = deleteCartGqlResponse.deleteCartDataResponse.status == "OK"
                                deleteCartData.message = if (deleteCartGqlResponse.deleteCartDataResponse.status == "OK") {
                                    if (deleteCartGqlResponse.deleteCartDataResponse.data?.message?.isNotEmpty() == true) {
                                        deleteCartGqlResponse.deleteCartDataResponse.data.message[0]
                                    } else {
                                        ""
                                    }
                                } else {
                                    if (deleteCartGqlResponse.deleteCartDataResponse.errorMessage.isNotEmpty()) {
                                        deleteCartGqlResponse.deleteCartDataResponse.errorMessage[0]
                                    } else {
                                        ""
                                    }
                                }
                                deleteCartData
                            }
                }
                .flatMap { deleteCartData ->
                    if (toBeDeletedPromoCode.isEmpty()) {
                        Observable.just(deleteCartData)
                    } else {
                        clearCacheAutoApplyStackUseCase.setParams(ClearCacheAutoApplyStackUseCase.PARAM_VALUE_MARKETPLACE, toBeDeletedPromoCode)
                        clearCacheAutoApplyStackUseCase.createObservable(RequestParams.create())
                                .map { deleteCartData }
                    }
                }
                .subscribeOn(schedulers.io)
                .observeOn(schedulers.main)
    }

}