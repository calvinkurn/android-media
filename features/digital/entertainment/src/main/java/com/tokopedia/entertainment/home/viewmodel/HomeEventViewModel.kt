package com.tokopedia.entertainment.home.viewmodel

import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.entertainment.R
import com.tokopedia.entertainment.home.adapter.HomeEventItem
import com.tokopedia.entertainment.home.adapter.viewmodel.*
import com.tokopedia.entertainment.home.data.EventHomeDataResponse
import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.usecase.launch_cache_error.launchCatchError
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * Author errysuprayogi on 06,February,2020
 */

class HomeEventViewModel @Inject constructor(
        private val dispatcher: CoroutineDispatcher,
        private val repository: GraphqlRepository) : BaseViewModel(dispatcher) {

    companion object{
        val TAG = HomeEventViewModel::class.java.simpleName
        val CATEGORY = "category"
    }

    fun getHomeData(v: FragmentView, onSuccess: ((MutableList<HomeEventItem<*>>) -> Unit),
                    onError: ((Throwable) -> Unit)){
        launchCatchError(block = {
            val data = withContext(Dispatchers.IO) {
                val request = GraphqlRequest(
                        GraphqlHelper.loadRawString(v.getRes(), R.raw.event_home_query),
                        EventHomeDataResponse.Data::class.java, mapOf(CATEGORY to "event"))
                val cacheStrategy = GraphqlCacheStrategy
                        .Builder(CacheType.ALWAYS_CLOUD).build()
                repository.getReseponse(listOf(request), cacheStrategy)
            }
            data.getSuccessData<EventHomeDataResponse.Data>().let {
                onSuccess(mappingItem(it))
            }
        }) {
            onError(it)
        }
    }

    private fun mappingItem(data: EventHomeDataResponse.Data): MutableList<HomeEventItem<*>> {
        val items: MutableList<HomeEventItem<*>> = mutableListOf()
        val layouts = data.eventHome?.layout
        val bannerItem: EventHomeDataResponse.Data.EventHome.Layout? = layouts?.find { it.id.toInt() == 0}
        bannerItem?.let {
            items.add(BannerViewModel(it))
            layouts?.remove(it)
        }
        items.add(CategoryViewModel(data.eventChildCategory))
        layouts?.let {
            it.forEachIndexed { index, it ->
                if(index == 2){
                    items.add(EventLocationViewModel(data.eventLocationSearch))
                }
                when(it.id.toInt()){
                    28 -> items.add(EventCarouselViewModel(it))
                    else -> items.add(EventGridViewModel(it))
                }
            }
        }
        return items
    }
}