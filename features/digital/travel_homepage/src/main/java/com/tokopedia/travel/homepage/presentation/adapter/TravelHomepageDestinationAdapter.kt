package com.tokopedia.travel.homepage.presentation.adapter

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.kotlin.extensions.view.loadImage
import com.tokopedia.travel.homepage.R
import com.tokopedia.travel.homepage.data.TravelHomepageDestinationModel
import com.tokopedia.travel.homepage.presentation.listener.OnItemClickListener
import kotlinx.android.synthetic.main.travel_homepage_destination_section_list_item.view.*

/**
 * @author by jessica on 2019-08-09
 */

class TravelHomepageDestinationAdapter(private var list: List<TravelHomepageDestinationModel.Destination>,
                                       var listener: OnItemClickListener) :
        RecyclerView.Adapter<TravelHomepageDestinationAdapter.DestinationViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): DestinationViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(DestinationViewHolder.LAYOUT, parent, false)
        return DestinationViewHolder(view)
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: DestinationViewHolder, position: Int) {
        holder.bind(list[position], position, listener)
    }

    fun updateList(newList: List<TravelHomepageDestinationModel.Destination>) {
        this.list = newList
        notifyDataSetChanged()
    }

    class DestinationViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(destination: TravelHomepageDestinationModel.Destination, position: Int, listener: OnItemClickListener) {
            with(itemView) {
                image.loadImage(destination.attributes.imageUrl)
                title.text = destination.attributes.title
                subtitle.text = destination.attributes.subtitle
            }
            if (listener != null) itemView.setOnClickListener {
                listener.onTrackPopularDestinationClick(destination, position+1)
                listener.onItemClick(destination.attributes.appUrl)
            }
        }

        companion object {
            val LAYOUT = R.layout.travel_homepage_destination_section_list_item
        }
    }
}