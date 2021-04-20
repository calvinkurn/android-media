package com.tokopedia.digital.home.presentation.adapter

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.digital.home.R
import com.tokopedia.digital.home.model.RechargeHomepageSections
import com.tokopedia.digital.home.presentation.listener.RechargeHomepageItemListener
import com.tokopedia.kotlin.extensions.view.loadImage
import kotlinx.android.synthetic.main.view_recharge_home_carousell_image.view.*


class RechargeItemCarousellAdapter(val items: List<RechargeHomepageSections.Item>, val listener: RechargeHomepageItemListener)
    : RecyclerView.Adapter<RechargeItemCarousellAdapter.CarousellRechargeItemViewHolder>() {

    override fun onBindViewHolder(viewHolder: CarousellRechargeItemViewHolder, position: Int) {
        viewHolder.bind(items[position], listener)
    }

    override fun onCreateViewHolder(parent: ViewGroup, position: Int): CarousellRechargeItemViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.view_recharge_home_carousell_image, parent, false)
        return CarousellRechargeItemViewHolder(view)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    class CarousellRechargeItemViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
        fun bind(element: RechargeHomepageSections.Item, onItemBindListener: RechargeHomepageItemListener) {
            itemView.img_carousell_recharge_home_page.loadImage(element.mediaUrl)
            itemView.setOnClickListener {
                if(element.applink.isNotEmpty()){
                    try {
                        val uri = Uri.parse(element.applink)
                        if (uri.pathSegments.size >= 1){
                            onItemBindListener.onRechargeSectionItemClicked(element)
                        }
                    } catch (e: Exception){

                    }
                }
            }
        }

    }
}