package com.tokopedia.shop.newinfo.view.adapter.viewholder

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.shop.R
import com.tokopedia.shop.newinfo.view.adapter.ShopNewInfoAdapter

class SupportViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private var supportView: View? = null
    private var sellerView: View? = null

    init {
        supportView = itemView.findViewById(R.id.support_layout)
        sellerView = itemView.findViewById(R.id.seller_layout)
    }

    fun bind(clickListener: ShopNewInfoAdapter.SupportItemClickListener) {
        supportView?.setOnClickListener { clickListener.onGetSupportClicked() }
        sellerView?.setOnClickListener { clickListener.onGetTipsClicked() }
    }
}