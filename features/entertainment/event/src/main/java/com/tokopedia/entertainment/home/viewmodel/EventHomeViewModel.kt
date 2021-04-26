package com.tokopedia.entertainment.home.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
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
import kotlinx.coroutines.withContext
import javax.inject.Inject

class EventHomeViewModel @Inject constructor(
        private val dispatcher: CoroutineDispatchers,
        private val graphqlRepository: GraphqlRepository,
        private val restRepository: RestRepository
): BaseViewModel(dispatcher.io){

    private val mutableEventHomeListData = MutableLiveData<Result<List<HomeEventItem>>>()
    val eventHomeListData: LiveData<Result<List<HomeEventItem>>>
        get() = mutableEventHomeListData

    private val mutableLikedProduct = MutableLiveData<EventItemModel>()
    val eventLikedProduct: LiveData<EventItemModel>
        get() = mutableLikedProduct

    private val mutableErrorLikedProduct = MutableLiveData<Throwable>()
    val errorLikedProduct: LiveData<Throwable>
        get() = mutableErrorLikedProduct

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
            val data = withContext(dispatcher.io) {
                graphqlRepository.getReseponse(listOf(graphqlRequest), cacheStrategy)
            }.getSuccessData<EventHomeDataResponse.Data>()

            mutableEventHomeListData.postValue(Success(mappingItem(data)))
        }){
            mutableEventHomeListData.postValue(Fail(it))
        }
    }

    fun likeProduct(item: EventItemModel, userId: String){
        launchCatchError(
                block = {
                    val result = withContext(dispatcher.io) {
                        val copy = item.copy(isLiked = item.isLiked != true)
                        val actionLikedRequest = ActionLikedRequest(ActionLikedRequest.Rating(
                                isLiked = copy.isLiked.toString(),
                                productId = copy.produkId,
                                userId = userId.toIntOrZero()))
                        val headers = HashMap<String, String>()
                        headers.put("Content-Type", "application/json")
                        val restRequest = RestRequest.Builder(BASE_REST_URL + PATH_EVENTS_LIKES,
                                object : TypeToken<ActionLikedResponse>() {}.type)
                                .setBody(Gson().toJson(actionLikedRequest))
                                .setHeaders(headers)
                                .setRequestType(RequestType.POST)
                                .build()
                        restRepository.getResponse(restRequest)
                    }
                    val data = (result.getData() as ActionLikedResponse).data
                    data?.let {
                        item.isLiked = it.isLiked
                        mutableLikedProduct.postValue(item)
                    }
                },
                onError = {
                    mutableErrorLikedProduct.postValue(it)
                }
        )
    }

    private fun mappingItem(data: EventHomeDataResponse.Data?): MutableList<HomeEventItem> {
        val items: MutableList<HomeEventItem> = mutableListOf()
        data?.let {
            val layouts = it.eventHome.layout
            val bannerItem: EventHomeDataResponse.Data.EventHome.Layout? = layouts.find { it.id.toIntOrZero() == 0 }
            bannerItem?.let {
                items.add(BannerModel(it))
                layouts.remove(it)
            }
            items.add(CategoryModel(data.eventChildCategory))
            layouts.let {
                it.forEachIndexed { index, it ->
                    if (index == 2) {
                        items.add(EventLocationModel(data.eventLocationSearch))
                    }
                    if (it.isCard == 1) {
                        items.add(EventCarouselModel(it))
                    } else {
                        items.add(EventGridModel(it))
                    }
                }
            }
        }
        return items
    }

    private fun requestEmptyItem(): List<HomeEventItem> {
        return listOf(EmptyHomeModel())
    }

    companion object {
        private const val CATEGORY = "category"
    }
}