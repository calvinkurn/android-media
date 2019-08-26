package com.tokopedia.home_recom.viewmodel

import android.arch.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.home_recom.PARAM_X_SOURCE
import com.tokopedia.home_recom.util.Response
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.recommendation_widget_common.data.RecomendationEntity
import com.tokopedia.recommendation_widget_common.data.mapper.RecommendationEntityMapper
import com.tokopedia.recommendation_widget_common.domain.GetRecommendationUseCase
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.topads.sdk.domain.interactor.TopAdsWishlishedUseCase
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.wishlist.common.usecase.AddWishListUseCase
import com.tokopedia.wishlist.common.usecase.RemoveWishListUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Named

/**
 * Created by Lukas on 26/08/19
 */
open class SimilarProductRecommendationViewModel @Inject constructor(
        private val graphqlRepository: GraphqlRepository,
        private val userSessionInterface: UserSessionInterface,
        private val getRecommendationUseCase: GetRecommendationUseCase,
        private val addWishListUseCase: AddWishListUseCase,
        private val removeWishListUseCase: RemoveWishListUseCase,
        private val topAdsWishlishedUseCase: TopAdsWishlishedUseCase,
        @Named("Main") val dispatcher: CoroutineDispatcher
) : BaseViewModel(dispatcher){
    /**
     * public variable
     */
    val recommendationItem: MutableLiveData<Response<List<RecommendationItem>>> = MutableLiveData()

    fun getSimilarProductRecommendation(page: Int = 0){
        if(recommendationItem.value == null) recommendationItem.postValue(Response.loading())
        else recommendationItem.postValue(Response.loadingMore(recommendationItem.value?.data))
        launchCatchError(block = {
            val gqlData = withContext(Dispatchers.IO) {
                val cacheStrategy =
                        GraphqlCacheStrategy.Builder(CacheType.ALWAYS_CLOUD).build()

                val params = mapOf(
                        PARAM_X_SOURCE to "android"
                )

                val gqlRecommendationRequest = GraphqlRequest(
                        "",
                        RecomendationEntity::class.java,
                        params
                )

                graphqlRepository.getReseponse(listOf(gqlRecommendationRequest), cacheStrategy)
            }
            gqlData.getSuccessData<RecomendationEntity>().productRecommendationWidget?.data?.let {
                val productDetailResponse = mapToRecommendationItem(it)
                recommendationItem.postValue(Response.success(combineList(recommendationItem.value?.data ?: emptyList(), productDetailResponse)))
            }
        }, onError = {
            recommendationItem.postValue(Response.error(it.localizedMessage, recommendationItem.value?.data))
        })
    }

    private fun <T> combineList(first: List<T>, second: List<T>): List<T>{
        return ArrayList(first).apply { addAll(second) }
    }

    private fun mapToRecommendationItem(list: List<RecomendationEntity.RecomendationData>): List<RecommendationItem>{
        return RecommendationEntityMapper.mappingToRecommendationModel(list)[0].recommendationItemList
    }
}