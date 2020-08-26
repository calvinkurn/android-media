package com.tokopedia.cart.view.viewholder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.cart.R
import com.tokopedia.cart.view.ActionListener
import com.tokopedia.cart.view.uimodel.DisabledShopHolderData
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.show
import kotlinx.android.synthetic.main.item_cart_disabled_shop.view.*

/**
 * Created by Irfan Khoirul on 2019-10-16.
 */

class DisabledShopViewHolder(itemView: View, val actionListener: ActionListener?) : RecyclerView.ViewHolder(itemView) {

    companion object {
        val LAYOUT = R.layout.item_cart_disabled_shop
    }

    fun bind(data: DisabledShopHolderData) {
        itemView.text_shop_name.text = data.shopName
        itemView.text_shop_name.setOnClickListener { actionListener?.onCartShopNameClicked(data.shopId, data.shopName) }
        if (data.showDivider) {
            itemView.v_divider_item_cart_error.show()
        } else {
            itemView.v_divider_item_cart_error.gone()
        }
    }

}