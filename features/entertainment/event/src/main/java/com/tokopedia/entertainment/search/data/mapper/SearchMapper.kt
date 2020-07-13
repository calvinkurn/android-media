package com.tokopedia.entertainment.search.data.mapper

import com.tokopedia.entertainment.search.adapter.viewholder.HistoryBackgroundItemViewHolder
import com.tokopedia.entertainment.search.adapter.viewholder.SearchEventListViewHolder
import com.tokopedia.entertainment.search.adapter.viewholder.SearchLocationListViewHolder
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

}