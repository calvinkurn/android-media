package com.tokopedia.tokopedianow.common.domain.usecase

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.tokopedianow.common.domain.model.AddToWishListResponse
import com.tokopedia.tokopedianow.common.domain.query.AddToWishlistQuery
import com.tokopedia.usecase.RequestParams
import javax.inject.Inject

class AddToWishlistUseCase @Inject constructor(graphqlRepository: GraphqlRepository) {

    companion object {
        private const val PARAM_PRODUCT_ID = "productID"
        private const val PARAM_USER_ID = "userID"
        private const val PARAM_SOURCE_COLLECTION_ID = "sourceCollectionId"
    }

    private val graphql by lazy { GraphqlUseCase<AddToWishListResponse>(graphqlRepository) }

    suspend fun execute(productID: String, userID: String, sourceCollectionID: Int): AddToWishListResponse {
        graphql.apply {

            val requestParams = RequestParams().apply {
                putInt(PARAM_PRODUCT_ID, productID.toIntOrZero())
                putInt(PARAM_USER_ID, userID.toIntOrZero())
                putInt(PARAM_SOURCE_COLLECTION_ID, sourceCollectionID)
            }.parameters

            setGraphqlQuery(AddToWishlistQuery)
            setTypeClass(AddToWishListResponse::class.java)
            setRequestParams(requestParams)
        }

        return graphql.executeOnBackground()
    }
}
