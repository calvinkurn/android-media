package com.tokopedia.play.ui.productsheet.viewholder

import android.graphics.Paint
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import com.tokopedia.adapterdelegate.BaseViewHolder
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.loadImage
import com.tokopedia.kotlin.extensions.view.loadImageRounded
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.play.R
import com.tokopedia.play.view.type.DiscountedPrice
import com.tokopedia.play.view.type.OriginalPrice
import com.tokopedia.play.view.type.ProductSheetProduct
import com.tokopedia.unifycomponents.UnifyButton

/**
 * Created by jegul on 03/03/20
 */
class ProductLineViewHolder(itemView: View) : BaseViewHolder(itemView) {

    private val ivProductImage: ImageView = itemView.findViewById(R.id.iv_product_image)
    private val tvProductTitle: TextView = itemView.findViewById(R.id.tv_product_title)
    private val llProductDiscount: LinearLayout = itemView.findViewById(R.id.ll_product_discount)
    private val tvProductDiscount: TextView = itemView.findViewById(R.id.tv_product_discount)
    private val tvOriginalPrice: TextView = itemView.findViewById(R.id.tv_original_price)
    private val tvCurrentPrice: TextView = itemView.findViewById(R.id.tv_current_price)
    private val btnProductBuy: UnifyButton = itemView.findViewById(R.id.btn_product_buy)
    private val ivProductAtc: ImageView = itemView.findViewById(R.id.iv_product_atc)

    private val imageRadius = itemView.resources.getDimensionPixelSize(R.dimen.play_product_image_radius).toFloat()

    init {
        tvOriginalPrice.paintFlags = tvOriginalPrice.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
    }

    fun bind(item: ProductSheetProduct) {
        ivProductImage.loadImageRounded(item.imageUrl, imageRadius)
        tvProductTitle.text = item.title

        when (item.price) {
            is DiscountedPrice -> {
                llProductDiscount.visible()
                tvProductDiscount.text = itemView.context.getString(R.string.play_discount_percent, item.price.discountPercent)
                tvOriginalPrice.text = item.price.originalPrice
                tvCurrentPrice.text = item.price.discountedPrice
            }
            is OriginalPrice -> {
                llProductDiscount.gone()
                tvCurrentPrice.text = item.price.price
            }
        }

        btnProductBuy.setOnClickListener {
            Toast.makeText(itemView.context, "Buy product ${item.title}", Toast.LENGTH_SHORT).show()
        }

        ivProductAtc.setOnClickListener {
            Toast.makeText(itemView.context, "Atc product ${item.title}", Toast.LENGTH_SHORT).show()
        }
    }
}
