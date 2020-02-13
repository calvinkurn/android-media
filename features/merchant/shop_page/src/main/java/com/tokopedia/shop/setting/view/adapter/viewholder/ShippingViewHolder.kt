package com.tokopedia.shop.setting.view.adapter.viewholder

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.shop.R
import com.tokopedia.shop.setting.view.adapter.ShopPageSettingAdapter

class ShippingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private var editLocationView: TextView? = null
    private var manageShippingServiceView: TextView? = null

    init {
        editLocationView = itemView.findViewById(R.id.tv_edit_location)
        manageShippingServiceView = itemView.findViewById(R.id.tv_manage_shipping_service)
    }

    fun bind(clickListener: ShopPageSettingAdapter.ShippingItemClickListener) {
        editLocationView?.setOnClickListener { clickListener.onEditLocationClicked() }
        manageShippingServiceView?.setOnClickListener { clickListener.onManageShippingServiceClicked() }
    }
}