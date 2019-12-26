package com.tokopedia.purchase_platform.features.cart.domain.usecase

import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.promocheckout.common.domain.ClearCacheAutoApplyStackUseCase
import com.tokopedia.purchase_platform.common.domain.schedulers.ExecutorSchedulers
import com.tokopedia.purchase_platform.features.cart.data.model.request.RemoveCartRequest
import com.tokopedia.purchase_platform.features.cart.data.model.response.ShopGroupSimplifiedGqlResponse
import com.tokopedia.purchase_platform.features.cart.data.model.response.deletecart.DeleteCartDataResponse
import com.tokopedia.purchase_platform.features.cart.data.model.response.deletecart.DeleteCartGqlResponse
import com.tokopedia.purchase_platform.features.cart.domain.model.DeleteCartResponseData
import com.tokopedia.purchase_platform.features.cart.domain.model.cartlist.DeleteCartData
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase
import rx.Observable
import java.util.ArrayList
import javax.inject.Inject

/**
 * Created by Irfan Khoirul on 2019-12-26.
 */

class DeleteCartItemUseCase @Inject constructor(private val clearCacheAutoApplyStackUseCase: ClearCacheAutoApplyStackUseCase,
                                                private val schedulers: ExecutorSchedulers) : UseCase<DeleteCartData>() {

    companion object {
        const val PARAM_TO_BE_REMOVED_PROMO_CODES = "PARAM_TO_BE_REMOVED_PROMO_CODES"
        const val PARAM_REMOVE_CART_REQUEST = "PARAM_REMOVE_CART_REQUEST"

        const val PARAM_KEY_LANG = "lang"
        const val PARAM_VALUE_ID = "id"
        const val PARAM_KEY_ADD_TO_WISHLIST = "addWishlist"
        const val PARAM_KEY_CART_IDS = "cartIds"
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
                                val deleteCartGqlResponse = it.getData<DeleteCartDataResponse>(DeleteCartGqlResponse::class.java)
                                val deleteCartData = DeleteCartData()
                                deleteCartData.isSuccess = deleteCartGqlResponse.data.success == 1
                                deleteCartData.message = if (deleteCartGqlResponse.errorMessage.isNotEmpty()) {
                                    deleteCartGqlResponse.errorMessage[0]
                                } else {
                                    ""
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

//        return graphqlUseCase.createObservable(RequestParams.EMPTY)
//                .map {
//                    val deleteCartGqlResponse = it.getData<DeleteCartDataResponse>(DeleteCartGqlResponse::class.java)
//                    val deleteCartData = DeleteCartData()
//                    deleteCartData.isSuccess = deleteCartGqlResponse.data.success == 1
//                    deleteCartData.message = if (deleteCartGqlResponse.errorMessage.isNotEmpty()) {
//                        deleteCartGqlResponse.errorMessage[0]
//                    } else {
//                        ""
//                    }
//                    deleteCartData
//                }


//        return Observable.just(DeleteCartResponseData())
//                .flatMap { deleteAndRefreshCartListData ->
//                    cartRepository.deleteCartData(paramDelete).map { deleteCartDataResponse ->
//                        deleteAndRefreshCartListData.deleteCartData = cartMapper.convertToDeleteCartData(deleteCartDataResponse)
//                        deleteAndRefreshCartListData
//                    }
//                }
//                .flatMap { deleteAndRefreshCartListData ->
//                    if (toBeDeletedPromoCode.isEmpty()) {
//                        Observable.just(deleteAndRefreshCartListData)
//                    } else {
//                        clearCacheAutoApplyStackUseCase.setParams(ClearCacheAutoApplyStackUseCase.PARAM_VALUE_MARKETPLACE, toBeDeletedPromoCode)
//                        clearCacheAutoApplyStackUseCase.createObservable(RequestParams.create())
//                                .map { deleteAndRefreshCartListData }
//                    }
//                }
//                .subscribeOn(schedulers.io)
//                .observeOn(schedulers.main)

    }

    private val QUERY = """
        mutation remove_from_cart(${'$'}addWishlist: Int, ${'$'}cartIds: [Int], ${'$'}lang: String){
            remove_from_cart(addWishlist: ${'$'}addWishlist, cartIds:${'$'}cartIds, lang: ${'$'}lang){
            error_message
            status
            data {
              success
            }
          }
        }
        """.trimIndent()
}