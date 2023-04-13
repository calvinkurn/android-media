package com.tokopedia.wishlistcommon.domain

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.usecase.coroutines.UseCase
import com.tokopedia.wishlistcommon.data.response.DeleteWishlistV2Response
import com.tokopedia.wishlistcommon.util.GQL_WISHLIST_REMOVE_V2
import com.tokopedia.wishlistcommon.util.WishlistV2CommonConsts.PRODUCT_ID
import com.tokopedia.wishlistcommon.util.WishlistV2CommonConsts.USER_ID
import javax.inject.Inject

@GqlQuery("DeleteWishlistV2", GQL_WISHLIST_REMOVE_V2)
class DeleteWishlistV2UseCase @Inject constructor(private val gqlRepository: GraphqlRepository) : UseCase<Result<DeleteWishlistV2Response.Data.WishlistRemoveV2>>() {
    private var params: Map<String, Any?>? = null
    override suspend fun executeOnBackground(): Result<DeleteWishlistV2Response.Data.WishlistRemoveV2> {
        return try {
            val request = GraphqlRequest(DeleteWishlistV2(), DeleteWishlistV2Response.Data::class.java, params)
            val response = gqlRepository.response(listOf(request)).getSuccessData<DeleteWishlistV2Response.Data>()
            Success(response.wishlistRemoveV2)
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
}
