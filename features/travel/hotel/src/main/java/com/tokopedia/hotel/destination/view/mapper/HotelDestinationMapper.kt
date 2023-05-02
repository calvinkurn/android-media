package com.tokopedia.hotel.destination.view.mapper

import com.tokopedia.hotel.destination.data.model.HotelSuggestion
import com.tokopedia.hotel.destination.data.model.SearchDestination
import javax.inject.Inject

class HotelDestinationMapper @Inject constructor() {

    fun mapSource(data: HotelSuggestion.Response): List<SearchDestination> {
        return data.propertySearchSuggestion.searchDestinationList.map {
            it.source = data.propertySearchSuggestion.source
            it
        }
    }
}
