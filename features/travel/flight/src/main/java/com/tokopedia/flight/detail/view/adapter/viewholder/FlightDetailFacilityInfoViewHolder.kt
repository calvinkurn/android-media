package com.tokopedia.flight.detail.view.adapter.viewholder

import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.flight.databinding.ItemFlightDetailFacilityInfoBinding
import com.tokopedia.flight.detail.view.model.FlightDetailRouteInfoModel

/**
 * Created by furqan on 06/10/21.
 */
class FlightDetailFacilityInfoViewHolder(val binding: ItemFlightDetailFacilityInfoBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bindData(info: FlightDetailRouteInfoModel) {
        with(binding) {
            titleInfo.text = info.label
            descInfo.text = info.value
        }
    }
}