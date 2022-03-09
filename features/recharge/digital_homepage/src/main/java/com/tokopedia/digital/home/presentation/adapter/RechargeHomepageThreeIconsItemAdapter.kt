package com.tokopedia.digital.home.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.digital.home.databinding.LayoutDigitalItemThreeIconsBinding
import com.tokopedia.digital.home.model.RechargeHomepageSections
import com.tokopedia.digital.home.presentation.listener.RechargeHomepageItemListener
import com.tokopedia.home_component.util.loadImage

class RechargeHomepageThreeIconsItemAdapter(
    val items: List<RechargeHomepageSections.Item>,
    val listener: RechargeHomepageItemListener
): RecyclerView.Adapter<RechargeHomepageThreeIconsItemAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutDigitalItemThreeIconsBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size

    inner class ViewHolder(
        val binding: LayoutDigitalItemThreeIconsBinding
    ): RecyclerView.ViewHolder(binding.root) {
        fun bind(element: RechargeHomepageSections.Item){
            with(binding){
                tvTitle.text = element.title
                ivIcon.loadImage(element.mediaUrl)
            }
        }
    }
}