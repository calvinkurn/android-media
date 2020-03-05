package com.tokopedia.hotel.homepage.presentation.model

import com.tokopedia.common.travel.data.entity.TravelRecentSearchModel

/**
 * @author by furqan on 14/01/2020
 */
data class HotelRecentSearchModel(val title: String, val items: List<TravelRecentSearchModel.Item>)