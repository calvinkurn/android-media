package com.tokopedia.salam.umrah.pdp.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.salam.umrah.R
import kotlinx.android.synthetic.main.item_umrah_pdp_facility.view.*

/**
 * @author by M on 31/10/19
 */
class UmrahPdpFacilityAdapter : RecyclerView.Adapter<UmrahPdpFacilityAdapter.UmrahPdpFacilityViewHolder>() {
    var facilities = emptyList<String>()
    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): UmrahPdpFacilityViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_umrah_pdp_facility,parent,false)
        return UmrahPdpFacilityViewHolder(view)
    }

    override fun getItemCount(): Int = facilities.size

    override fun onBindViewHolder(holder: UmrahPdpFacilityViewHolder, position: Int) {
        holder.bind(facilities[position])
    }

    inner class UmrahPdpFacilityViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(facility: String) {
            with(itemView){
                item_umrah_pdp_facility_text.text = facility
            }
        }
    }
}