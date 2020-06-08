package com.tokopedia.hotel.homepage.presentation.model.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.common.travel.constant.TravelType
import com.tokopedia.common.travel.data.entity.TravelCollectiveBannerModel
import com.tokopedia.common.travel.domain.GetTravelCollectiveBannerUseCase
import com.tokopedia.common.travel.domain.TravelRecentSearchUseCase
import com.tokopedia.common.travel.utils.TravelDispatcherProvider
import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.hotel.homepage.data.cloud.entity.HotelDeleteRecentSearchEntity
import com.tokopedia.hotel.homepage.data.cloud.entity.HotelPropertyDefaultHome
import com.tokopedia.hotel.homepage.presentation.model.HotelRecentSearchModel
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * @author by furqan on 04/04/19
 */
class HotelHomepageViewModel @Inject constructor(
        private val graphqlRepository: GraphqlRepository,
        private val bannerUseCase: GetTravelCollectiveBannerUseCase,
        private val travelRecentSearchUseCase: TravelRecentSearchUseCase,
        val dispatcher: TravelDispatcherProvider) : BaseViewModel(dispatcher.io()) {

    val promoData = MutableLiveData<Result<TravelCollectiveBannerModel>>()

    private val mutableRecentSearch = MutableLiveData<Result<HotelRecentSearchModel>>()
    val recentSearch: LiveData<Result<HotelRecentSearchModel>>
        get() = mutableRecentSearch

    private val mutableHomepageDefaultParam = MutableLiveData<HotelPropertyDefaultHome>()
    val homepageDefaultParam: LiveData<HotelPropertyDefaultHome>
        get() = mutableHomepageDefaultParam

    private val mutableDeleteRecentSearch = MutableLiveData<Result<Boolean>>()
    val deleteRecentSearch: LiveData<Result<Boolean>>
        get() = mutableDeleteRecentSearch

    fun getHotelPromo(rawQuery: String) {
        launch {
            promoData.postValue(bannerUseCase.execute(rawQuery, TravelType.HOTEL, true))
        }
    }

    fun getDefaultHomepageParameter(rawQuery: String) {
        launchCatchError(block = {
            val data = withContext(dispatcher.ui()) {
                val graphqlRequest = GraphqlRequest(rawQuery, HotelPropertyDefaultHome.Response::class.java)
                var graphQlCacheStrategy = GraphqlCacheStrategy.Builder(CacheType.ALWAYS_CLOUD).build()
                graphqlRepository.getReseponse(listOf(graphqlRequest), graphQlCacheStrategy)
            }.getSuccessData<HotelPropertyDefaultHome.Response>().response.data

            mutableHomepageDefaultParam.postValue(data)
        }) { }
    }

    fun getRecentSearch(rawQuery: String) {
        launchCatchError(block = {
            val data = travelRecentSearchUseCase.execute(rawQuery, true)
            mutableRecentSearch.postValue(Success(HotelRecentSearchModel(title = data.travelMeta.title, items = data.items)))
        }) {
            mutableRecentSearch.postValue(Fail(it))
        }
    }

    fun deleteRecentSearch(rawQuery: String) {
        launchCatchError(block = {
            val data = withContext(dispatcher.ui()) {
                val graphqlRequest = GraphqlRequest(rawQuery, HotelDeleteRecentSearchEntity.Response::class.java)
                var graphQlCacheStrategy = GraphqlCacheStrategy.Builder(CacheType.ALWAYS_CLOUD).build()
                graphqlRepository.getReseponse(listOf(graphqlRequest), graphQlCacheStrategy)
            }.getSuccessData<HotelDeleteRecentSearchEntity.Response>()

            mutableDeleteRecentSearch.postValue(Success(data.travelRecentSearchHotelDelete.result))
        }) {
            mutableDeleteRecentSearch.postValue(Fail(it))
        }
    }
}