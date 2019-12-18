package com.tokopedia.vouchergame.detail.view.adapter.viewholder

import android.graphics.Paint
import androidx.appcompat.content.res.AppCompatResources
import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
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
                    product_promo_label.visibility = View.INVISIBLE
                }

                if (detail.isNotEmpty()) {
                    product_detail.visibility = View.VISIBLE
                    if (detailCompat.isNotEmpty()) product_detail.text = detailCompat
                    product_detail.setOnClickListener { listener.onDetailClicked(product) }
                } else {
                    product_detail.visibility = if (hasMoreDetails) View.INVISIBLE else View.GONE
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