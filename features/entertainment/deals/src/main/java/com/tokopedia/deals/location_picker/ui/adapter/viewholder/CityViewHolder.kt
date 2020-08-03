package com.tokopedia.deals.location_picker.ui.adapter.viewholder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.deals.R
import com.tokopedia.deals.location_picker.listener.DealsLocationListener
import com.tokopedia.deals.location_picker.model.response.Location
import com.tokopedia.kotlin.extensions.view.loadImage
import com.tokopedia.unifycomponents.ChipsUnify

class CityViewHolder(itemView: View, private val locationListener: DealsLocationListener): RecyclerView.ViewHolder(itemView) {

    private val cityView: ChipsUnify = itemView.findViewById(R.id.chip_item)

    fun bindData(location: Location) {
        cityView.chip_text.text = location.name
        cityView.chip_image_icon.loadImage(location.iconApp)
        cityView.chip_image_icon.visibility = View.VISIBLE
        locationListener.onCityClicked(itemView, location, adapterPosition)
    }
}