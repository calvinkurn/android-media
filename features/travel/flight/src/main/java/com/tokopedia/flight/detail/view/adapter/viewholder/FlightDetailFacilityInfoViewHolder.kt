package com.tokopedia.flight.detail.view.adapter.viewholder

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.flight.R
import com.tokopedia.flight.detail.view.model.FlightDetailRouteInfoModel

/**
 * Created by furqan on 06/10/21.
 */
class FlightDetailFacilityInfoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val titleInfo: TextView = itemView.findViewById<View>(R.id.title_info) as TextView
    private val descInfo: TextView = itemView.findViewById<View>(R.id.desc_info) as TextView

    fun bindData(info: FlightDetailRouteInfoModel) {
        titleInfo.text = info.label
        descInfo.text = info.value
    }

}