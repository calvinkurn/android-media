package com.tokopedia.wishlist.domain

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.wishlist.data.model.response.BulkDeleteWishlistV2Response
import com.tokopedia.wishlist.util.WishlistV2Consts
import javax.inject.Inject

class BulkDeleteWishlistV2UseCase @Inject constructor(@ApplicationContext private val gqlRepository: GraphqlRepository) {
    suspend fun executeSuspend(productId: List<String>, userId: String): Result<BulkDeleteWishlistV2Response.Data.WishlistBulkRemoveV2> {
        return try {
            val request = GraphqlRequest(QUERY, BulkDeleteWishlistV2Response.Data::class.java, generateParam(productId, userId))
            val response = gqlRepository.response(listOf(request)).getSuccessData<BulkDeleteWishlistV2Response.Data>()
            Success(response.wishlistBulkRemoveV2)
        } catch (e: Exception) {
            Fail(e)
        }
    }

    private fun generateParam(productId: List<String>, userId: String): Map<String, Any?> {
        return mapOf(
                WishlistV2Consts.PRODUCT_IDs to productId,
                WishlistV2Consts.USER_ID to userId)
    }

    companion object {
        val QUERY = """
            mutation WishlistBulkRemoveV2(${'$'}productIDs: [SuperInteger], ${'$'}userID: SuperInteger) {
                    wishlist_bulk_remove_v2(productID: ${'$'}productIDs, userID: ${'$'}userID) {
                    id
                    success
                    message
                    button {
                      text
                      action
                      url
                    }
                }
            }
            """.trimIndent()
    }
}