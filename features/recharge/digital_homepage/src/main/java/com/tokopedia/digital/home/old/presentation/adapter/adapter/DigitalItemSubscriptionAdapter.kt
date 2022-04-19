package com.tokopedia.digital.home.old.presentation.adapter.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.digital.home.databinding.LayoutDigitalHomeSubscriptionItemBinding
import com.tokopedia.digital.home.old.model.DigitalHomePageSectionModel
import com.tokopedia.digital.home.old.presentation.util.DigitalHomepageTrackingActionConstant.SUBSCRIPTION_GUIDE_CLICK
import com.tokopedia.digital.home.old.presentation.listener.OnItemBindListener

class DigitalItemSubscriptionAdapter(val items: List<DigitalHomePageSectionModel.Item>, val onItemBindListener: OnItemBindListener)
    : RecyclerView.Adapter<DigitalItemSubscriptionAdapter.DigitalItemSubscriptionViewHolder>() {

    override fun onBindViewHolder(viewHolder: DigitalItemSubscriptionViewHolder, position: Int) {
        viewHolder.bind(items[position], onItemBindListener)
    }

    override fun onCreateViewHolder(parent: ViewGroup, position: Int): DigitalItemSubscriptionViewHolder {
        val view = LayoutDigitalHomeSubscriptionItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DigitalItemSubscriptionViewHolder(view)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    class DigitalItemSubscriptionViewHolder(val binding:  LayoutDigitalHomeSubscriptionItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(element: DigitalHomePageSectionModel.Item, onItemBindListener: OnItemBindListener) {
            binding.subscriptionName.text = element.title
            itemView.setOnClickListener {
                onItemBindListener.onSectionItemClicked(element, adapterPosition, SUBSCRIPTION_GUIDE_CLICK)
            }
        }

    }
}
