package com.tokopedia.checkout.view.feature.promostacking.viewholder

import android.support.v7.widget.RecyclerView
import android.view.View
import com.tokopedia.checkout.R
import com.tokopedia.promocheckout.common.view.uimodel.ClashingVoucherOrderUiModel
import kotlinx.android.synthetic.main.item_clashing_voucher_inner.view.*

/**
 * Created by Irfan Khoirul on 22/03/19.
 */

class ClashingInnerViewHolder(val view: View) : RecyclerView.ViewHolder(view) {

    companion object {
        val LAYOUT = R.layout.item_clashing_voucher_inner
    }

    fun bind(element: ClashingVoucherOrderUiModel?) {
        if (element != null) {
            if (adapterPosition == 0) {
                itemView.holder_item_cart_divider.visibility = View.GONE
            } else {
                itemView.holder_item_cart_divider.visibility = View.VISIBLE
            }

            itemView.tv_shop_name.text = element.shopName
            itemView.tv_cashback_detail.text = element.promoName
        }
    }

}