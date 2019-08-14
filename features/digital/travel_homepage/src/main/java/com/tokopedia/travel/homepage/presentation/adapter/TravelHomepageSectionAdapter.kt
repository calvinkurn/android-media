package com.tokopedia.travel.homepage.presentation.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.kotlin.extensions.view.loadImage
import com.tokopedia.travel.homepage.R
import com.tokopedia.travel.homepage.data.TravelHomepageSectionViewModel
import com.tokopedia.travel.homepage.presentation.listener.OnItemClickListener
import kotlinx.android.synthetic.main.travel_homepage_travel_section_list_item.view.*

/**
 * @author by jessica on 2019-08-09
 */

class TravelHomepageSectionAdapter(private var list: List<TravelHomepageSectionViewModel.Item>,
                                   private var type: Int,
                                   var listener: OnItemClickListener):
        RecyclerView.Adapter<TravelHomepageSectionAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, position: Int): ViewHolder {
        val item = list.get(position)
        lateinit var view: View
        if (type == TravelHomepageSectionViewModel.TYPE_ORDER_LIST) {
            view = if (item.subtitle.isNotBlank())
                LayoutInflater.from(parent.context).inflate(ViewHolder.ORDER_LAYOUT, parent, false)
            else LayoutInflater.from(parent.context).inflate(ViewHolder.ORDER_LAYOUT_WITHOUT_SUBTITLE, parent, false)
        } else {
            view = if (item.subtitle.isNotBlank())
                LayoutInflater.from(parent.context).inflate(ViewHolder.LAYOUT, parent, false)
            else LayoutInflater.from(parent.context).inflate(ViewHolder.LAYOUT_WITHOUT_SUBTITLE, parent, false)
        }
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(list.get(position), position, listener)
    }


    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

        fun bind(item: TravelHomepageSectionViewModel.Item, position: Int, listener: OnItemClickListener) {
            with(itemView) {
                image.loadImage(item.imageUrl)
                title.text = item.title
                if (item.subtitle.isNotBlank()) subtitle.text = item.subtitle
                prefix.text = item.value
            }
            if (listener != null) itemView.setOnClickListener {
                listener.onItemClick(item.appUrl)
            }
        }

        companion object {
            val LAYOUT = R.layout.travel_homepage_section_list_item
            val LAYOUT_WITHOUT_SUBTITLE = R.layout.travel_homepage_section_list_item_without_subtitle
            val ORDER_LAYOUT = R.layout.travel_homepage_order_section_list_item
            val ORDER_LAYOUT_WITHOUT_SUBTITLE = R.layout.travel_homepage_order_section_list_without_subtitle_item
        }
    }
}