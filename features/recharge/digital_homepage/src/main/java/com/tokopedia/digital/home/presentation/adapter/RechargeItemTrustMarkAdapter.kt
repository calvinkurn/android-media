package com.tokopedia.digital.home.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.digital.home.R
import com.tokopedia.digital.home.model.RechargeHomepageSections
import com.tokopedia.kotlin.extensions.view.loadImage
import kotlinx.android.synthetic.main.layout_digital_home_trustmark_item.view.*

class RechargeItemTrustMarkAdapter(val items: List<RechargeHomepageSections.Item>)
    : RecyclerView.Adapter<RechargeItemTrustMarkAdapter.RechargeItemTrustMarkViewHolder>() {

    override fun onBindViewHolder(viewHolder: RechargeItemTrustMarkViewHolder, position: Int) {
        viewHolder.bind(items[position])
    }

    override fun onCreateViewHolder(parent: ViewGroup, position: Int): RechargeItemTrustMarkViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.layout_digital_home_trustmark_item, parent, false)
        return RechargeItemTrustMarkViewHolder(view)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    class RechargeItemTrustMarkViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
        fun bind(element: RechargeHomepageSections.Item) {
            itemView.trustmark_image.loadImage(element.mediaUrl)
            itemView.trustmark_name.text = element.title
        }

    }
}
