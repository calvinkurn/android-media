package com.tokopedia.vouchergame.detail.view.adapter.viewholder

import android.graphics.Paint
import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.invisible
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.vouchergame.R
import com.tokopedia.vouchergame.detail.data.VoucherGameProduct
import com.tokopedia.vouchergame.detail.view.adapter.VoucherGameDetailAdapter
import kotlinx.android.synthetic.main.item_voucher_game_detail.view.*

/**
 * @author by resakemal on 12/08/19
 */

class VoucherGameProductViewHolder(val view: View, val listener: OnClickListener) : AbstractViewHolder<VoucherGameProduct>(view) {

    var hasMoreDetails = false
    lateinit var adapter: VoucherGameDetailAdapter

    override fun bind(product: VoucherGameProduct) {
        with(itemView) {
            with(product.attributes) {
                title_product.text = desc
                product_promo_price.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
                if (promo != null) {
                    product_promo_price.visibility = View.VISIBLE
                    product_promo_price.text = price
                    product_price.text = promo?.newPrice
                } else {
                    product_promo_price.visibility = View.INVISIBLE
                    product_price.text = price
                }

                if (productLabels.isNotEmpty()) {
                    product_promo_label.visibility = View.VISIBLE
                    product_promo_label.text = productLabels.joinToString(",", limit = 2)
                } else {
                    product_promo_label.visibility = View.GONE
                }

                when {
                    detail.isNotEmpty() -> {
                        product_detail.show()
                        product_detail.setOnClickListener { listener.onDetailClicked(product) }
                    }
                    hasMoreDetails -> {
                        product_detail.invisible()
                    }
                    else -> {
                        product_detail.hide()
                    }
                }
            }
            if (::adapter.isInitialized) {
                isSelected = adapter.selectedPos == adapterPosition

                layout_product.setOnClickListener {
                    adapter.setSelectedProduct(adapterPosition)
                    listener.onItemClicked(product, adapterPosition)
                }
            }
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