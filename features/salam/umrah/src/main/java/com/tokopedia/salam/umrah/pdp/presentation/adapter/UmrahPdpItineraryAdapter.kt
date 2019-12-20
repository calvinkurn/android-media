package com.tokopedia.salam.umrah.pdp.presentation.adapter

import android.view.View
import android.view.View.GONE
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.inflateLayout
import com.tokopedia.salam.umrah.R
import com.tokopedia.salam.umrah.pdp.data.UmrahPdpItineraryModel
import kotlinx.android.synthetic.main.item_umrah_pdp_itinerary.view.*

/**
 * @author by M on 22/10/2019
 */
class UmrahPdpItineraryAdapter : RecyclerView.Adapter<UmrahPdpItineraryAdapter.UmrahPdpItineraryViewHolder>() {
    private val _item: MutableList<UmrahPdpItineraryModel> = mutableListOf()
    private var isLast = false

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): UmrahPdpItineraryViewHolder {
        return UmrahPdpItineraryViewHolder(parent.inflateLayout(R.layout.item_umrah_pdp_itinerary))
    }

    fun updateItems(items: List<UmrahPdpItineraryModel>) {
        _item.clear()
        _item.addAll(items)
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = _item.size

    override fun onBindViewHolder(holder: UmrahPdpItineraryViewHolder, position: Int) {
        isLast = position == _item.size - 1
        holder.bindItem(_item[position])
    }

    inner class UmrahPdpItineraryViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bindItem(item: UmrahPdpItineraryModel) {
            with(itemView) {
                tg_umrah_pdp_itinerary_title.text = resources.getString(R.string.umrah_pdp_itinerary_day, item.day)
                tg_umrah_pdp_itinerary_desc.text = item.desc
                if (isLast) iv_umrah_pdp_itinerary_line.visibility = GONE
            }
        }
    }
}