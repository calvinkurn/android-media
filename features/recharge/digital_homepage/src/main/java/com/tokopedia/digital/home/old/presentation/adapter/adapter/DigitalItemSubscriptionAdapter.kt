package com.tokopedia.digital.home.old.presentation.adapter.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.digital.home.R
import com.tokopedia.digital.home.old.model.DigitalHomePageSectionModel
import com.tokopedia.digital.home.old.presentation.util.DigitalHomepageTrackingActionConstant.SUBSCRIPTION_GUIDE_CLICK
import com.tokopedia.digital.home.old.presentation.listener.OnItemBindListener
import kotlinx.android.synthetic.main.layout_digital_home_subscription_item.view.*

class DigitalItemSubscriptionAdapter(val items: List<DigitalHomePageSectionModel.Item>, val onItemBindListener: OnItemBindListener)
    : RecyclerView.Adapter<DigitalItemSubscriptionAdapter.DigitalItemSubscriptionViewHolder>() {

    override fun onBindViewHolder(viewHolder: DigitalItemSubscriptionViewHolder, position: Int) {
        viewHolder.bind(items[position], onItemBindListener)
    }

    override fun onCreateViewHolder(parent: ViewGroup, position: Int): DigitalItemSubscriptionViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.layout_digital_home_subscription_item, parent, false)
        return DigitalItemSubscriptionViewHolder(view)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    class DigitalItemSubscriptionViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
        fun bind(element: DigitalHomePageSectionModel.Item, onItemBindListener: OnItemBindListener) {
            itemView.subscription_name.text = element.title
            itemView.setOnClickListener {
                onItemBindListener.onSectionItemClicked(element, adapterPosition, SUBSCRIPTION_GUIDE_CLICK)
            }
        }

    }
}
