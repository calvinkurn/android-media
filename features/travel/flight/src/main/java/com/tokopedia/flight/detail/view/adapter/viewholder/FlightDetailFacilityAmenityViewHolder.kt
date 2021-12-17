package com.tokopedia.flight.detail.view.adapter.viewholder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.flight.databinding.ItemFlightDetailFacilityAmenityBinding
import com.tokopedia.flight.detail.util.FlightAmenityIconUtil.getImageResource
import com.tokopedia.flight.search.data.cloud.single.Amenity

/**
 * Created by furqan on 06/10/21.
 */
class FlightDetailFacilityAmenityViewHolder(val binding: ItemFlightDetailFacilityAmenityBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bindData(amenity: Amenity) {
        with(binding) {
            if (amenity.isDefault) {
                imageAmenity.visibility = View.GONE
                textAmenity.visibility = View.GONE
            } else {
                imageAmenity.visibility = View.VISIBLE
                textAmenity.visibility = View.VISIBLE
                imageAmenity.setImageDrawable(
                    MethodChecker.getDrawable(
                        imageAmenity.context,
                        getImageResource(amenity.icon)
                    )
                )
                textAmenity.text = amenity.label
            }
        }
    }

}