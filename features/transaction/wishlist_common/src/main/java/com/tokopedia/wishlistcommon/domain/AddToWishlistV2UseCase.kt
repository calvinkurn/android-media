package com.tokopedia.wishlistcommon.domain

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.usecase.coroutines.UseCase
import com.tokopedia.wishlistcommon.data.response.AddToWishlistV2Response
import javax.inject.Inject

class AddToWishlistV2UseCase @Inject constructor(@ApplicationContext private val gqlRepository: GraphqlRepository) : UseCase<Result<AddToWishlistV2Response.Data.WishlistAdd>>() {
    private var params: Map<String, Any?>? = null

    override suspend fun executeOnBackground(): Result<AddToWishlistV2Response.Data.WishlistAdd> {
        return try {
            val request = GraphqlRequest(MUTATION, AddToWishlistV2Response.Data::class.java, params)
            val response = gqlRepository.response(listOf(request)).getSuccessData<AddToWishlistV2Response.Data>()
            Success(response.wishlistAdd)
        } catch (e: Exception) {
            Fail(e)
        }
    }

    fun setParams(productId: String, userId: String) {
        params = mapOf(
                PRODUCT_ID to productId,
                USER_ID to userId)
    }

    companion object {
        val MUTATION = """
        mutation WishlistAddV2(${'$'}productID: SuperInteger, ${'$'}userID: SuperInteger) {
          wishlist_add_v2(productID:${'$'}productID, userID:${'$'}userID) {
            id
            success
            message
            toaster_color
            button{
                text
                action
                url
            }
          }
        }
        """.trimIndent()

        private const val PRODUCT_ID = "productID"
        private const val USER_ID = "userID"
    }
}