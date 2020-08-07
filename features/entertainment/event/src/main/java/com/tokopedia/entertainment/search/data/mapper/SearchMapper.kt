package com.tokopedia.entertainment.search.data.mapper

import android.content.res.Resources
import com.tokopedia.entertainment.search.adapter.SearchEventItem
import com.tokopedia.entertainment.search.adapter.viewholder.HistoryBackgroundItemViewHolder
import com.tokopedia.entertainment.search.adapter.viewholder.SearchEventListViewHolder
import com.tokopedia.entertainment.search.adapter.viewholder.SearchLocationListViewHolder
import com.tokopedia.entertainment.search.adapter.viewmodel.*
import com.tokopedia.entertainment.search.data.EventSearchFullLocationResponse
import com.tokopedia.entertainment.search.data.EventSearchHistoryResponse
import com.tokopedia.entertainment.search.data.EventSearchLocationResponse

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
                category = event.childCategoryIds
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

    fun mapperLocationtoSearchList(dataLocation: EventSearchFullLocationResponse.Data): MutableList<SearchEventItem<*>>{
        val listViewHolder : MutableList<SearchEventItem<*>> = mutableListOf()
        val lists: MutableList<SearchLocationListViewHolder.LocationSuggestion> = mutableListOf()
        dataLocation.let {
            it.eventLocationSearch.let {
                if(it.count.toInt() > 0){
                    it.locations.forEach{
                        lists.add(mappingLocationFullSuggestion(it))
                    }
                    listViewHolder.add(SearchLocationModel(lists, allLocation = true))
                }
            }
        }

        return listViewHolder
    }

    fun mappingLocationandKegiatantoSearchList(dataLocation: EventSearchLocationResponse.Data, text:String, resources: Resources): MutableList<SearchEventItem<*>>{
        val listViewHolder : MutableList<SearchEventItem<*>> = mutableListOf()
        val listsLocation : MutableList<SearchLocationListViewHolder.LocationSuggestion> = mutableListOf()
        val listsKegiatan : MutableList<SearchEventListViewHolder.KegiatanSuggestion> = mutableListOf()
        dataLocation?.let {
            it.eventLocationSearch.let {
                if(it.count.toInt()  > 0){
                    it.locations.forEach {
                        listsLocation.add(SearchMapper.mappingLocationSuggestion(it))
                    }
                    listViewHolder.add(SearchLocationModel(listsLocation, query = text))
                }
            }
            it.eventSearch.let {
                if(it.count.toInt() > 0){
                    it.products.forEach{
                        listsKegiatan.add(SearchMapper.mappingEventSuggestion(it))
                    }
                    if(listsKegiatan.size > 0) listViewHolder.add(SearchEventModel(listsKegiatan, resources))
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
}