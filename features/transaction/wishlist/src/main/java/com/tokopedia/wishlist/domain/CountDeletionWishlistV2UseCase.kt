package com.tokopedia.wishlist.domain

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.usecase.coroutines.UseCase
import com.tokopedia.wishlist.data.model.response.CountDeletionWishlistV2Response
import com.tokopedia.wishlistcommon.util.GQL_COUNT_DELETION_WISHLIST_V2
import javax.inject.Inject

@GqlQuery("CountDeletionProgressQuery", GQL_COUNT_DELETION_WISHLIST_V2)
class CountDeletionWishlistV2UseCase @Inject constructor(private val gqlRepository: GraphqlRepository) : UseCase<Result<CountDeletionWishlistV2Response>>() {

    override suspend fun executeOnBackground(): Result<CountDeletionWishlistV2Response> {
        return try {
            val request = GraphqlRequest(CountDeletionProgressQuery(), CountDeletionWishlistV2Response::class.java)
            val response = gqlRepository.response(listOf(request)).getSuccessData<CountDeletionWishlistV2Response>()
            Success(response)
        } catch (e: Exception) {
            Fail(e)
        }
    }
}
