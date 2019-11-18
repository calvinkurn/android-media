package com.tokopedia.home_wishlist.data.repository

import android.text.TextUtils
import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.home_wishlist.model.entity.Wishlist
import com.tokopedia.home_wishlist.model.entity.WishlistEntityData
import com.tokopedia.home_wishlist.model.entity.WishlistResponse
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationWidget
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Named

class WishlistRepository @Inject constructor(
        private val graphqlRepository: GraphqlRepository,
        @Named("wishlistQuery") private val query: String
) {
    companion object{
        private const val PARAM_COUNT = "count"
        private const val PARAM_PAGE = "page"
        private const val PARAM_QUERY = "query"
        private const val DEFAULT_COUNT = 20
    }

    suspend fun getData(keyword: String, page: Int) : WishlistEntityData{
        val data = withContext(Dispatchers.IO){
            val cacheStrategy =
                    GraphqlCacheStrategy.Builder(CacheType.ALWAYS_CLOUD).build()

            val params = mapOf(
                    PARAM_QUERY to keyword,
                    PARAM_PAGE to page,
                    PARAM_COUNT to DEFAULT_COUNT
            )

            val gqlRecommendationRequest = GraphqlRequest(
                    query,
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