package com.tokopedia.salam.umrah.pdp.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.salam.umrah.R
import kotlinx.android.synthetic.main.item_umrah_pdp_non_facility.view.*
/**
 * @author by M on 30/10/19
 */
class UmrahPdpNonFacilityAdapter : RecyclerView.Adapter<UmrahPdpNonFacilityAdapter.UmrahPdpNonFacilityViewHolder>() {
    var nonFacilities = emptyList<String>()
    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): UmrahPdpNonFacilityViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_umrah_pdp_non_facility,parent,false)
        return UmrahPdpNonFacilityViewHolder(view)
    }

    override fun getItemCount(): Int = nonFacilities.size

    override fun onBindViewHolder(holder: UmrahPdpNonFacilityViewHolder, position: Int) {
        holder.bind(nonFacilities[position])
    }

    inner class UmrahPdpNonFacilityViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(nonFacility: String) {
            with(itemView){
                item_umrah_pdp_non_facilities_title.text = nonFacility
            }
        }
    }
}