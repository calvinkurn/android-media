package com.tokopedia.wishlist.common.usecase

import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase
import com.tokopedia.wishlist.common.data.datamodel.WishlistActionData
import com.tokopedia.wishlist.common.response.BulkRemoveWishListResponse
import rx.Observable
import javax.inject.Inject

open class BulkRemoveWishlistUseCase @Inject constructor(val graphqlUseCase: GraphqlUseCase): UseCase<WishlistActionData>() {


    val bulk_remove_wishlist_req = "mutation bulkRemoveWishlist(\$productIDs: [SuperInteger]!, \$userID: SuperInteger!) {\n" +
            "    wishlist_bulk_remove_v2(productID: \$productIDs, userID: \$userID) {\n" +
            "        id\n" +
            "        success\n" +
            "        __typename\n" +
            "    }\n" +
            "}"

    companion object {
        val PARAM_USER_ID = "userID"
        val PARAM_PRODUCT_IDS = "productIDs"
    }

    override fun createObservable(requestParams: RequestParams): Observable<WishlistActionData> {
        val productRequestIds = requestParams.parameters.get(PARAM_PRODUCT_IDS) as MutableList<String>
        val userId = requestParams.getString(PARAM_USER_ID, "")

        val variables = HashMap<String, Any>()

        variables[PARAM_PRODUCT_IDS] = productRequestIds
        variables[PARAM_USER_ID] = Integer.parseInt(userId)

        graphqlUseCase.clearRequest()

        val graphqlRequest = GraphqlRequest(
                bulk_remove_wishlist_req,
                BulkRemoveWishListResponse::class.java,
                variables)

        graphqlUseCase.addRequest(graphqlRequest)
        return graphqlUseCase.createObservable(RequestParams.EMPTY)
                .map<WishlistActionData> { graphqlResponse ->
                    val bulkRemoveResponse = graphqlResponse.getData<BulkRemoveWishListResponse>(BulkRemoveWishListResponse::class.java)
                    WishlistActionData(
                            bulkRemoveResponse.wishlistRemove.success,
                            bulkRemoveResponse.wishlistRemove.id
                    )
                }
    }
}