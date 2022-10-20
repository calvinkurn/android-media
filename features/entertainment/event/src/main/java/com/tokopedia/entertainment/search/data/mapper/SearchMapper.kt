package com.tokopedia.entertainment.search.data.mapper

import com.tokopedia.entertainment.search.adapter.SearchEventItem
import com.tokopedia.entertainment.search.adapter.viewholder.CategoryTextBubbleAdapter
import com.tokopedia.entertainment.search.adapter.viewholder.EventGridAdapter
import com.tokopedia.entertainment.search.adapter.viewholder.HistoryBackgroundItemViewHolder
import com.tokopedia.entertainment.search.adapter.viewholder.SearchEventListViewHolder
import com.tokopedia.entertainment.search.adapter.viewholder.SearchLocationListViewHolder
import com.tokopedia.entertainment.search.adapter.viewmodel.FirstTimeModel
import com.tokopedia.entertainment.search.adapter.viewmodel.HistoryModel
import com.tokopedia.entertainment.search.adapter.viewmodel.SearchEmptyStateModel
import com.tokopedia.entertainment.search.adapter.viewmodel.SearchEventModel
import com.tokopedia.entertainment.search.adapter.viewmodel.SearchLocationModel
import com.tokopedia.entertainment.search.data.CategoryModel
import com.tokopedia.entertainment.search.data.EventDetailResponse
import com.tokopedia.entertainment.search.data.EventSearchFullLocationResponse
import com.tokopedia.entertainment.search.data.EventSearchHistoryResponse
import com.tokopedia.entertainment.search.data.EventSearchLocationResponse
import com.tokopedia.entertainment.search.viewmodel.EventDetailViewModel
import com.tokopedia.kotlin.extensions.view.toIntSafely

/**
 * Author errysuprayogi on 05,March,2020
 */

object SearchMapper {

    fun mappingRecentSearch(event: EventSearchHistoryResponse.Data.TravelCollectiveRecentSearches.ItemsItem) : HistoryBackgroundItemViewHolder.EventModel{
        return HistoryBackgroundItemViewHolder.EventModel(
                nama_event = event.title,
                lokasi_event = event.subtitle,
                tanggal_event = "",
                image_url = event.imageURL,
                app_url = event.appURL
        )
    }

    fun mappingLocationSuggestion(location: EventSearchLocationResponse.Data.EventLocationSearch.LocationsItem) : SearchLocationListViewHolder.LocationSuggestion{
        return SearchLocationListViewHolder.LocationSuggestion(
                id_city = location.id,
                city = location.name,
                country = "",
                type = "Kota",
                imageUrl = location.imageApp
        )
    }

    fun mappingEventSuggestion(event: EventSearchLocationResponse.Data.EventSearch.ProductsItem) : SearchEventListViewHolder.KegiatanSuggestion{
        return SearchEventListViewHolder.KegiatanSuggestion(
                id = event.id,
                price = event.price,
                nama_kegiatan = event.displayName,
                tanggal_kegiatan = event.minStartDate,
                lokasi_kegiatan = event.cityName,
                image_url = event.imageApp,
                isLiked = event.isLiked,
                app_url = event.appUrl,
                category = event.childCategoryIds,
                sales_price = event.salesPrice
        )
    }

    fun mappingLocationFullSuggestion(location: EventSearchFullLocationResponse.Data.EventLocationSearch.LocationsItem) : SearchLocationListViewHolder.LocationSuggestion{
        return SearchLocationListViewHolder.LocationSuggestion(
                id_city = location.id,
                city = location.name,
                country = "",
                type = "Kota",
                imageUrl = location.imageApp
        )
    }

    fun mappingLocationandKegiatantoSearchList(dataLocation: EventSearchLocationResponse.Data, text:String): MutableList<SearchEventItem<*>>{
        val listViewHolder : MutableList<SearchEventItem<*>> = mutableListOf()
        val listsLocation : MutableList<SearchLocationListViewHolder.LocationSuggestion> = mutableListOf()
        val listsKegiatan : MutableList<SearchEventListViewHolder.KegiatanSuggestion> = mutableListOf()
        dataLocation?.let {
            it.eventLocationSearch.let {
                if(it.count.toIntSafely()  > 0){
                    it.locations.forEach {
                        listsLocation.add(SearchMapper.mappingLocationSuggestion(it))
                    }
                    listViewHolder.add(SearchLocationModel(listsLocation, query = text))
                }
            }
            it.eventSearch.let {
                if(it.count.toIntSafely() > 0){
                    it.products.forEach{
                        listsKegiatan.add(SearchMapper.mappingEventSuggestion(it))
                    }
                    if(listsKegiatan.size > 0) listViewHolder.add(SearchEventModel(listsKegiatan))
                }
            }
            if(it.eventLocationSearch.locations.isEmpty() && it.eventSearch.products.isEmpty()) {
                listViewHolder.clear()
                listViewHolder.add(SearchEmptyStateModel())
            }
        }
         return listViewHolder
    }

    fun mappingHistorytoSearchList(history :  EventSearchHistoryResponse.Data):MutableList<SearchEventItem<*>> {
        val listViewHolder: MutableList<SearchEventItem<*>> = mutableListOf()
        val lists: MutableList<HistoryBackgroundItemViewHolder.EventModel> = mutableListOf()
        history.travelCollectiveRecentSearches.let {
            if (it.items.isNotEmpty()) {
                it.items.forEach {
                    lists.add(mappingRecentSearch(it))
                }
                listViewHolder.add(HistoryModel(lists))
            } else {
                listViewHolder.add(FirstTimeModel())
            }
        }
        return listViewHolder
    }

    fun mappingForbiddenID( categories: List<EventDetailResponse.Data.EventChildCategory.CategoriesItem>): MutableList<CategoryTextBubbleAdapter.CategoryTextBubble> {
        val categoryData : MutableList<CategoryTextBubbleAdapter.CategoryTextBubble> = mutableListOf()
        categories.let{
           it.forEach {
                if (it.title != EventDetailViewModel.FORBIDDEN_TITLE || it.id != EventDetailViewModel.FORBIDDEN_ID) { //Feedback #3 Remove Trending Events
                    categoryData.add(DetailMapper.mapToCategory(it))
                }
            }
        }
        return categoryData
    }

    fun mapInitCategory(categoryData:MutableList<CategoryTextBubbleAdapter.CategoryTextBubble>, hashSet:HashSet<String>, categoryModel: CategoryModel){
        categoryModel.hashSet = hashSet
        categoryData.forEachIndexed{index, it ->
            if(hashSet.contains(it.id)){
                categoryModel.position = index
                return@forEachIndexed
            }
        }
    }

    fun mapSearchtoGrid(eventSearch : EventDetailResponse.Data.EventSearch): MutableList<EventGridAdapter.EventGrid>{
        val eventData : MutableList<EventGridAdapter.EventGrid> = mutableListOf()
        if(eventSearch.products.isNotEmpty()){
            eventSearch.products.forEach {
                eventData.add(DetailMapper.mapToGrid(it))
            }
        }

        return eventData
    }


}