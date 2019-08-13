package com.tokopedia.travel.homepage.presentation.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.kotlin.extensions.view.loadImage
import com.tokopedia.travel.homepage.R
import com.tokopedia.travel.homepage.data.TravelHomepageDestinationModel
import kotlinx.android.synthetic.main.travel_homepage_travel_section_list_item.view.*

/**
 * @author by jessica on 2019-08-09
 */

class TravelHomepageDestinationAdapter(private var list: List<TravelHomepageDestinationModel.Destination>,
                                       var listener: DestinationViewHolder.OnItemClickListener?):
        RecyclerView.Adapter<TravelHomepageDestinationAdapter.DestinationViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): DestinationViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(DestinationViewHolder.LAYOUT, parent, false)
        return DestinationViewHolder(view)
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: DestinationViewHolder, position: Int) {
        holder.bind(list.get(position).attributes, position, listener)
    }


    class DestinationViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

        fun bind(destination: TravelHomepageDestinationModel.Attribute, position: Int, listener: OnItemClickListener?) {
            with(itemView) {
                image.loadImage(destination.imageUrl)
                title.text = destination.title
                subtitle.text = destination.subtitle
            }
            if (listener != null) itemView.setOnClickListener { listener.onItemClick(destination, position) }
        }

        companion object {
            val LAYOUT = R.layout.travel_homepage_destination_section_list_item
        }

        interface OnItemClickListener {
            fun onItemClick(category: TravelHomepageDestinationModel.Attribute, position: Int)
        }
    }
}