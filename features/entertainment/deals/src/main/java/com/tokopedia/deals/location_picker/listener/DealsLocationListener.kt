package com.tokopedia.deals.location_picker.listener

import android.view.View
import com.tokopedia.deals.location_picker.model.response.Location

interface DealsLocationListener {
    fun onCityClicked(itemView: View, location: Location, position: Int)
    fun onLocationClicked(itemView: View, location: Location, position: Int)
}