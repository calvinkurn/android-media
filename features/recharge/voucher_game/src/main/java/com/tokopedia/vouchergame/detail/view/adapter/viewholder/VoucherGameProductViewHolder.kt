package com.tokopedia.vouchergame.detail.view.adapter.viewholder

import android.content.Context
import android.graphics.Paint
import android.view.View
import androidx.annotation.LayoutRes
import androidx.core.content.ContextCompat
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.invisible
import com.tokopedia.kotlin.extensions.view.shouldShowWithAction
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.unifycomponents.Label
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.vouchergame.R
import com.tokopedia.vouchergame.databinding.ItemVoucherGameDetailBinding
import com.tokopedia.vouchergame.detail.data.VoucherGameProduct
import com.tokopedia.vouchergame.detail.view.adapter.VoucherGameDetailAdapter

/**
 * @author by resakemal on 12/08/19
 */

class VoucherGameProductViewHolder(
    val view: View,
    private val listener: OnClickListener
) : AbstractViewHolder<VoucherGameProduct>(view) {

    var hasMoreDetails = false
    lateinit var adapter: VoucherGameDetailAdapter

    override fun bind(product: VoucherGameProduct) {
        with(itemView) {
            val binding = ItemVoucherGameDetailBinding.bind(this)
            with(product.attributes) {
                binding.titleProduct.run {
                    text = desc
                    setStatusOutOfStockColor(
                        status,
                        context,
                        com.tokopedia.unifyprinciples.R.color.Unify_NN950_96
                    )
                }

                binding.productPrice.run {
                    if (promo != null) {
                        text = promo?.newPrice
                    } else {
                        text = price
                    }
                    setStatusOutOfStockColor(
                        status,
                        context,
                        com.tokopedia.unifyprinciples.R.color.Unify_YN500
                    )
                }

                binding.productPromoPrice.run {
                    if (promo != null) {
                        show()
                        text = price
                    } else {
                        invisible()
                    }
                    paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
                    setStatusOutOfStockColor(
                        status,
                        context,
                        com.tokopedia.unifyprinciples.R.color.Unify_NN950_44
                    )
                }

                binding.productPromoLabel.run {
                    shouldShowWithAction(productLabels.isNotEmpty()) {
                        text = productLabels.joinToString(",", limit = 2)
                    }
                    setStatusOutOfStockColor(status, Label.HIGHLIGHT_DARK_RED)
                }

                binding.productOutOfStockLabel.run {
                    showWithCondition(status == STATUS_OUT_OF_STOCK)
                    setStatusOutOfStockColor(status, Label.HIGHLIGHT_LIGHT_GREY)
                }

                binding.productDetail.run {
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
                        com.tokopedia.unifyprinciples.R.color.Unify_GN500
                    )
                }

                if (::adapter.isInitialized && status != STATUS_OUT_OF_STOCK) {
                    isSelected = adapter.selectedPos == adapterPosition

                    binding.layoutProduct.setOnClickListener {
                        listener.onItemClicked(product, adapterPosition)
                    }
                } else {
                    binding.layoutProduct.setOnClickListener {
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
        @LayoutRes
        val LAYOUT = R.layout.item_voucher_game_detail
        const val STATUS_OUT_OF_STOCK = 3
    }

    interface OnClickListener {
        fun onItemClicked(product: VoucherGameProduct, position: Int)
        fun onDetailClicked(product: VoucherGameProduct)
    }
}
