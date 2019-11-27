package com.tokopedia.salam.umrah.pdp.presentation.adapter

import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.View.INVISIBLE
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.loadImage
import com.tokopedia.kotlin.extensions.view.setMargin
import com.tokopedia.salam.umrah.R
import com.tokopedia.salam.umrah.common.util.UmrahDateUtil.getDate
import com.tokopedia.salam.umrah.common.util.UmrahDateUtil.getDay
import com.tokopedia.salam.umrah.pdp.data.UmrahPdpAirlineModel
import kotlinx.android.synthetic.main.item_umrah_pdp_airline.view.*

/**
 * @author by M on 30/10/19
 */
class UmrahPdpAirlineAdapter : RecyclerView.Adapter<UmrahPdpAirlineAdapter.UmrahPdpAirlineViewHolder>() {
    var airlines = emptyList<UmrahPdpAirlineModel>()
    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): UmrahPdpAirlineViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_umrah_pdp_airline,parent,false)
        return UmrahPdpAirlineViewHolder(view)
    }

    override fun getItemCount(): Int = airlines.size

    override fun onBindViewHolder(holder: UmrahPdpAirlineViewHolder, position: Int) {
        holder.bind(airlines[position])
    }

    inner class UmrahPdpAirlineViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        fun bind(umrahPdpAirlineModel: UmrahPdpAirlineModel) {
            with(itemView){
                tg_umrah_pdp_flight_plane_name.text = umrahPdpAirlineModel.name
                iv_umrah_pdp_flight_plane_logo.loadImage(umrahPdpAirlineModel.logoUrl)
                tg_umrah_pdp_flight_departure.text = umrahPdpAirlineModel.departureCity.name
                tg_umrah_pdp_flight_arrival.text = umrahPdpAirlineModel.arrivalCity.name
                tg_umrah_pdp_airline_facility.visibility = INVISIBLE
                tg_umrah_pdp_flight_header_title.text = resources.getString(R.string.umrah_pdp_airline_header,getDay("EEEE",umrahPdpAirlineModel.date),getDate("dd MMMM yyyy",umrahPdpAirlineModel.date))
            }
        }
    }
}