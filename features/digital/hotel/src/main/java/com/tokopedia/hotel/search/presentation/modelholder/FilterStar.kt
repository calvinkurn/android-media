package com.tokopedia.hotel.search.presentation.modelholder

import com.tokopedia.hotel.search.presentation.adapter.HotelSearchFilterAdapter

data class FilterStar(
        private val title: String = "",
        private val id: String = ""
): HotelSearchFilterAdapter.HotelFilterItem {
    override fun isRateItem(): Boolean = true

    override fun getItemId(): String = id

    override fun getItemTitle(): String = title
}