package com.tokopedia.play.ui.productsheet.viewholder

import android.view.View
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.iconunify.getIconUnifyDrawable
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.play.R
import com.tokopedia.play.ui.product.ProductBasicViewHolder
import com.tokopedia.play.view.type.OutOfStock
import com.tokopedia.play.view.type.StockAvailable
import com.tokopedia.play.view.uimodel.PlayProductUiModel
import com.tokopedia.unifycomponents.UnifyButton

/**
 * Created by jegul on 03/03/20
 */
class ProductLineViewHolder(itemView: View, private val listener: Listener) : ProductBasicViewHolder(itemView, listener) {

    private val btnProductBuy: UnifyButton = itemView.findViewById(R.id.btn_product_buy)
    private val ivProductAtc: UnifyButton = itemView.findViewById(R.id.iv_product_atc)
    private val tvProductStock: TextView = itemView.findViewById(R.id.tv_product_out_of_stock)

    override fun bind(item: PlayProductUiModel.Product) {
        super.bind(item)
        when (item.stock) {
            OutOfStock -> {
                tvProductStock.gone()
                ivProductAtc.setBackgroundResource(0)
                btnProductBuy.isEnabled = false
                ivProductAtc.isEnabled = false
                ivProductAtc.text = getString(R.string.play_product_empty)
                btnProductBuy.text = getString(R.string.play_product_empty)
            }

            is StockAvailable -> {
                tvProductStock.show()
                tvProductStock.text = getString(R.string.play_product_item_stock, item.stock.stock)
                btnProductBuy.isEnabled = true
                ivProductAtc.isEnabled = true
                btnProductBuy.text = getString(R.string.play_product_buy)
                ivProductAtc.text = getString(R.string.play_product_atc)
                ivProductAtc.setDrawable(
                    getIconUnifyDrawable(itemView.context, IconUnify.ADD, ContextCompat.getColor(itemView.context, com.tokopedia.unifyprinciples.R.color.Unify_G500))
                )
            }
        }

        btnProductBuy.setOnClickListener {
            listener.onBuyProduct(item)
        }

        ivProductAtc.setOnClickListener {
            listener.onAtcProduct(item)
        }
    }

    companion object {
        val LAYOUT = R.layout.item_play_product_line
    }

    interface Listener : ProductBasicViewHolder.Listener {
        fun onBuyProduct(product: PlayProductUiModel.Product)
        fun onAtcProduct(product: PlayProductUiModel.Product)
    }
}
