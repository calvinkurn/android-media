package com.tokopedia.vouchergame.detail.view.adapter.viewholder

import android.content.Context
import android.graphics.Paint
import android.view.View
import androidx.core.content.ContextCompat
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.invisible
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.unifycomponents.Label
import com.tokopedia.unifyprinciples.Typography
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
                title_product.run {
                    text = desc
                    setStatusOutOfStockColor(
                        status,
                        context,
                        com.tokopedia.unifyprinciples.R.color.Unify_N700_96
                    )
                }

                product_price.run {
                    if (promo != null) {
                        product_price.text = promo?.newPrice
                    } else {
                        product_price.text = price
                    }
                    setStatusOutOfStockColor(
                        status,
                        context,
                        com.tokopedia.unifyprinciples.R.color.Unify_Y500
                    )
                }

                product_promo_price.run {
                    if (promo != null) {
                        product_promo_price.visibility = View.VISIBLE
                        product_promo_price.text = price
                    } else {
                        product_promo_price.visibility = View.INVISIBLE
                    }
                    paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
                    setStatusOutOfStockColor(
                        status,
                        context,
                        com.tokopedia.unifyprinciples.R.color.Unify_N700_44
                    )
                }

                product_promo_label.run {
                    if (productLabels.isNotEmpty()) {
                        visibility = View.VISIBLE
                        text = productLabels.joinToString(",", limit = 2)
                    } else {
                        visibility = View.GONE
                    }
                    setStatusOutOfStockColor(status, Label.GENERAL_DARK_RED)
                }

                product_out_of_stock_label.run {
                    if (status == STATUS_OUT_OF_STOCK) {
                        product_out_of_stock_label.visibility = View.VISIBLE
                    } else {
                        product_out_of_stock_label.visibility = View.GONE
                    }
                    setStatusOutOfStockColor(status, Label.HIGHLIGHT_LIGHT_GREY)
                }

                product_detail.run {
                    when {
                        detail.isNotEmpty() -> {
                            show()
                            setOnClickListener {
                                if (status != STATUS_OUT_OF_STOCK) listener.onDetailClicked(product)
                            }
                        }
                        hasMoreDetails -> invisible()
                        else -> hide()
                    }
                    setStatusOutOfStockColor(
                        status,
                        context,
                        com.tokopedia.unifyprinciples.R.color.Unify_G500
                    )
                }

                if (::adapter.isInitialized && status != STATUS_OUT_OF_STOCK) {
                    isSelected = adapter.selectedPos == adapterPosition

                    layout_product.setOnClickListener {
                        adapter.setSelectedProduct(adapterPosition)
                        listener.onItemClicked(product, adapterPosition)
                    }
                } else {
                    layout_product.setOnClickListener {
                        // do nothing or disabled
                    }
                }
            }
        }
    }

    private fun Label.setStatusOutOfStockColor(status: Int, defaultLabelId: Int) {
        if (status == STATUS_OUT_OF_STOCK) {
            setLabelType(Label.HIGHLIGHT_LIGHT_GREY)
        } else {
            setLabelType(defaultLabelId)
        }
    }

    private fun Typography.setStatusOutOfStockColor(status: Int, context: Context, defaultColorId: Int) {
        if (status == STATUS_OUT_OF_STOCK) {
            setTextColor(
                ContextCompat.getColor(
                    context,
                    com.tokopedia.unifyprinciples.R.color.Unify_NN400
                )
            )
        } else {
            setTextColor(
                ContextCompat.getColor(
                    context,
                    defaultColorId
                )
            )
        }
    }

    companion object {
        val LAYOUT = R.layout.item_voucher_game_detail
        const val STATUS_OUT_OF_STOCK = 3
    }

    interface OnClickListener {
        fun onItemClicked(product: VoucherGameProduct, position: Int)
        fun onDetailClicked(product: VoucherGameProduct)
    }

}