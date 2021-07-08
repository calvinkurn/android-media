package com.tokopedia.flight.detail.view.adapter.viewholder

import android.view.View
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.flight.R
import com.tokopedia.flight.detail.util.FlightAmenityIconUtil.getImageResource
import com.tokopedia.flight.search.data.cloud.single.Amenity

/**
 * Created by furqan on 06/10/21.
 */
class FlightDetailFacilityAmenityViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val imageAmenity: AppCompatImageView = itemView.findViewById<View>(R.id.image_amenity) as AppCompatImageView
    private val textAmenity: TextView = itemView.findViewById<View>(R.id.text_amenity) as TextView

    fun bindData(amenity: Amenity) {
        if (amenity.isDefault) {
            imageAmenity.visibility = View.GONE
            textAmenity.visibility = View.GONE
        } else {
            imageAmenity.visibility = View.VISIBLE
            textAmenity.visibility = View.VISIBLE
            imageAmenity.setImageDrawable(MethodChecker.getDrawable(imageAmenity.context, getImageResource(amenity.icon)))
            textAmenity.text = amenity.label
        }
    }

}