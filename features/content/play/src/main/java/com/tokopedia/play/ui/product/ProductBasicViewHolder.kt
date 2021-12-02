package com.tokopedia.play.ui.product

import android.graphics.Paint
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.tokopedia.adapterdelegate.BaseViewHolder
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.play.R
import com.tokopedia.play.view.type.DiscountedPrice
import com.tokopedia.play.view.type.OriginalPrice
import com.tokopedia.play.view.type.OutOfStock
import com.tokopedia.play.view.type.StockAvailable
import com.tokopedia.play.view.uimodel.PlayProductUiModel
import com.tokopedia.play_common.view.loadImage

/**
 * Created by jegul on 23/02/21
 */
open class ProductBasicViewHolder(
        itemView: View, private val listener: Listener
) : BaseViewHolder(itemView) {

    val tvProductTitle: TextView = itemView.findViewById(R.id.tv_product_title)
    private val ivProductImage: ImageView = itemView.findViewById(R.id.iv_product_image)
    private val tvProductDiscount: TextView = itemView.findViewById(R.id.tv_product_discount)
    private val tvOriginalPrice: TextView = itemView.findViewById(R.id.tv_original_price)
    private val tvCurrentPrice: TextView = itemView.findViewById(R.id.tv_current_price)
    private val tvOutOfStock: TextView = itemView.findViewById(R.id.tv_product_out_of_stock)

    init {
        tvOriginalPrice.paintFlags = tvOriginalPrice.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
    }

    open fun bind(item: PlayProductUiModel.Product) {
        ivProductImage.loadImage(item.imageUrl)
        tvProductTitle.text = item.title

        when (item.stock) {
            is OutOfStock -> tvOutOfStock.show()
            is StockAvailable -> tvOutOfStock.hide()
        }

        when (item.price) {
            is DiscountedPrice -> {
                tvProductDiscount.show()
                tvOriginalPrice.show()
                tvProductDiscount.text = itemView.context.getString(R.string.play_discount_percent, item.price.discountPercent)
                tvOriginalPrice.text = item.price.originalPrice
                tvCurrentPrice.text = item.price.discountedPrice
            }
            is OriginalPrice -> {
                tvProductDiscount.hide()
                tvOriginalPrice.hide()
                tvCurrentPrice.text = item.price.price
            }
        }

        itemView.setOnClickListener {
            if (!item.applink.isNullOrEmpty()) listener.onClickProductCard(item, adapterPosition)
        }
    }

    interface Listener {
        fun onClickProductCard(product: PlayProductUiModel.Product, position: Int)
    }
}