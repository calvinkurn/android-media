package com.tokopedia.play.ui.productsheet.viewholder

import android.view.View
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
import com.tokopedia.unifycomponents.Label
import com.tokopedia.unifycomponents.UnifyButton

/**
 * Created by jegul on 03/03/20
 */
class ProductLineViewHolder(itemView: View, private val listener: Listener) : ProductBasicViewHolder(itemView, listener) {

    private val btnProductBuy: UnifyButton = itemView.findViewById(R.id.btn_product_buy)
    private val btnProductAtc: UnifyButton = itemView.findViewById(R.id.btn_product_atc)
    private val lblOutOfStock: Label = itemView.findViewById(R.id.label_out_of_stock)
    private val shadowOutOfStock: View = itemView.findViewById(R.id.shadow_out_of_stock)

    override fun bind(item: PlayProductUiModel.Product) {
        super.bind(item)
        when (item.stock) {
            OutOfStock -> {
                shadowOutOfStock.show()
                lblOutOfStock.show()
                btnProductAtc.setDrawable(
                    getIconUnifyDrawable(itemView.context, IconUnify.ADD, ContextCompat.getColor(itemView.context, com.tokopedia.unifyprinciples.R.color.Unify_NN100))
                )
                btnProductBuy.isEnabled = false
                btnProductAtc.isEnabled = false
            }

            is StockAvailable -> {
                shadowOutOfStock.gone()
                lblOutOfStock.gone()
                btnProductBuy.isEnabled = true
                btnProductAtc.isEnabled = true
                btnProductAtc.setDrawable(
                    getIconUnifyDrawable(itemView.context, IconUnify.ADD, ContextCompat.getColor(itemView.context, com.tokopedia.unifyprinciples.R.color.Unify_G500))
                )
            }
        }

        btnProductBuy.setOnClickListener {
            listener.onBuyProduct(item)
        }

        btnProductAtc.setOnClickListener {
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
