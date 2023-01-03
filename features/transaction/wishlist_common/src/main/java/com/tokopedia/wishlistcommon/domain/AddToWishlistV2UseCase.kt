package com.tokopedia.wishlistcommon.domain

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.usecase.coroutines.UseCase
import com.tokopedia.wishlistcommon.data.response.AddToWishlistV2Response
import com.tokopedia.wishlistcommon.util.GQL_WISHLIST_ADD_V2
import com.tokopedia.wishlistcommon.util.WishlistV2CommonConsts.COLLECTION_SHARING
import com.tokopedia.wishlistcommon.util.WishlistV2CommonConsts.PRODUCT_ID
import com.tokopedia.wishlistcommon.util.WishlistV2CommonConsts.SOURCE_COLLECTION_ID
import com.tokopedia.wishlistcommon.util.WishlistV2CommonConsts.USER_ID
import javax.inject.Inject

@GqlQuery("AddToWishlistV2", GQL_WISHLIST_ADD_V2)
open class AddToWishlistV2UseCase @Inject constructor(private val gqlRepository: GraphqlRepository) : UseCase<Result<AddToWishlistV2Response.Data.WishlistAddV2>>() {
    private var params: Map<String, Any?>? = null

    override suspend fun executeOnBackground(): Result<AddToWishlistV2Response.Data.WishlistAddV2> {
        return try {
            val request = GraphqlRequest(AddToWishlistV2(), AddToWishlistV2Response.Data::class.java, params)
            val response = gqlRepository.response(listOf(request)).getSuccessData<AddToWishlistV2Response.Data>()
            Success(response.wishlistAdd)
        } catch (e: Exception) {
            Fail(e)
        }
    }

    fun setParams(productId: String, userId: String) {
        params = mapOf(
            PRODUCT_ID to productId,
            USER_ID to userId
        )
    }

    fun setParams(productId: String, userId: String, sourceCollectionId: String) {
        val collectionSharingParams = mapOf(
            SOURCE_COLLECTION_ID to sourceCollectionId
        )

        params = mapOf(
            PRODUCT_ID to productId,
            USER_ID to userId,
            COLLECTION_SHARING to collectionSharingParams
        )
    }
}
