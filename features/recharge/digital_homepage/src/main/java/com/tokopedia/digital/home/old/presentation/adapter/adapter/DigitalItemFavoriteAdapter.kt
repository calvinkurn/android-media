package com.tokopedia.digital.home.old.presentation.adapter.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.digital.home.R
import com.tokopedia.digital.home.old.model.DigitalHomePageSectionModel
import com.tokopedia.digital.home.old.presentation.util.DigitalHomepageTrackingActionConstant.BEHAVIORAL_CATEGORY_CLICK
import com.tokopedia.digital.home.old.presentation.listener.OnItemBindListener
import com.tokopedia.kotlin.extensions.view.loadImage
import kotlinx.android.synthetic.main.layout_digital_home_category_item_submenu_frame.view.*
import kotlinx.android.synthetic.main.view_recharge_home_card_image.view.*

class DigitalItemFavoriteAdapter(val items: List<DigitalHomePageSectionModel.Item>, val onItemBindListener: OnItemBindListener)
    : RecyclerView.Adapter<DigitalItemFavoriteAdapter.DigitalItemFavoriteViewHolder>() {

    override fun onBindViewHolder(viewHolder: DigitalItemFavoriteViewHolder, position: Int) {
        viewHolder.bind(items[position], onItemBindListener)
    }

    override fun onCreateViewHolder(parent: ViewGroup, position: Int): DigitalItemFavoriteViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.layout_digital_home_category_item_submenu_frame, parent, false)
        return DigitalItemFavoriteViewHolder(view)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    class DigitalItemFavoriteViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
        fun bind(element: DigitalHomePageSectionModel.Item, onItemBindListener: OnItemBindListener) {
            itemView.category_frame_image.card_image.loadImage(element.mediaUrl)
            itemView.category_frame_name.text = element.title
            itemView.setOnClickListener {
                onItemBindListener.onSectionItemClicked(element, adapterPosition, BEHAVIORAL_CATEGORY_CLICK)
            }
        }

    }
}
