package com.tokopedia.tokopedianow.common.domain.usecase

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.tokopedianow.common.domain.model.RemoveFromWishListResponse
import com.tokopedia.tokopedianow.common.domain.query.RemoveFromWishlistQuery
import com.tokopedia.usecase.RequestParams
import javax.inject.Inject

class RemoveFromWishlistUseCase @Inject constructor(graphqlRepository: GraphqlRepository) {

    companion object {
        private const val PARAM_PRODUCT_ID = "productID"
        private const val PARAM_USER_ID = "userID"
        private const val PARAM_SOURCE_COLLECTION_ID = "sourceCollectionId"
    }

    private val graphql by lazy { GraphqlUseCase<RemoveFromWishListResponse>(graphqlRepository) }

    suspend fun execute(productID: String, userID: String, sourceCollectionID: Int): RemoveFromWishListResponse {
        graphql.apply {

            val requestParams = RequestParams().apply {
                putInt(PARAM_PRODUCT_ID, productID.toIntOrZero())
                putInt(PARAM_USER_ID, userID.toIntOrZero())
                putInt(PARAM_SOURCE_COLLECTION_ID, sourceCollectionID)
            }.parameters

            setGraphqlQuery(RemoveFromWishlistQuery)
            setTypeClass(RemoveFromWishListResponse::class.java)
            setRequestParams(requestParams)
        }

        return graphql.executeOnBackground()
    }
}
