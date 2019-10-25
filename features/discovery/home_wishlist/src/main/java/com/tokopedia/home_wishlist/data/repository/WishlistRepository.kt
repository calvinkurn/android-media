package com.tokopedia.home_wishlist.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.home_wishlist.model.entity.WishlistItem
import com.tokopedia.home_wishlist.model.entity.WishlistResponse
import com.tokopedia.home_wishlist.util.Response
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
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
        private const val DEFAULT_COUNT = 8
        private const val DEFAULT_START_PAGE = 0
        private const val DEFAULT_ERROR_MESSAGE = "Terjadi kesalahan koneksi, silahkan coba lagi."
    }

    fun load(filter: String, page: Int): LiveData<Response<List<WishlistItem>>> = MediatorLiveData<Response<List<WishlistItem>>>().apply{
        value = if(page == DEFAULT_START_PAGE) Response.loading() else Response.loadingMore()
        GlobalScope.launchCatchError(block=  {
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
            data.getSuccessData<WishlistResponse>().wishlist.items.let {
                postValue(Response.success(it))
            }
        }){
            postValue(Response.error(it.message ?: DEFAULT_ERROR_MESSAGE))
        }
    }
}