package com.tokopedia.home_wishlist.data.repository

import android.text.TextUtils
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.home_wishlist.model.datamodel.*
import com.tokopedia.home_wishlist.model.entity.WishlistItem
import com.tokopedia.home_wishlist.model.entity.WishlistResponse
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.recommendation_widget_common.domain.RecommendationDataSource
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationWidget
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
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
        private const val DEFAULT_COUNT = 8
        const val DEFAULT_START_PAGE = 1
        private const val DEFAULT_ERROR_MESSAGE = "Terjadi kesalahan koneksi, silahkan coba lagi."
    }

    suspend fun getData(filter: String, page: Int) : List<WishlistItem>{
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
                    throw Throwable(it[0].message)
                }
            }
        }
        data.getSuccessData<WishlistResponse>().wishlist.items.let {
            return it
        }
    }

    suspend fun getRecommendationData(count: Int, startPageNumber: Int, productIds: List<String>): List<RecommendationWidget>{
        val list = mutableListOf<RecommendationWidget>()
        var page = startPageNumber
        for(i in count downTo 1){
            val widget = recommendationDataSource.load(
                    pageName = "wishlist",
                    pageNumber = page,
                    productIds = productIds
            )
            if(widget.isNotEmpty()){
                list.add(widget[0])
                page++
            }
        }
        return list
    }

    fun get(filter: String, page: Int): LiveData<List<Visitable<*>>> = MediatorLiveData<List<Visitable<*>>>().apply{
        value = if(page == DEFAULT_START_PAGE) listOf(LoadingDataModel()) else listOf(LoadMoreDataModel())
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
                val newList = ArrayList(mappingToVisitable(it))
                postValue(mappingLoadingRecommendation(newList))
                val count = it.size / 4
                for(i in count downTo 1){
                    val widget = recommendationDataSource.load(
                            pageName = "wishlist",
                            pageNumber = i/4,
                            productIds = it.map { it.id }
                    )
                    if(widget.isNotEmpty()){
                        val index = i * 4 + i -1
                        newList.add(i * 4, RecommendationCarouselDataModel(title = widget.first().title, list = widget.first().recommendationItemList.map { RecommendationCarouselItemDataModel(it) }, seeMoreAppLink = widget.first().seeMoreAppLink))
                    }
                }
                postValue(newList)
            }
        }){
            postValue(listOf(ErrorWishlistDataModel(it.message ?: DEFAULT_ERROR_MESSAGE)))
        }
    }

    private fun mappingToVisitable(list: List<WishlistItem>): List<Visitable<*>>{
        return list.map{ WishlistItemDataModel(it) }
    }

    private fun mappingLoadingRecommendation(list: List<Visitable<*>>): List<Visitable<*>>{
        val newMappingList = ArrayList(list)
        val countRecommendationWidget = list.size / 4
        for(i in countRecommendationWidget downTo 1){
            newMappingList.add(i * 4, LoadMoreDataModel())
        }
        return newMappingList
    }
}