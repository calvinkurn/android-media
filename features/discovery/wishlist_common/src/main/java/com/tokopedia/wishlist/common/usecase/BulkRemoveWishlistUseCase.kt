package com.tokopedia.wishlist.common.usecase

import android.content.Context
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.graphql.data.GraphqlClient
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase
import com.tokopedia.wishlist.common.R
import com.tokopedia.wishlist.common.data.datamodel.WishlistData
import com.tokopedia.wishlist.common.response.RemoveWishListResponse
import rx.Observable
import rx.functions.FuncN
import javax.inject.Inject

open class BulkRemoveWishlistUseCase @Inject constructor(val context: Context): UseCase<List<WishlistData>>() {

    companion object {
        val PARAM_USER_ID = "userID"
        val PARAM_PRODUCT_IDS = "productIDs"
        private val PARAM_PRODUCT_ID = "productID"
        private val OPERATION_NAME = "removeWishlist"
    }

    private val graphqlUseCase: GraphqlUseCase = GraphqlUseCase()

    init {
        GraphqlClient.init(context)
    }

    override fun createObservable(requestParams: RequestParams): Observable<List<WishlistData>> {
        val productRequest = requestParams.parameters.get(PARAM_PRODUCT_IDS) as MutableList<Pair<String, Int>>
        val userId = requestParams.getString(PARAM_USER_ID, "")
        val observables = mutableListOf<Observable<WishlistData>>()
        productRequest.forEach {
            observables.add(createSingleRemoveUseCaseObservable(it.first, userId, it.second))
        }
        return Observable.zip(observables, object : FuncN<List<WishlistData>> {
            override fun call(vararg args: Any?): List<WishlistData>{
                return args.map {
                    val wishlistData = it as WishlistData
                    wishlistData
                }
            }
        })
    }

    private fun createSingleRemoveUseCaseObservable(productId: String, userId: String, position: Int): Observable<WishlistData> {
        graphqlUseCase.clearRequest()

        val variables = HashMap<String, Any>()

        variables[PARAM_PRODUCT_ID] = Integer.parseInt(productId)
        variables[PARAM_USER_ID] = Integer.parseInt(userId)

        val graphqlRequest = GraphqlRequest(
                GraphqlHelper.loadRawString(context.getResources(),
                        R.raw.query_remove_wishlist),
                RemoveWishListResponse::class.java,
                variables, OPERATION_NAME)
        graphqlUseCase.addRequest(graphqlRequest)

        return graphqlUseCase.createObservable(RequestParams.EMPTY)
                .map {
                    val removeWishListResponse: RemoveWishListResponse = it.getData(RemoveWishListResponse::class.java)
                    WishlistData(removeWishListResponse.wishlistRemove.success, position)
                }
    }
}