package com.tokopedia.deals.location_picker.ui.adapter.viewholder

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.deals.R
import com.tokopedia.deals.location_picker.listener.DealsLocationListener
import com.tokopedia.deals.location_picker.model.response.Location
import com.tokopedia.kotlin.extensions.view.loadImage

class PopularLocationViewHolder(itemView: View, private val locationListener: DealsLocationListener): AbstractViewHolder<Location>(itemView) {

    private var tvLocationName = itemView.findViewById<TextView>(R.id.tv_popular_loc_name)
    private var imgLocation = itemView.findViewById<ImageView>(R.id.img_popular_loc)
    private var tvLocationAddress = itemView.findViewById<TextView>(R.id.tv_popular_loc_address)
    private var tvLocationType = itemView.findViewById<TextView>(R.id.tv_popular_loc_type)

    override fun bind(element: Location) {
        tvLocationName.text = element.name
        tvLocationAddress.text = element.address
        tvLocationType.text = element.locType.displayName
        imgLocation.loadImage(element.iconApp)
        locationListener.onLocationClicked(itemView, element, adapterPosition)
    }

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_deals_popular_location_bottomsheet
    }
}