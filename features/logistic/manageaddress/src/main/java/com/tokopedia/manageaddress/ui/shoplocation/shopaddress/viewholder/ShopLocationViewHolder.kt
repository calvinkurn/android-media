package com.tokopedia.manageaddress.ui.shoplocation.shopaddress.viewholder

import android.text.TextUtils
import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.manageaddress.R
import com.tokopedia.manageaddress.databinding.ItemShopLocationBinding
import com.tokopedia.manageaddress.domain.model.shoplocation.ShopLocationOldUiModel

class ShopLocationViewHolder(private val binding: ItemShopLocationBinding, private val listener: OnIconMoreClicked): AbstractViewHolder<ShopLocationOldUiModel>(binding.root) {
    override fun bind(element: ShopLocationOldUiModel) {
        binding.title.text = element.name
        binding.addressLine1.text = element.address
        binding.addressLine2.text = binding.root.context.getString(R.string.shop_address_line_2_placeholder,
                element.districtName,
                element.cityName, element.stateName, element.postalCode)
        if (TextUtils.isEmpty(element.phone)) {
            binding.phoneNumber.visibility = View.GONE
        } else {
            binding.phoneNumber.title = element.phone
            binding.phoneNumber.visibility = View.VISIBLE
        }
        if (TextUtils.isEmpty(element.email)) {
            binding.email.visibility = View.GONE
        } else {
            binding.email.title = element.email
            binding.email.visibility = View.VISIBLE
        }
        if (TextUtils.isEmpty(element.fax)) {
            binding.fax.visibility = View.GONE
        } else {
            binding.fax.title = element.fax
            binding.fax.visibility = View.VISIBLE
        }
        binding.menuMore.setOnClickListener { listener.onIconClicked(element, adapterPosition) }
    }

    companion object {
        val LAYOUT = R.layout.item_shop_location
    }

    interface OnIconMoreClicked {
        fun onIconClicked(item: ShopLocationOldUiModel, pos: Int)
    }
}