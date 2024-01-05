package com.tokopedia.deals.ui.home.ui.dataview

import com.tokopedia.deals.ui.location_picker.model.response.Location

data class VoucherPlaceCardDataView(
    val id: String = "",
    val imageUrl: String = "",
    val name: String = "",
    val count: String = "",
    val appUrl: String = "",
    val location: Location = Location()
)
