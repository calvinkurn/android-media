package com.tokopedia.shop.setting.view.adapter.viewholder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.shop.R
import com.tokopedia.shop.setting.view.adapter.ShopPageSettingAdapter

class SupportViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private var supportView: View? = null
    private var sellerView: View? = null

    init {
        supportView = itemView.findViewById(R.id.support_layout)
        sellerView = itemView.findViewById(R.id.seller_layout)
    }

    fun bind(clickListener: ShopPageSettingAdapter.SupportItemClickListener) {
        supportView?.setOnClickListener { clickListener.onGetSupportClicked() }
        sellerView?.setOnClickListener { clickListener.onGetTipsClicked() }
    }
}