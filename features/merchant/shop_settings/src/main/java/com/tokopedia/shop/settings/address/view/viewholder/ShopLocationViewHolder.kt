package com.tokopedia.shop.settings.address.view.viewholder

import android.text.TextUtils
import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.shop.settings.address.data.ShopLocationViewModel
import com.tokopedia.shop.settings.R
import kotlinx.android.synthetic.main.item_shop_location.view.*

class ShopLocationViewHolder(val view: View, private val listener: OnIconMoreClicked): AbstractViewHolder<ShopLocationViewModel>(view) {
    override fun bind(element: ShopLocationViewModel) {
        itemView.title.text = element.name
        itemView.address_line_1.text = element.address
        itemView.address_line_2.text = itemView.context.getString(R.string.shop_address_line_2_placeholder,
                element.districtName,
                element.cityName, element.stateName, element.postalCode)
        if (TextUtils.isEmpty(element.phone)) {
            itemView.phone_number.visibility = View.GONE
        } else {
            itemView.phone_number.title = element.phone
            itemView.phone_number.visibility = View.VISIBLE
        }
        if (TextUtils.isEmpty(element.email)) {
            itemView.email.visibility = View.GONE
        } else {
            itemView.email.title = element.email
            itemView.email.visibility = View.VISIBLE
        }
        if (TextUtils.isEmpty(element.fax)) {
            itemView.fax.visibility = View.GONE
        } else {
            itemView.fax.title = element.fax
            itemView.fax.visibility = View.VISIBLE
        }
        itemView.menu_more.setOnClickListener { listener.onIconClicked(element, adapterPosition) }
    }

    companion object {
        val LAYOUT = R.layout.item_shop_location
    }

    interface OnIconMoreClicked {
        fun onIconClicked(item: ShopLocationViewModel, pos: Int)
    }
}