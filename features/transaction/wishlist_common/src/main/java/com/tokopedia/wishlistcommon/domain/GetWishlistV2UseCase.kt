package com.tokopedia.wishlistcommon.domain

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.wishlist.data.model.WishlistV2Params
import com.tokopedia.wishlist.data.model.response.GetWishlistV2Response
import com.tokopedia.wishlistcommon.query.GQL_WISHLIST_V2
import javax.inject.Inject

@GqlQuery("GetWishlistV2", GQL_WISHLIST_V2)
class GetWishlistV2UseCase @Inject constructor(graphqlRepository: GraphqlRepository): GraphqlUseCase<GetWishlistV2Response>(graphqlRepository) {

    fun loadWishlistV2(param: WishlistV2Params, onSuccess: (response: GetWishlistV2Response) -> Unit,
                       onError: (Throwable) -> Unit) {
        try {
            val params = setParams(param)
            this.setTypeClass(GetWishlistV2Response::class.java)
            this.setRequestParams(params)
            this.setGraphqlQuery(GetWishlistV2.GQL_QUERY)
            execute({
                onSuccess(it)
            }, {
                onError(it)
            })
        } catch (e: Exception) {
            onError(e)
        }

    }

    private fun setParams(param: WishlistV2Params): Map<String, Any?> {
        return mapOf(PARAMS to param)
    }

    companion object {
        const val PARAMS = "params"
    }
}