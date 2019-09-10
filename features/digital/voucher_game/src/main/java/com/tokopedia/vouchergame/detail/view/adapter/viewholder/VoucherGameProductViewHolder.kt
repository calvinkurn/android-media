package com.tokopedia.vouchergame.detail.view.adapter.viewholder

import android.graphics.Paint
import android.support.v7.content.res.AppCompatResources
import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.vouchergame.R
import com.tokopedia.vouchergame.detail.data.VoucherGameProduct
import kotlinx.android.synthetic.main.item_voucher_game_detail.view.*

/**
 * @author by resakemal on 12/08/19
 */

class VoucherGameProductViewHolder(val view: View, val listener: OnClickListener) : AbstractViewHolder<VoucherGameProduct>(view) {

    override fun bind(product: VoucherGameProduct) {
        with(itemView) {
            title_product.text = product.attributes.desc
            product_promo_price.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
            if (product.attributes.promo == null) {
                product_promo_price.visibility = View.INVISIBLE
                product_price.text = product.attributes.price
            } else {
                product_promo_label.text = product.attributes.productLabels.joinToString { "," }
                product_promo_price.text = product.attributes.price
                product_price.text = product.attributes.promo.newPrice
            }

            // Show selected item
            var drawable = AppCompatResources.getDrawable(context, R.drawable.digital_bg_transparent_round)
            if (product.selected) {
                drawable = AppCompatResources.getDrawable(context, R.drawable.digital_bg_green_light_rounded)
            }
            layout_product.background = drawable

            layout_product.setOnClickListener { listener.onItemClicked(product, adapterPosition) }
        }
    }

    companion object {
        val LAYOUT = R.layout.item_voucher_game_detail
    }

    interface OnClickListener {
        fun onItemClicked(product: VoucherGameProduct, position: Int)
    }

}