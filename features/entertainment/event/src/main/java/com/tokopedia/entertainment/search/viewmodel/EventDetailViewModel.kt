package com.tokopedia.entertainment.search.viewmodel

import android.content.res.Resources
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.entertainment.R
import com.tokopedia.entertainment.search.adapter.DetailEventItem
import com.tokopedia.entertainment.search.adapter.viewholder.CategoryTextBubbleAdapter
import com.tokopedia.entertainment.search.adapter.viewholder.EventGridAdapter
import com.tokopedia.entertainment.search.data.CategoryModel
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
import kotlinx.coroutines.withContext
import timber.log.Timber

class EventDetailViewModel(private val dispatcher: CoroutineDispatcher,
                           private val gqlRepository: GraphqlRepository,
                           private val userSession: UserSessionInterface) : BaseViewModel(dispatcher) {

    companion object{
        private val TAG = EventDetailViewModel::class.java.simpleName
        private val CATEGORYID = "category_ids"
        private val CITIES = "cities"
        private val PAGE = "page"
    }

    val hashSet = HashSet<String>()
    lateinit var resources: Resources
    var cityID = ""
    var category = ""
    var initCategory = false
    var categoryModel =  CategoryModel()
    var page = "1"

    val isItRefreshing : MutableLiveData<Boolean> by lazy { MutableLiveData<Boolean>() }
    val isItShimmering : MutableLiveData<Boolean> by lazy { MutableLiveData<Boolean>() }
    val showParentView : MutableLiveData<Boolean> by lazy { MutableLiveData<Boolean>() }
    val showResetFilter : MutableLiveData<Boolean> by lazy { MutableLiveData<Boolean>() }
    val showProgressBar : MutableLiveData<Boolean> by lazy { MutableLiveData<Boolean>() }
    val errorReport : MutableLiveData<String> by lazy { MutableLiveData<String>() }

    val catLiveData: MutableLiveData<CategoryModel> by lazy { MutableLiveData<CategoryModel>() }
    val categoryData : MutableList<CategoryTextBubbleAdapter.CategoryTextBubble> = mutableListOf()

    val eventLiveData: MutableLiveData<MutableList<EventGridAdapter.EventGrid>> by lazy { MutableLiveData<MutableList<EventGridAdapter.EventGrid>>() }

    fun getData(cacheType: CacheType = CacheType.CACHE_FIRST){
        if(category.isBlank()) hashSet.clear()

        launchCatchError(
                block = {
                    if(page == "1") {
                        isItShimmering.value = true
                        showParentView.value = false
                    } else showProgressBar.value = true
                    showResetFilter.value = false
                    val eventData : MutableList<EventGridAdapter.EventGrid> = mutableListOf()
                    val data = getQueryData(cacheType)
                    data.let {
                        it.eventChildCategory.let {
                            if(categoryIsDifferentOrEmpty(it)){
                                categoryData.clear()
                                it.categories.forEach {
                                    if(it.title != "Trending Events" || it.id != "28"){ //Feedback #3 Remove Trending Events
                                        categoryData.add(DetailMapper.mapToCategory(it))
                                    }
                                }
                                categoryModel.listCategory = categoryData

                                if(initCategory) {
                                    categoryModel.hashSet = hashSet
                                    categoryData.forEachIndexed{index, it ->
                                        if(hashSet.contains(it.id)){
                                            categoryModel.position = index
                                            return@forEachIndexed
                                        }
                                    }
                                    initCategory=false
                                } else {
                                    categoryModel.hashSet = HashSet()
                                    categoryModel.position = -1
                                }

                                catLiveData.value = categoryModel
                            }
                        }

                        it.eventSearch.let {
                            if(it.products.isNotEmpty()){
                                it.products.forEach {
                                    eventData.add(DetailMapper.mapToGrid(it))
                                }
                            }

                            eventLiveData.value = eventData
                            isItRefreshing.value = false
                            isItShimmering.value = false
                            if(page == "1" && eventData.isNotEmpty()) showParentView.value = true
                            else if(page == "1" && eventData.isEmpty()) showResetFilter.value = true
                            else if(page != "1") showProgressBar.value = false
                        }
                    }
                },
                onError = {
                    Timber.tag(TAG + "Error").w(it)
                    errorReport.value = it.message
                    isItRefreshing.value = false
                    isItShimmering.value = false
                    showParentView.value = false
                    showProgressBar.value = false
                    showResetFilter.value = true
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
        resetPage()
        hashSet.clear()
        hashToString()
        getData()
    }

    fun putCategoryToQuery(id : String){
        resetPage()
        if(hashSet.contains(id)) hashSet.remove(id)
        else hashSet.add(id)

        hashToString()
        getData()
    }

    private fun resetPage() { page = "1" }

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

    suspend fun getQueryData(cacheType: CacheType) : EventDetailResponse.Data{
        return withContext(Dispatchers.IO){
            val req = GraphqlRequest(
                    GraphqlHelper.loadRawString(resources, R.raw.query_event_search_category),
                    EventDetailResponse.Data::class.java, mapOf(CATEGORYID to category, CITIES to cityID, PAGE to page))
            val cacheStrategy = GraphqlCacheStrategy.Builder(cacheType).build()
            gqlRepository.getReseponse(listOf(req), cacheStrategy).getSuccessData<EventDetailResponse.Data>()
        }
    }
}