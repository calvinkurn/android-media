package com.tokopedia.salam.umrah.homepage.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.loadImage
import com.tokopedia.salam.umrah.R
import com.tokopedia.salam.umrah.common.data.TravelAgent
import kotlinx.android.synthetic.main.item_umrah_home_page_partner_travel.view.*

/**
 * @author by firman on 20/01/19
 */

class UmrahHomepagePartnerTravelAdapter : RecyclerView.Adapter<UmrahHomepagePartnerTravelAdapter.UmrahHomepagePartnerTravelViewHolder>(){
    private var listCategories = emptyList<TravelAgent>()

    inner class UmrahHomepagePartnerTravelViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        fun bind(travel: TravelAgent) {
            with(itemView) {
                iv_umrah_partner_travel.loadImage(travel.imageUrl)
            }
        }
    }

    override fun getItemCount(): Int = listCategories.size
    override fun onBindViewHolder(holder: UmrahHomepagePartnerTravelViewHolder, position: Int) {
        holder.bind(listCategories[position])
    }

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): UmrahHomepagePartnerTravelViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_umrah_home_page_partner_travel, parent, false)
        return UmrahHomepagePartnerTravelViewHolder(itemView)
    }

    fun setList(list: List<TravelAgent>) {
        listCategories = list
        notifyDataSetChanged()
    }
}