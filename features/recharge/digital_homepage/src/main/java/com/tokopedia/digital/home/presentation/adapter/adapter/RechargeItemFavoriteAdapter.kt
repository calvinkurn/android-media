package com.tokopedia.digital.home.presentation.adapter.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.digital.home.R
import com.tokopedia.digital.home.model.RechargeHomepageSections
import com.tokopedia.digital.home.presentation.Util.DigitalHomepageTrackingActionConstant.BEHAVIORAL_CATEGORY_CLICK
import com.tokopedia.digital.home.presentation.listener.OnItemBindListener
import com.tokopedia.kotlin.extensions.view.loadImage
import kotlinx.android.synthetic.main.layout_digital_home_category_item_submenu_frame.view.*
import kotlinx.android.synthetic.main.view_recharge_home_card_image.view.*

class RechargeItemFavoriteAdapter(val items: List<RechargeHomepageSections.Item>, val listener: OnItemBindListener)
    : RecyclerView.Adapter<RechargeItemFavoriteAdapter.DigitalItemFavoriteViewHolder>() {

    override fun onBindViewHolder(viewHolder: DigitalItemFavoriteViewHolder, position: Int) {
        viewHolder.bind(items[position], listener)
    }

    override fun onCreateViewHolder(parent: ViewGroup, position: Int): DigitalItemFavoriteViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.layout_digital_home_category_item_submenu_frame, parent, false)
        return DigitalItemFavoriteViewHolder(view)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    class DigitalItemFavoriteViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
        fun bind(element: RechargeHomepageSections.Item, onItemBindListener: OnItemBindListener) {
            itemView.category_frame_image.card_image.loadImage(element.mediaUrl)
            itemView.category_frame_name.text = element.title
            itemView.setOnClickListener {
                onItemBindListener.onRechargeSectionItemClicked(element)
            }
        }

    }
}
