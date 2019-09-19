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

    var hasMoreDetails = false

    override fun bind(product: VoucherGameProduct) {
        with(itemView) {
            with(product.attributes) {
                title_product.text = desc
                product_promo_price.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
                if (promo == null) {
                    product_promo_price.visibility = View.INVISIBLE
                    product_price.text = price
                } else {
                    product_promo_price.text = price
                    product_price.text = promo.newPrice
                }

                if (productLabels.isNotEmpty())
                    product_promo_label.visibility = View.VISIBLE
                    product_promo_label.text = productLabels.joinToString(",", limit = 2)

                if (detail.isNotEmpty()) {
                    product_detail.visibility = View.VISIBLE
                    if (detailCompat.isNotEmpty()) product_detail.text = detailCompat
                    product_detail.setOnClickListener { listener.onDetailClicked(product) }
                } else {
                    product_detail.visibility = if (hasMoreDetails) View.INVISIBLE else View.GONE
                }
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
        fun onDetailClicked(product: VoucherGameProduct)
    }

}