package com.tokopedia.wishlistcommon.domain

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.usecase.coroutines.UseCase
import com.tokopedia.wishlistcommon.data.WishlistV2Params
import com.tokopedia.wishlistcommon.data.response.GetWishlistV2Response
import com.tokopedia.wishlistcommon.util.GQL_WISHLIST_V2
import com.tokopedia.wishlistcommon.util.WishlistV2CommonConsts.PARAMS
import javax.inject.Inject

@GqlQuery("GetWishlistV2", GQL_WISHLIST_V2)
class GetWishlistV2UseCase @Inject constructor(private val graphqlRepository: GraphqlRepository) :
    UseCase<Result<GetWishlistV2Response.Data.WishlistV2>>() {
    private var params: Map<String, Any?>? = null
    override suspend fun executeOnBackground(): Result<GetWishlistV2Response.Data.WishlistV2> {
        return try {
            val request = GraphqlRequest(GetWishlistV2(), GetWishlistV2Response.Data::class.java, params)
            val response = graphqlRepository.response(listOf(request)).getSuccessData<GetWishlistV2Response.Data>()
            Success(response.wishlistV2)
        } catch (e: Exception) {
            Fail(e)
        }
    }

    fun setParams(param: WishlistV2Params) {
        params = mapOf(PARAMS to param)
    }
}
