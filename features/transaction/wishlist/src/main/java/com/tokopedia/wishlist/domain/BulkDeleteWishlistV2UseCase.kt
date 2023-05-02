package com.tokopedia.wishlist.domain

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.wishlist.data.model.WishlistV2BulkRemoveAdditionalParams
import com.tokopedia.wishlist.data.model.response.BulkDeleteWishlistV2Response
import com.tokopedia.wishlist.util.WishlistV2Consts
import javax.inject.Inject

class BulkDeleteWishlistV2UseCase @Inject constructor(@ApplicationContext private val gqlRepository: GraphqlRepository) {
    suspend fun executeSuspend(
        productId: List<String>,
        userId: String,
        mode: Int,
        additionalParams: WishlistV2BulkRemoveAdditionalParams,
        source: String
    ): Result<BulkDeleteWishlistV2Response.Data.WishlistBulkRemoveV2> {
        return try {
            val request = GraphqlRequest(
                QUERY,
                BulkDeleteWishlistV2Response.Data::class.java,
                generateParam(productId, userId, mode, additionalParams, source)
            )
            val response = gqlRepository.response(listOf(request))
                .getSuccessData<BulkDeleteWishlistV2Response.Data>()
            Success(response.wishlistBulkRemoveV2)
        } catch (e: Exception) {
            Fail(e)
        }
    }

    private fun generateParam(
        productId: List<String>,
        userId: String,
        mode: Int,
        additionalParams: WishlistV2BulkRemoveAdditionalParams,
        source: String
    ): MutableMap<String, Any> {
        val params = mutableMapOf(
            WishlistV2Consts.PRODUCT_ID to productId,
            WishlistV2Consts.USER_ID to userId,
            WishlistV2Consts.MODE to mode,
            WishlistV2Consts.SOURCE to source
        )

        if (mode == 2) {
            params[WishlistV2Consts.ADDITIONAL_PARAMS] = additionalParams
        }
        return params
    }

    companion object {
        val QUERY = """
            mutation WishlistBulkRemoveV2(${'$'}productID: [SuperInteger], ${'$'}userID: SuperInteger, ${'$'}mode: Int, ${'$'}additionalParameters: AddParams, ${'$'}source: String) {
                    wishlist_bulk_remove_v2(productID: ${'$'}productID, userID: ${'$'}userID, mode: ${'$'}mode, additionalParameters: ${'$'}additionalParameters, source: ${'$'}source) {
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
