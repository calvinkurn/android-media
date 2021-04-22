package com.tokopedia.play.ui.productsheet.viewholder

import android.view.View
import android.widget.ImageView
import androidx.core.graphics.drawable.DrawableCompat
import com.tokopedia.abstraction.common.utils.view.MethodChecker
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
    private val ivProductAtc: ImageView = itemView.findViewById(R.id.iv_product_atc)

    init {
        ivProductAtc.drawable.mutate()
    }

    override fun bind(item: PlayProductUiModel.Product) {
        super.bind(item)
        when (item.stock) {
            OutOfStock -> {
                btnProductBuy.isEnabled = false
                ivProductAtc.isEnabled = false
                btnProductBuy.text = getString(R.string.play_product_empty)

                DrawableCompat.setTint(ivProductAtc.drawable, MethodChecker.getColor(itemView.context, R.color.play_dms_atc_image_disabled))
            }
            is StockAvailable -> {
                btnProductBuy.isEnabled = true
                ivProductAtc.isEnabled = true
                btnProductBuy.text = getString(R.string.play_product_buy)

                DrawableCompat.setTintList(ivProductAtc.drawable, null)
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
