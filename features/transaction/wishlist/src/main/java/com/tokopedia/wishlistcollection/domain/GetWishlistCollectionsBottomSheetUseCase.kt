package com.tokopedia.wishlistcollection.domain

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.usecase.coroutines.UseCase
import com.tokopedia.wishlistcollection.data.response.GetWishlistCollectionsBottomSheetResponse
import com.tokopedia.wishlistcommon.util.GQL_GET_WISHLIST_COLLECTIONS_BOTTOMSHEET
import com.tokopedia.wishlistcommon.util.WishlistV2CommonConsts
import com.tokopedia.wishlistcommon.util.WishlistV2CommonConsts.PARAMS
import javax.inject.Inject

@GqlQuery("GetWishlistCollectionsBottomsheetQuery", GQL_GET_WISHLIST_COLLECTIONS_BOTTOMSHEET)
class GetWishlistCollectionsBottomSheetUseCase @Inject constructor(@ApplicationContext private val gqlRepository: GraphqlRepository) :
    UseCase<Result<GetWishlistCollectionsBottomSheetResponse.Data.GetWishlistCollectionsBottomsheet>>() {
    private var params: Map<String, Any?>? = null

    override suspend fun executeOnBackground(): Result<GetWishlistCollectionsBottomSheetResponse.Data.GetWishlistCollectionsBottomsheet> {
        return try {
            val request = GraphqlRequest(
                GetWishlistCollectionsBottomsheetQuery(),
                GetWishlistCollectionsBottomSheetResponse.Data::class.java, params
            )
            val response = gqlRepository.response(listOf(request)).getSuccessData<GetWishlistCollectionsBottomSheetResponse.Data>()
            Success(response.getWishlistCollectionsBottomsheet)
        } catch (e: Exception) {
            Fail(e)
        }
    }

    fun setParams(productId: String, source: String) {
        params = mapOf(PARAMS to mapOf(
            WishlistV2CommonConsts.PRODUCT_IDs to productId,
            WishlistV2CommonConsts.SOURCE to source))
    }
}