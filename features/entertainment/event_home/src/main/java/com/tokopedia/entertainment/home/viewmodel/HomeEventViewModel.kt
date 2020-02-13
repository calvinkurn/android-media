package com.tokopedia.entertainment.home.viewmodel

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.common.network.coroutines.repository.RestRepository
import com.tokopedia.common.network.data.model.RequestType
import com.tokopedia.common.network.data.model.RestRequest
import com.tokopedia.entertainment.R
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
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.*
import javax.inject.Inject

/**
 * Author errysuprayogi on 06,February,2020
 */

class HomeEventViewModel @Inject constructor(
        private val dispatcher: CoroutineDispatcher,
        private val gqlRepository: GraphqlRepository,
        private val restRepository: RestRepository,
        private val userSession: UserSessionInterface) : BaseViewModel(dispatcher) {

    companion object {
        val TAG = HomeEventViewModel::class.java.simpleName
        val CATEGORY = "category"
    }

    fun getHomeData(v: FragmentView, onSuccess: ((MutableList<HomeEventItem<*>>) -> Unit),
                    onError: ((Throwable) -> Unit), cacheType: CacheType) {
        launchCatchError(block = {
            val data = withContext(Dispatchers.IO) {
                val request = GraphqlRequest(
                        GraphqlHelper.loadRawString(v.getRes(), R.raw.event_home_query),
                        EventHomeDataResponse.Data::class.java, mapOf(CATEGORY to "event"))
                val cacheStrategy = GraphqlCacheStrategy
                        .Builder(cacheType).build()
                gqlRepository.getReseponse(listOf(request), cacheStrategy)
            }
            data.getSuccessData<EventHomeDataResponse.Data>().let {
                onSuccess(mappingItem(it))
            }
        }) {
            onError(it)
        }
    }

    fun postLiked(item: EventItemModel, onSuccessPostLike: ((EventItemModel) -> Unit),
                  onErrorPostLike: ((Throwable) -> Unit)) {
        launchCatchError(
                block = {
                    val result = withContext(Dispatchers.IO) {
                        val copy = item.copy(isLiked = item.isLiked != true)
                        val actionLikedRequest = ActionLikedRequest(ActionLikedRequest.Rating(
                                        isLiked = copy.isLiked.toString(),
                                        productId = copy.produkId,
                                        userId = userSession.userId.toIntOrZero()))
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
                    var data = (result.getData() as ActionLikedResponse).data
                    data?.let {
                        item.isLiked = it.isLiked
                        onSuccessPostLike(item)
                    }
                },
                onError = {
                    onErrorPostLike(it)
                }
        )
    }

    private fun mappingItem(data: EventHomeDataResponse.Data): MutableList<HomeEventItem<*>> {
        val items: MutableList<HomeEventItem<*>> = mutableListOf()
        val layouts = data.eventHome.layout
        val bannerItem: EventHomeDataResponse.Data.EventHome.Layout? = layouts.find { it.id.toIntOrZero() == 0 }
        bannerItem?.let {
            items.add(BannerViewModel(it))
            layouts.remove(it)
        }
        items.add(CategoryViewModel(data.eventChildCategory))
        layouts.let {
            it.forEachIndexed { index, it ->
                if (index == 2) {
                    items.add(EventLocationViewModel(data.eventLocationSearch))
                }
                if (it.isCard == 1) {
                    items.add(EventCarouselViewModel(it))
                } else {
                    items.add(EventGridViewModel(it))
                }
            }
        }
        return items
    }

}