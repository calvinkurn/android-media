package com.tokopedia.wishlistcommon.domain

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.usecase.coroutines.UseCase
import com.tokopedia.wishlistcommon.data.response.GetWishlistCollectionsBottomSheetResponse
import com.tokopedia.wishlistcommon.util.GQL_GET_WISHLIST_COLLECTION
import javax.inject.Inject

@GqlQuery("GetWishlistCollectionsBottomsheetQuery", GQL_GET_WISHLIST_COLLECTION)
class GetWishlistCollectionsBottomSheetUseCase @Inject constructor(private val gqlRepository: GraphqlRepository) :
    UseCase<Result<GetWishlistCollectionsBottomSheetResponse.Data.GetWishlistCollectionsBottomsheet>>() {

    override suspend fun executeOnBackground(): Result<GetWishlistCollectionsBottomSheetResponse.Data.GetWishlistCollectionsBottomsheet> {
        return try {
            val request = GraphqlRequest(
                GetWishlistCollectionsBottomsheetQuery(),
                GetWishlistCollectionsBottomSheetResponse.Data::class.java
            )
            val response = gqlRepository.response(listOf(request)).getSuccessData<GetWishlistCollectionsBottomSheetResponse.Data>()
            Success(response.getWishlistCollectionsBottomsheet)
        } catch (e: Exception) {
            Fail(e)
        }
    }
}