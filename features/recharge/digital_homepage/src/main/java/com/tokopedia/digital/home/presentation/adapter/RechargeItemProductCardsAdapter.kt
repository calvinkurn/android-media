package com.tokopedia.digital.home.presentation.adapter

import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.digital.home.R
import com.tokopedia.digital.home.model.RechargeHomepageSections
import com.tokopedia.digital.home.presentation.listener.RechargeHomepageItemListener
import com.tokopedia.kotlin.extensions.view.loadImage
import kotlinx.android.synthetic.main.view_recharge_home_product_cards_item.view.*

class RechargeItemProductCardsAdapter(val items: List<RechargeHomepageSections.Item>, val onItemBindListener: RechargeHomepageItemListener)
    : RecyclerView.Adapter<RechargeItemProductCardsAdapter.RechargeItemProductCardViewHolder>() {

    override fun onBindViewHolder(viewHolder: RechargeItemProductCardViewHolder, position: Int) {
        viewHolder.bind(items[position], onItemBindListener)
    }

    override fun onCreateViewHolder(parent: ViewGroup, position: Int): RechargeItemProductCardViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.view_recharge_home_product_cards_item, parent, false)
        return RechargeItemProductCardViewHolder(view)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    class RechargeItemProductCardViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
        fun bind(element: RechargeHomepageSections.Item, onItemBindListener: RechargeHomepageItemListener) {
            itemView.iv_recharge_home_product_cards_item.loadImage(element.mediaUrl)
            itemView.tv_recharge_home_product_cards_item_category.text = element.title
            itemView.tv_recharge_home_product_cards_item_title.text = element.subtitle
            itemView.tv_recharge_home_product_cards_item_price_info.text = Html.fromHtml(element.label1)
            itemView.tv_recharge_home_product_cards_item_price.text = element.label2
            itemView.setOnClickListener {
                onItemBindListener.onRechargeSectionItemClicked(element)
            }
        }

    }
}
