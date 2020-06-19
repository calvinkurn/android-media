package com.tokopedia.digital.home.presentation.adapter.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.digital.home.R
import com.tokopedia.digital.home.model.RechargeHomepageSections
import com.tokopedia.digital.home.presentation.Util.DigitalHomepageTrackingActionConstant.DYNAMIC_ICON_CLICK
import com.tokopedia.digital.home.presentation.listener.OnItemBindListener
import com.tokopedia.kotlin.extensions.view.loadImage
import kotlinx.android.synthetic.main.layout_digital_home_category_item_submenu.view.*

class RechargeItemCategoryAdapter(val items: List<RechargeHomepageSections.Item>, val listener: OnItemBindListener)
    : RecyclerView.Adapter<RechargeItemCategoryAdapter.DigitalItemSubmenuCategoryViewHolder>() {

    override fun onBindViewHolder(viewHolder: DigitalItemSubmenuCategoryViewHolder, position: Int) {
        viewHolder.bind(items[position], listener)
    }

    override fun onCreateViewHolder(parent: ViewGroup, position: Int): DigitalItemSubmenuCategoryViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.layout_digital_home_category_item_submenu, parent, false)
        return DigitalItemSubmenuCategoryViewHolder(view)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    class DigitalItemSubmenuCategoryViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
        fun bind(element: RechargeHomepageSections.Item, onItemBindListener: OnItemBindListener) {
            itemView.category_image.loadImage(element.mediaUrl)
            itemView.category_name.text = element.title
            itemView.setOnClickListener {
                onItemBindListener.onRechargeSectionItemClicked(element, adapterPosition, DYNAMIC_ICON_CLICK)
            }
        }

    }
}
