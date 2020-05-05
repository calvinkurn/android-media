package com.tokopedia.salam.umrah.homepage.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.kotlin.extensions.view.loadImage
import com.tokopedia.salam.umrah.R
import com.tokopedia.salam.umrah.common.data.TravelAgent
import com.tokopedia.salam.umrah.homepage.presentation.listener.onItemBindListener
import kotlinx.android.synthetic.main.item_umrah_home_page_partner_travel.view.*

/**
 * @author by firman on 20/01/19
 */

class UmrahHomepagePartnerTravelAdapter(val onBindItemBindListener: onItemBindListener) : RecyclerView.Adapter<UmrahHomepagePartnerTravelAdapter.UmrahHomepagePartnerTravelViewHolder>(){
    private var listCategories = emptyList<TravelAgent>()

    inner class UmrahHomepagePartnerTravelViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        fun bind(travel: TravelAgent) {
            with(itemView) {
                iv_umrah_partner_travel.loadImage(travel.imageUrl)
                setOnClickListener {
                    onBindItemBindListener.onClickPartnerTravel(resources.getString(R.string.umrah_home_page_partner_label), travel)
                    RouteManager.route(context, ApplinkConst.SALAM_UMRAH_AGEN, travel.slugName)
                }
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