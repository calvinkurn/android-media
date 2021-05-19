package com.tokopedia.entertainment.home.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.common.network.coroutines.repository.RestRepository
import com.tokopedia.common.network.data.model.RequestType
import com.tokopedia.common.network.data.model.RestRequest
import com.tokopedia.entertainment.common.util.EventQuery
import com.tokopedia.entertainment.home.UrlConstant.BASE_REST_URL
import com.tokopedia.entertainment.home.UrlConstant.PATH_EVENTS_LIKES
import com.tokopedia.entertainment.home.adapter.HomeEventItem
import com.tokopedia.entertainment.home.adapter.viewmodel.*
import com.tokopedia.entertainment.home.data.ActionLikedRequest
import com.tokopedia.entertainment.home.data.ActionLikedResponse
import com.tokopedia.entertainment.home.data.EventHomeDataResponse
import com.tokopedia.entertainment.home.utils.MapperHomeData.mappingItem
import com.tokopedia.graphql.GraphqlConstant
import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class EventHomeViewModel @Inject constructor(
        private val dispatcher: CoroutineDispatcher,
        private val graphqlRepository: GraphqlRepository
): BaseViewModel(dispatcher){

    private val mutableEventHomeListData = MutableLiveData<Result<List<HomeEventItem>>>()
    val eventHomeListData: LiveData<Result<List<HomeEventItem>>>
        get() = mutableEventHomeListData

    fun getIntialList() {
        val list: List<HomeEventItem> = requestEmptyItem()
        mutableEventHomeListData.value = Success(list)
    }

    fun getHomeData(isLoadFromCloud: Boolean){
        launchCatchError(block = {
            val graphqlRequest = GraphqlRequest(
                    EventQuery.getEventHomeQuery(),
                    EventHomeDataResponse.Data::class.java,
                    mapOf(CATEGORY to "event")
            )
            val cacheStrategy = GraphqlCacheStrategy.Builder(if (isLoadFromCloud) CacheType.CLOUD_THEN_CACHE else CacheType.CACHE_FIRST)
                    .setExpiryTime(GraphqlConstant.ExpiryTimes.MINUTE_1.`val`() * 5).build()
            val data = withContext(dispatcher) {
                graphqlRepository.getReseponse(listOf(graphqlRequest), cacheStrategy)
            }.getSuccessData<EventHomeDataResponse.Data>()

            mutableEventHomeListData.value = Success(mappingItem(data))
        }){
            mutableEventHomeListData.postValue(Fail(it))
        }
    }

    private fun requestEmptyItem(): List<HomeEventItem> {
        return listOf(LoadingHomeModel())
    }

    companion object {
        private const val CATEGORY = "category"
    }
}