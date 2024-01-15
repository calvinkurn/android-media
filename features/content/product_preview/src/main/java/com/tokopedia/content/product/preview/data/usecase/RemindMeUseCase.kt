package com.tokopedia.content.product.preview.data.usecase

import com.google.gson.annotations.SerializedName
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.content.product.preview.data.AddWishlistResponse
import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.gql_query_annotation.GqlQueryInterface
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import javax.inject.Inject

/**
 * @author by astidhiyaa on 11/01/24
 */

@GqlQuery(RemindMeUseCase.QUERY_NAME, RemindMeUseCase.QUERY)
class RemindMeUseCase @Inject constructor(
    @ApplicationContext private val repo: GraphqlRepository,
    dispatchers: CoroutineDispatchers
) : CoroutineUseCase<RemindMeUseCase.Param, AddWishlistResponse>(dispatchers.io) {

    private val query : GqlQueryInterface = RemindMeUseCaseQuery()
    override suspend fun execute(params: Param): AddWishlistResponse {
        return repo.request(query, params)
    }

    override fun graphqlQuery(): String = query.getQuery()

    data class Param(
        @SerializedName("userID")
        val userId: Long,
        @SerializedName("productID")
        val productId: Long,
    )

    companion object {
        const val QUERY_NAME = "RemindMeUseCaseQuery"
        const val QUERY = """
            mutation remindMe(${'$'}productID: SuperInteger, ${'$'}userID: SuperInteger) {
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
                error_type
              }
            }
        """
    }
}
