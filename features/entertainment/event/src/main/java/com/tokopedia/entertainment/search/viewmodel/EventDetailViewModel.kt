package com.tokopedia.entertainment.search.viewmodel

import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.entertainment.search.adapter.viewholder.CategoryTextBubbleAdapter
import com.tokopedia.entertainment.search.adapter.viewholder.EventGridAdapter
import com.tokopedia.entertainment.search.data.CategoryModel
import com.tokopedia.entertainment.search.data.EventDetailResponse
import com.tokopedia.entertainment.search.data.mapper.SearchMapper.mapInitCategory
import com.tokopedia.entertainment.search.data.mapper.SearchMapper.mapSearchtoGrid
import com.tokopedia.entertainment.search.data.mapper.SearchMapper.mappingForbiddenID
import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.usecase.launch_cache_error.launchCatchError
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import timber.log.Timber

class EventDetailViewModel(private val dispatcher: CoroutineDispatcher,
                           private val gqlRepository: GraphqlRepository) : BaseViewModel(dispatcher) {

    companion object{
        private val TAG = EventDetailViewModel::class.java.simpleName
        private val CATEGORYID = "category_ids"
        private val CITIES = "cities"
        private val PAGE = "page"
        const val FORBIDDEN_TITLE =  "Trending Events"
        const val FORBIDDEN_ID = "28"
    }

    val hashSet = HashSet<String>()
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
    var categoryData : MutableList<CategoryTextBubbleAdapter.CategoryTextBubble> = mutableListOf()

    val eventLiveData: MutableLiveData<MutableList<EventGridAdapter.EventGrid>> by lazy { MutableLiveData<MutableList<EventGridAdapter.EventGrid>>() }

    fun getData(cacheType: CacheType = CacheType.CACHE_FIRST, query: String){
        if(category.isBlank()) hashSet.clear()

        launchCatchError(
                block = {
                    if(page == "1") {
                        isItShimmering.value = true
                        showParentView.value = false
                    } else showProgressBar.value = true
                    showResetFilter.value = false

                    val data = getQueryData(cacheType, query)
                    data.let {
                        it.eventChildCategory.let {
                            if(categoryIsDifferentOrEmpty(it)){
                                categoryData.clear()
                                categoryData = mappingForbiddenID(it.categories)

                                categoryModel.listCategory = categoryData

                                if(initCategory) {
                                    mapInitCategory(categoryData,hashSet,categoryModel)
                                    initCategory=false
                                } else {
                                    categoryModel.hashSet = HashSet()
                                    categoryModel.position = -1
                                }

                                catLiveData.value = categoryModel
                            }
                        }

                        it.eventSearch.let {
                            eventLiveData.value = mapSearchtoGrid(it)
                            isItRefreshing.value = false
                            isItShimmering.value = false
                            if(page == "1" && mapSearchtoGrid(it).isNotEmpty()) showParentView.value = true
                            else if(page == "1" && mapSearchtoGrid(it).isEmpty()) showResetFilter.value = true
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

    private fun categoryIsDifferentOrEmpty(list: EventDetailResponse.Data.EventChildCategory): Boolean{
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

    fun clearFilter(query: String){
        resetPage()
        hashSet.clear()
        hashToString()
        getData(query=query)
    }

    fun putCategoryToQuery(id : String, query: String){
        resetPage()
        if(hashSet.contains(id)) hashSet.remove(id)
        else hashSet.add(id)

        hashToString()
        getData(query=query)
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

    suspend fun getQueryData(cacheType: CacheType, query:String) : EventDetailResponse.Data{
        return withContext(dispatcher){
            val req = GraphqlRequest(
                    query,
                    EventDetailResponse.Data::class.java, mapOf(CATEGORYID to category, CITIES to cityID, PAGE to page))
            val cacheStrategy = GraphqlCacheStrategy.Builder(cacheType).build()
            gqlRepository.getReseponse(listOf(req), cacheStrategy).getSuccessData<EventDetailResponse.Data>()
        }
    }
}