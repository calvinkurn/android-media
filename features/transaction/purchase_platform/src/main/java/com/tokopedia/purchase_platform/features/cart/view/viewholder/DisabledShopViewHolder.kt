package com.tokopedia.purchase_platform.features.cart.view.viewholder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.purchase_platform.R
import com.tokopedia.purchase_platform.features.cart.view.ActionListener
import com.tokopedia.purchase_platform.features.cart.view.viewmodel.DisabledShopHolderData
import kotlinx.android.synthetic.main.item_cart_disabled_shop.view.*

/**
 * Created by Irfan Khoirul on 2019-10-16.
 */

class DisabledShopViewHolder(itemView: View, val actionListener: ActionListener) : RecyclerView.ViewHolder(itemView) {

    companion object {
        val LAYOUT = R.layout.item_cart_disabled_shop
    }

    fun bind(data: DisabledShopHolderData) {
        itemView.text_shop_name.text = data.shopName
        itemView.text_shop_location.text = data.shopLocation
        itemView.label_error.apply {
            text = data.errorLabel
            if (data.errorLabel.isNotBlank()) {
                visibility = View.VISIBLE
            } else {
                visibility = View.GONE
            }
        }
    }

}