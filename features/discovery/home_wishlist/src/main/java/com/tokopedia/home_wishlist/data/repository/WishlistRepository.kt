package com.tokopedia.home_wishlist.data.repository

import android.text.TextUtils
import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.home_wishlist.model.entity.Wishlist
import com.tokopedia.home_wishlist.model.entity.WishlistData
import com.tokopedia.home_wishlist.model.entity.WishlistItem
import com.tokopedia.home_wishlist.model.entity.WishlistResponse
import com.tokopedia.recommendation_widget_common.domain.RecommendationDataSource
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationWidget
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Named


class WishlistRepository @Inject constructor(
        private val graphqlRepository: GraphqlRepository,
        private val recommendationDataSource: RecommendationDataSource,
        @Named("wishlistQuery") private val query: String
) {
    companion object{
        private const val PARAM_COUNT = "count"
        private const val PARAM_PAGE = "page"
        private const val PARAM_QUERY = "query"
        private const val DEFAULT_COUNT = 20
        const val DEFAULT_START_PAGE = 1
    }

    suspend fun getData(filter: String, page: Int) : WishlistData{
        val data = withContext(Dispatchers.IO){
            val cacheStrategy =
                    GraphqlCacheStrategy.Builder(CacheType.ALWAYS_CLOUD).build()

            val params = mapOf(
                    PARAM_QUERY to filter,
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
                    return WishlistData(isSuccess = false, errorMessage = it[0].message)
                }
            }
        }

        return mappingWishlistRepositoryItem(data.getSuccessData<WishlistResponse>().wishlist)
    }

    fun mappingWishlistRepositoryItem(data: Wishlist): WishlistData {
        return WishlistData(
                isSuccess = true,
                hasNextPage = data.hasNextPage,
                items = data.items,
                totalData = data.totalData
        )
    }

    suspend fun getRecommendationData(page: Int, productIds: List<String>): List<RecommendationWidget>{
        val list = mutableListOf<RecommendationWidget>()
        val widget = recommendationDataSource.load(
                pageName = "wishlist",
                pageNumber = page,
                productIds = productIds
        )
        if(widget.isNotEmpty()){
            list.add(widget[0])
        }
        return list
    }
}