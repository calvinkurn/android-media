package com.tokopedia.home_wishlist.data.repository

import android.text.TextUtils
import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.home_wishlist.data.query.WishlistQuery
import com.tokopedia.home_wishlist.model.entity.Wishlist
import com.tokopedia.home_wishlist.model.entity.WishlistEntityData
import com.tokopedia.home_wishlist.model.entity.WishlistResponse
import com.tokopedia.wishlist.common.request.WishlistAdditionalParamRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class WishlistRepository @Inject constructor(
        private val graphqlRepository: GraphqlRepository
) {
    companion object{
        private const val PARAM_COUNT = "count"
        private const val PARAM_PAGE = "page"
        private const val PARAM_QUERY = "query"
        private const val PARAM_ADDITIONAL_PARAMS = "additionalParams"
        private const val DEFAULT_COUNT = 20
    }

    suspend fun getData(keyword: String, page: Int, additionalParams: WishlistAdditionalParamRequest) : WishlistEntityData{
        val data = withContext(Dispatchers.IO){
            val cacheStrategy =
                    GraphqlCacheStrategy.Builder(CacheType.ALWAYS_CLOUD).build()

            val params = mapOf(
                    PARAM_QUERY to keyword,
                    PARAM_PAGE to page,
                    PARAM_COUNT to DEFAULT_COUNT,
                    PARAM_ADDITIONAL_PARAMS to additionalParams
            )

            val gqlRecommendationRequest = GraphqlRequest(
                    WishlistQuery.getQuery(),
                    WishlistResponse::class.java,
                    params
            )

            graphqlRepository.getReseponse(listOf(gqlRecommendationRequest), cacheStrategy)
        }
        data.getError(WishlistResponse::class.java)?.let {
            if (it.isNotEmpty()) {
                if (!TextUtils.isEmpty(it[0].message)){
                    return WishlistEntityData(isSuccess = false, errorMessage = it[0].message)
                }
            }
        }

        return mappingWishlistRepositoryItem(data.getSuccessData<WishlistResponse>().wishlist)
    }

    private fun mappingWishlistRepositoryItem(data: Wishlist): WishlistEntityData {
        return WishlistEntityData(
                isSuccess = true,
                hasNextPage = data.hasNextPage,
                items = data.items,
                totalData = data.totalData
        )
    }
}