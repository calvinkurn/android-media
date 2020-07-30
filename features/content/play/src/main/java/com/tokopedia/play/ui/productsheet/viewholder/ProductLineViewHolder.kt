package com.tokopedia.play.ui.productsheet.viewholder

import android.graphics.Paint
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.graphics.drawable.DrawableCompat
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.adapterdelegate.BaseViewHolder
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.loadImageRounded
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.play.R
import com.tokopedia.play.view.type.*
import com.tokopedia.play.view.uimodel.ProductLineUiModel
import com.tokopedia.unifycomponents.UnifyButton

/**
 * Created by jegul on 03/03/20
 */
class ProductLineViewHolder(itemView: View, private val listener: Listener) : BaseViewHolder(itemView) {

    val tvProductTitle: TextView = itemView.findViewById(R.id.tv_product_title)
    private val ivProductImage: ImageView = itemView.findViewById(R.id.iv_product_image)
    private val llProductDiscount: LinearLayout = itemView.findViewById(R.id.ll_product_discount)
    private val tvProductDiscount: TextView = itemView.findViewById(R.id.tv_product_discount)
    private val tvOriginalPrice: TextView = itemView.findViewById(R.id.tv_original_price)
    private val tvCurrentPrice: TextView = itemView.findViewById(R.id.tv_current_price)
    private val btnProductBuy: UnifyButton = itemView.findViewById(R.id.btn_product_buy)
    private val ivProductAtc: ImageView = itemView.findViewById(R.id.iv_product_atc)

    private val imageRadius = itemView.resources.getDimensionPixelSize(R.dimen.play_product_line_image_radius).toFloat()

    init {
        tvOriginalPrice.paintFlags = tvOriginalPrice.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG

        ivProductAtc.drawable.mutate()
    }

    fun bind(item: ProductLineUiModel) {
        ivProductImage.loadImageRounded(item.imageUrl, imageRadius)
        tvProductTitle.text = item.title

        when (item.stock) {
            OutOfStock -> {
                btnProductBuy.isEnabled = false
                ivProductAtc.isEnabled = false
                btnProductBuy.text = getString(R.string.play_product_empty)

                DrawableCompat.setTint(ivProductAtc.drawable, MethodChecker.getColor(itemView.context, R.color.play_atc_image_disabled))
            }
            is StockAvailable -> {
                btnProductBuy.isEnabled = true
                ivProductAtc.isEnabled = true
                btnProductBuy.text = getString(R.string.play_product_buy)

                DrawableCompat.setTintList(ivProductAtc.drawable, null)
            }
        }

        when (item.price) {
            is DiscountedPrice -> {
                llProductDiscount.show()
                tvProductDiscount.text = itemView.context.getString(R.string.play_discount_percent, item.price.discountPercent)
                tvOriginalPrice.text = item.price.originalPrice
                tvCurrentPrice.text = item.price.discountedPrice
            }
            is OriginalPrice -> {
                llProductDiscount.hide()
                tvCurrentPrice.text = item.price.price
            }
        }

        btnProductBuy.setOnClickListener {
            listener.onBuyProduct(item)
        }

        ivProductAtc.setOnClickListener {
            listener.onAtcProduct(item)
        }

        itemView.setOnClickListener {
            if (!item.applink.isNullOrEmpty()) listener.onClickProductCard(item, adapterPosition)
        }
    }

    interface Listener {
        fun onBuyProduct(product: ProductLineUiModel)
        fun onAtcProduct(product: ProductLineUiModel)
        fun onClickProductCard(product: ProductLineUiModel, position: Int)
    }
}
