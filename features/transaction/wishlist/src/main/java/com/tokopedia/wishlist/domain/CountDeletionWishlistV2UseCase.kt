package com.tokopedia.wishlist.domain

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.usecase.coroutines.UseCase
import com.tokopedia.wishlist.data.model.response.DeleteWishlistProgressV2Response
import com.tokopedia.wishlistcommon.util.GQL_COUNT_DELETION_WISHLIST_V2
import javax.inject.Inject

@GqlQuery("CountDeletionProgressQuery", GQL_COUNT_DELETION_WISHLIST_V2)
class CountDeletionWishlistV2UseCase @Inject constructor(private val gqlRepository: GraphqlRepository) : UseCase<Result<DeleteWishlistProgressV2Response.Data.DeleteWishlistProgress>>() {
    override suspend fun executeOnBackground(): Result<DeleteWishlistProgressV2Response.Data.DeleteWishlistProgress> {
        return try {
            val request = GraphqlRequest(
                CountDeletionProgressQuery(),
                DeleteWishlistProgressV2Response.Data::class.java
            )
            val response = gqlRepository.response(listOf(request)).getSuccessData<DeleteWishlistProgressV2Response.Data>()
            Success(response.deleteWishlistProgress)
        } catch (e: Exception) {
            Fail(e)
        }
    }
}