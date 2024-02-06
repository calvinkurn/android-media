package com.tokopedia.deals.ui.location_picker.listener

import android.view.View
import com.tokopedia.deals.ui.location_picker.model.response.Location

interface DealsLocationListener {
    fun onCityClicked(itemView: View, location: Location, position: Int)
    fun onLocationClicked(itemView: View, location: Location, position: Int)
}
