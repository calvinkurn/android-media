package com.tokopedia.digital.home.old.presentation.adapter.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.digital.home.R
import com.tokopedia.digital.home.old.model.DigitalHomePageSectionModel
import com.tokopedia.digital.home.old.presentation.util.DigitalHomepageTrackingActionConstant.SPOTLIGHT_BANNER_CLICK
import com.tokopedia.digital.home.old.presentation.listener.OnItemBindListener
import com.tokopedia.kotlin.extensions.view.loadImage
import kotlinx.android.synthetic.main.layout_digital_home_spotlight_item.view.*

class DigitalItemSpotlightAdapter(val items: List<DigitalHomePageSectionModel.Item>, val onItemBindListener: OnItemBindListener)
    : RecyclerView.Adapter<DigitalItemSpotlightAdapter.DigitalItemSpotlightViewHolder>() {

    override fun onBindViewHolder(viewHolder: DigitalItemSpotlightViewHolder, position: Int) {
        viewHolder.bind(items[position], onItemBindListener)
    }

    override fun onCreateViewHolder(parent: ViewGroup, position: Int): DigitalItemSpotlightViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.layout_digital_home_spotlight_item, parent, false)
        return DigitalItemSpotlightViewHolder(view)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    class DigitalItemSpotlightViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
        fun bind(element: DigitalHomePageSectionModel.Item, onItemBindListener: OnItemBindListener) {
            itemView.spotlight_image.loadImage(element.mediaUrl)
            itemView.spotlight_name.text = element.title
            itemView.setOnClickListener {
                onItemBindListener.onSectionItemClicked(element, adapterPosition, SPOTLIGHT_BANNER_CLICK)
            }
        }

    }
}
