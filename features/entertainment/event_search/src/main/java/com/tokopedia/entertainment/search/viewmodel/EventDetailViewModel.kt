package com.tokopedia.entertainment.search.viewmodel

import android.content.res.Resources
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.entertainment.search.R
import com.tokopedia.entertainment.search.adapter.DetailEventItem
import com.tokopedia.entertainment.search.adapter.viewholder.CategoryTextViewHolder
import com.tokopedia.entertainment.search.adapter.viewholder.SearchEventGridViewHolder
import com.tokopedia.entertainment.search.adapter.viewmodel.CategoryTextViewModel
import com.tokopedia.entertainment.search.adapter.viewmodel.ResetFilterViewModel
import com.tokopedia.entertainment.search.adapter.viewmodel.SearchEventGridViewModel
import com.tokopedia.entertainment.search.data.EventDetailResponse
import com.tokopedia.entertainment.search.data.mapper.DetailMapper
import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.usecase.launch_cache_error.launchCatchError
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.withContext
import timber.log.Timber

class EventDetailViewModel(private val dispatcher: CoroutineDispatcher,
                           private val gqlRepository: GraphqlRepository,
                           private val userSession: UserSessionInterface) : BaseViewModel(dispatcher) {

    companion object{
        private val TAG = EventDetailViewModel::class.java.simpleName
        private val CATEGORYID = "category_ids"
        private val CITIES = "cities"
    }

    val hashSet = HashSet<String>()
    lateinit var resources: Resources
    var cityID: String = ""
    var category : String = ""
    var initCategory = false

    val isItRefreshing : MutableLiveData<Boolean> by lazy { MutableLiveData<Boolean>() }

    val catLiveData: MutableLiveData<List<DetailEventItem<*>>> by lazy { MutableLiveData<List<DetailEventItem<*>>>() }
    val eventLiveData : MutableLiveData<List<DetailEventItem<*>>> by lazy { MutableLiveData<List<DetailEventItem<*>>>() }
    val errorReport : MutableLiveData<String> by lazy { MutableLiveData<String>() }
    val categoryData : MutableList<CategoryTextViewHolder.CategoryTextBubble> = mutableListOf()

    val categoryList : MutableList<DetailEventItem<*>> = mutableListOf()
    val eventList : MutableList<DetailEventItem<*>> = mutableListOf()

    fun getData(){
        if(category.isBlank()) hashSet.clear()

        launchCatchError(
                 block = {
                     val eventData : MutableList<SearchEventGridViewHolder.EventGrid> = mutableListOf()
                     val data = getQueryData(this.cityID, category)
                     data.let {
                         it.eventChildCategory.let {
                             if(categoryIsDifferentOrEmpty(it)){
                                 categoryData.clear()
                                 it.categories.forEach {
                                     if(it.title != "Trending Events" || it.id != "28"){ //Feedback #3 Remove Trending Events
                                         categoryData.add(DetailMapper.mapToCategory(it))
                                     }
                                 }
                                 categoryList.clear()
                                 //If there is initial category from the app link, need to pass the hashset to the viewmodel to turn the category green else no
                                 categoryList.add(CategoryTextViewModel(categoryData, if(initCategory) hashSet else HashSet()))
                                 catLiveData.value = categoryList
                                 if(initCategory) initCategory=false
                             }
                         }

                         it.eventSearch.let {
                             eventList.clear()
                             if(it.products.size  > 0){
                                 it.products.forEach {
                                     eventData.add(DetailMapper.mapToGrid(it))
                                 }
                                 eventList.add(SearchEventGridViewModel(eventData))
                             }else{
                                 eventList.add(ResetFilterViewModel())
                             }
                             eventLiveData.value = eventList
                             isItRefreshing.value = false
                         }
                     }
                 },
                 onError = {
                     Timber.tag(TAG + "Error").w(it)
                     errorReport.value = it.message
                     isItRefreshing.value = false
                 }
        )
    }

    fun categoryIsDifferentOrEmpty(list: EventDetailResponse.Data.EventChildCategory): Boolean{
        //Case 1 Still empty or Data Category size and API Category Size is different
        if(categoryData.size == 0 || hashSet.isEmpty() || categoryData.size != list.categories.size-1) return true

        //Add all id to hashset
        val listHashSet = HashSet<String>()
        list.categories.forEach{item -> listHashSet.add(item.id) }

        //Case 2 Data Category Size is Different but the data is different then reload the data
        categoryData.forEachIndexed{index, it -> if(!listHashSet.contains(categoryData.get(index).id)) return true }

        //Default no need to change the data
        return false
    }

    fun clearFilter(){
        hashSet.clear()
        hashToString()
        getData()
    }

    fun putCategoryToQuery(id : String){
        if(hashSet.contains(id)) hashSet.remove(id)
        else hashSet.add(id)

        hashToString()
        getData()
    }

    private fun hashToString(){
        var stringHash = ""
        hashSet.forEachIndexed{ index, item ->
            stringHash += item
            stringHash += (if (index == hashSet.size-1) "" else ",")
        }
        category = stringHash
    }

    fun setData(cityID: String){
        this.cityID = cityID
    }

    suspend fun getQueryData(cityID: String, category: String = "") : EventDetailResponse.Data{
        return withContext(Dispatchers.IO){
            val req = GraphqlRequest(
                    GraphqlHelper.loadRawString(resources, R.raw.query_event_search_category),
                    EventDetailResponse.Data::class.java, mapOf(CATEGORYID to category, CITIES to cityID))
            val cacheStrategy = GraphqlCacheStrategy.Builder(CacheType.CACHE_FIRST).build()
            gqlRepository.getReseponse(listOf(req), cacheStrategy).getSuccessData<EventDetailResponse.Data>()
        }
    }
}