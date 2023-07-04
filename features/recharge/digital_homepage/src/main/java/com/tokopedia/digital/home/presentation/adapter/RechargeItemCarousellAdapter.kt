package com.tokopedia.digital.home.presentation.adapter

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.digital.home.databinding.ViewRechargeHomeCarousellImageBinding
import com.tokopedia.digital.home.model.RechargeHomepageSections
import com.tokopedia.digital.home.presentation.listener.RechargeHomepageItemListener
import com.tokopedia.media.loader.loadImage

class RechargeItemCarousellAdapter(val items: List<RechargeHomepageSections.Item>, val listener: RechargeHomepageItemListener) :
    RecyclerView.Adapter<RechargeItemCarousellAdapter.CarousellRechargeItemViewHolder>() {

    override fun onBindViewHolder(viewHolder: CarousellRechargeItemViewHolder, position: Int) {
        viewHolder.bind(items[position], listener)
    }

    override fun onCreateViewHolder(parent: ViewGroup, position: Int): CarousellRechargeItemViewHolder {
        val view = ViewRechargeHomeCarousellImageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CarousellRechargeItemViewHolder(view)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    class CarousellRechargeItemViewHolder(val binding: ViewRechargeHomeCarousellImageBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(element: RechargeHomepageSections.Item, onItemBindListener: RechargeHomepageItemListener) {
            with(binding) {
                imgCarousellRechargeHomePage.loadImage(element.mediaUrl)
                root.setOnClickListener {
                    if (element.applink.isNotEmpty()) {
                        try {
                            val uri = Uri.parse(element.applink)
                            if (!uri.host.isNullOrEmpty()) {
                                onItemBindListener.onRechargeSectionItemClicked(element)
                            }
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }
                }
            }
        }
    }
}
