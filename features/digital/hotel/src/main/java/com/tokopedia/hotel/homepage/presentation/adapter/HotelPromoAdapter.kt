package com.tokopedia.hotel.homepage.presentation.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.tokopedia.hotel.homepage.data.cloud.entity.HotelPromoEntity
import com.tokopedia.hotel.homepage.presentation.adapter.viewholder.HotelPromoViewHolder

/**
 * @author by furqan on 10/04/19
 */
class HotelPromoAdapter(private var viewModels: List<HotelPromoEntity>) : RecyclerView.Adapter<HotelPromoViewHolder>() {

    var promoClickListener: PromoClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HotelPromoViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(HotelPromoViewHolder.LAYOUT, parent, false)
        return HotelPromoViewHolder(view)
    }

    override fun getItemCount(): Int = viewModels.size

    override fun onBindViewHolder(holder: HotelPromoViewHolder, position: Int) {
        holder.bind(viewModels[position], promoClickListener)
    }

    interface PromoClickListener {
        fun onPromoClicked(promo: HotelPromoEntity)
    }

}