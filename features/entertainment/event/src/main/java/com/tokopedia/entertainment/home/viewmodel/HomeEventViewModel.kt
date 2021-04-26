package com.tokopedia.entertainment.home.viewmodel

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.common.network.coroutines.repository.RestRepository
import com.tokopedia.common.network.data.model.RequestType
import com.tokopedia.common.network.data.model.RestRequest
import com.tokopedia.entertainment.home.UrlConstant.BASE_REST_URL
import com.tokopedia.entertainment.home.UrlConstant.PATH_EVENTS_LIKES
import com.tokopedia.entertainment.home.adapter.HomeEventItem
import com.tokopedia.entertainment.home.adapter.viewmodel.*
import com.tokopedia.entertainment.home.data.ActionLikedRequest
import com.tokopedia.entertainment.home.data.ActionLikedResponse
import com.tokopedia.entertainment.home.data.EventHomeDataResponse
import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.usecase.launch_cache_error.launchCatchError
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.*
import java.util.*
import javax.inject.Inject

/**
 * Author errysuprayogi on 06,February,2020
 */

//class HomeEventViewModel @Inject constructor(
//        private val dispatcher: CoroutineDispatcher,
//        private val gqlRepository: GraphqlRepository,
//        private val restRepository: RestRepository,
//        private val userSession: UserSessionInterface) : BaseViewModel(dispatcher) {
//
//    companion object {
//        val TAG = HomeEventViewModel::class.java.simpleName
//        val CATEGORY = "category"
//    }
//
//    fun getHomeData(queryHome: String, onSuccess: ((MutableList<HomeEventItem<*>>) -> Unit),
//                    onError: ((Throwable) -> Unit), cacheType: CacheType) {
//        launchCatchError(
//                block = {
//                     var data = fetchEventHomeData(queryHome, cacheType)
//                     data?.let {
//                         onSuccess(mappingItem(it))
//                      }
//                },
//                onError = {
//                    onError(it)
//                }
//        )
//    }
//
//    private suspend fun fetchEventHomeData(queryHome: String, cacheType: CacheType): EventHomeDataResponse.Data {
//        return withContext(dispatcher){
//            val request = GraphqlRequest(queryHome,
//                    EventHomeDataResponse.Data::class.java, mapOf(CATEGORY to "event"))
//            val cacheStrategy = GraphqlCacheStrategy
//                    .Builder(cacheType).build()
//            gqlRepository.getReseponse(listOf(request), cacheStrategy).getSuccessData<EventHomeDataResponse.Data>()
//        }
//    }
//
//    fun postLiked(item: EventItemModel, onSuccessPostLike: ((EventItemModel) -> Unit),
//                  onErrorPostLike: ((Throwable) -> Unit)) {
//        launchCatchError(
//                block = {
//                    val result = withContext(dispatcher) {
//                        val copy = item.copy(isLiked = item.isLiked != true)
//                        val actionLikedRequest = ActionLikedRequest(ActionLikedRequest.Rating(
//                                isLiked = copy.isLiked.toString(),
//                                productId = copy.produkId,
//                                userId = userSession.userId.toIntOrZero()))
//                        val headers = HashMap<String, String>()
//                        headers.put("Content-Type", "application/json")
//                        val restRequest = RestRequest.Builder(BASE_REST_URL + PATH_EVENTS_LIKES,
//                                object : TypeToken<ActionLikedResponse>() {}.type)
//                                .setBody(Gson().toJson(actionLikedRequest))
//                                .setHeaders(headers)
//                                .setRequestType(RequestType.POST)
//                                .build()
//                        restRepository.getResponse(restRequest)
//                    }
//                    var data = (result.getData() as ActionLikedResponse).data
//                    data?.let {
//                        item.isLiked = it.isLiked
//                        onSuccessPostLike(item)
//                    }
//                },
//                onError = {
//                    onErrorPostLike(it)
//                }
//        )
//    }
//
//    private fun mappingItem(data: EventHomeDataResponse.Data?): MutableList<HomeEventItem<*>> {
//        val items: MutableList<HomeEventItem<*>> = mutableListOf()
//        data?.let {
//            val layouts = it.eventHome.layout
//            val bannerItem: EventHomeDataResponse.Data.EventHome.Layout? = layouts.find { it.id.toIntOrZero() == 0 }
//            bannerItem?.let {
//                items.add(BannerModel(it))
//                layouts.remove(it)
//            }
//            items.add(CategoryModel(data.eventChildCategory))
//            layouts.let {
//                it.forEachIndexed { index, it ->
//                    if (index == 2) {
//                        items.add(EventLocationModel(data.eventLocationSearch))
//                    }
//                    if (it.isCard == 1) {
//                        items.add(EventCarouselModel(it))
//                    } else {
//                        items.add(EventGridModel(it))
//                    }
//                }
//            }
//        }
//        return items
//    }
//
//}