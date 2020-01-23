package com.tokopedia.salam.umrah.travel.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.loadImage
import com.tokopedia.salam.umrah.R
import com.tokopedia.salam.umrah.common.data.UmrahProductModel
import com.tokopedia.salam.umrah.common.util.CurrencyFormatter
import com.tokopedia.salam.umrah.common.util.UmrahPriceUtil
import kotlinx.android.synthetic.main.item_umrah_search.view.*

class UmrahTravelAgentProductsAdapter : RecyclerView.Adapter<UmrahTravelAgentProductsAdapter.UmrahTravelAgentProductsViewHolder>() {
    private var listProducts = emptyList<UmrahProductModel.UmrahProduct>()

    inner class UmrahTravelAgentProductsViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        fun bind(element: UmrahProductModel.UmrahProduct) {
            with(itemView) {
                iv_umrah_image.loadImage(element.banners.first())
                label_umrah_duration.text = resources.getString(R.string.umrah_search_duration_days, element.durationDays.toString())
                tg_umrah_title.text = element.title
                tg_umrah_price.text = CurrencyFormatter.getRupiahFormat(element.originalPrice)
                tg_umrah_calendar.text = element.ui.travelDates
                tg_umrah_hotel.text = element.ui.hotelStars
                tg_umrah_plane.text = element.airlines.first().name
                tg_umrah_start_from_label.text = UmrahPriceUtil.getSlashedPrice(resources, element.slashPrice)
            }
        }
    }

    override fun getItemCount(): Int = listProducts.size
    override fun onBindViewHolder(holder: UmrahTravelAgentProductsViewHolder, position: Int) {
        holder.bind(listProducts[position])
    }

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): UmrahTravelAgentProductsViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_umrah_search, parent, false)
        return UmrahTravelAgentProductsViewHolder(itemView)
    }

    fun setList(list: List<UmrahProductModel.UmrahProduct>) {
        listProducts = list
        notifyDataSetChanged()
    }

}