package com.tokopedia.wishlist.domain

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.wishlist.data.model.response.DeleteWishlistV2Response
import com.tokopedia.wishlist.util.WishlistV2Consts
import javax.inject.Inject

class DeleteWishlistV2UseCase @Inject constructor(@ApplicationContext private val gqlRepository: GraphqlRepository) {
    suspend fun executeSuspend(productId: String, userId: String): Result<DeleteWishlistV2Response.Data.WishlistRemoveV2> {
        return try {
            val request = GraphqlRequest(QUERY, DeleteWishlistV2Response.Data::class.java, generateParam(productId, userId))
            val response = gqlRepository.response(listOf(request)).getSuccessData<DeleteWishlistV2Response.Data>()
            Success(response.wishlistRemoveV2)
        } catch (e: Exception) {
            Fail(e)
        }
    }

    private fun generateParam(productId: String, userId: String): Map<String, Any?> {
        return mapOf(
                WishlistV2Consts.PRODUCT_ID to productId,
                WishlistV2Consts.USER_ID to userId)
    }

    companion object {
        val QUERY = """
            mutation WishlistRemoveV2(${'$'}productID: SuperInteger, ${'$'}userID: SuperInteger) {
              wishlist_remove_v2(productID:${'$'}productID, userID:${'$'}userID) {
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