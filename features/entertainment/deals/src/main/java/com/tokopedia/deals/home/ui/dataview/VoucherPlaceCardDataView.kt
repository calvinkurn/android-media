package com.tokopedia.deals.home.ui.dataview

import com.tokopedia.deals.location_picker.model.response.Location

data class VoucherPlaceCardDataView(
    val id: String = "",
    val imageUrl: String = "",
    val name: String = "",
    val count: String = "",
    val appUrl: String = "",
    val location: Location = Location()
)