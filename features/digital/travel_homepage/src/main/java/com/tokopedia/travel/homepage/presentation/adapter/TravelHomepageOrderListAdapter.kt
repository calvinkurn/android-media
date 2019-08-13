package com.tokopedia.travel.homepage.presentation.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.kotlin.extensions.view.loadImage
import com.tokopedia.travel.homepage.R
import com.tokopedia.travel.homepage.data.TravelHomepageOrderListModel
import kotlinx.android.synthetic.main.travel_homepage_travel_section_list_item.view.*

/**
 * @author by jessica on 2019-08-09
 */

class TravelHomepageOrderListAdapter(private var list: List<TravelHomepageOrderListModel.Order>,
                                     var listener: OrderViewHolder.OnItemClickListener?):
        RecyclerView.Adapter<TravelHomepageOrderListAdapter.OrderViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, position: Int): OrderViewHolder {
        val view = if (list.get(position).subtitle.isNotEmpty()) LayoutInflater.from(parent.context).inflate(OrderViewHolder.LAYOUT, parent, false)
        else LayoutInflater.from(parent.context).inflate(OrderViewHolder.LAYOUT_WITHOUT_SUBTITLE, parent, false)
        return OrderViewHolder(view)
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
        holder.bind(list.get(position), position, listener)
    }


    class OrderViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

        fun bind(order: TravelHomepageOrderListModel.Order, position: Int, listener: OnItemClickListener?) {
            with(itemView) {
                image.loadImage(order.imageUrl)
                title.text = order.title
                if (order.subtitle.isNotEmpty()) subtitle.text = order.subtitle
                prefix.text = order.value
            }
            if (listener != null) itemView.setOnClickListener { listener.onItemClick(order, position) }
        }

        companion object {
            val LAYOUT = R.layout.travel_homepage_order_section_list_item
            val LAYOUT_WITHOUT_SUBTITLE = R.layout.travel_homepage_order_section_list_without_subtitle_item
        }

        interface OnItemClickListener {
            fun onItemClick(category: TravelHomepageOrderListModel.Order, position: Int)
        }
    }
}