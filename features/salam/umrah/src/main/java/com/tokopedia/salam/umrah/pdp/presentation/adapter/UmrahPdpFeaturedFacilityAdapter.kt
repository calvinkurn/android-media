package com.tokopedia.salam.umrah.pdp.presentation.adapter

import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.kotlin.extensions.view.loadImageDrawable
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.loadImage
import com.tokopedia.kotlin.extensions.view.setMargin
import com.tokopedia.salam.umrah.R
import com.tokopedia.salam.umrah.pdp.data.UmrahPdpFeaturedFacilityModel
import kotlinx.android.synthetic.main.item_umrah_pdp_featured_facility.view.*
/**
 * @author by M on 30/10/19
 */
class UmrahPdpFeaturedFacilityAdapter : RecyclerView.Adapter<UmrahPdpFeaturedFacilityAdapter.UmrahPdpFeaturedFacilityViewHolder>() {
    var featuredFacilities = emptyList<UmrahPdpFeaturedFacilityModel>()
    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): UmrahPdpFeaturedFacilityViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_umrah_pdp_featured_facility,parent,false)
        return UmrahPdpFeaturedFacilityViewHolder(view)
    }

    override fun getItemCount(): Int = featuredFacilities.size

    override fun onBindViewHolder(holder: UmrahPdpFeaturedFacilityViewHolder, position: Int) {
        holder.bind(featuredFacilities[position])
    }

    inner class UmrahPdpFeaturedFacilityViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(umrahPdpFeaturedFacilityModel: UmrahPdpFeaturedFacilityModel) {
            with(itemView){
                tg_umrah_pdp_facility_header.text = umrahPdpFeaturedFacilityModel.header
                iv_umrah_pdp_facility_logo.loadImageDrawable(umrahPdpFeaturedFacilityModel.iconDrawable)
//                iv_umrah_pdp_facility_logo.loadImage(umrahPdpFeaturedFacilityModel.iconUrl)
            }
        }
    }
}