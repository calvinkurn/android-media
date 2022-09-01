package com.tokopedia.play.ui.productsheet.viewholder

import android.content.Context
import android.graphics.Paint
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.adapterdelegate.BaseViewHolder
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.iconunify.getIconUnifyDrawable
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.loadImageRounded
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.play.R
import com.tokopedia.play.view.type.ComingSoon
import com.tokopedia.play.view.type.DiscountedPrice
import com.tokopedia.play.view.type.OriginalPrice
import com.tokopedia.play.view.type.OutOfStock
import com.tokopedia.play.view.type.StockAvailable
import com.tokopedia.play.view.uimodel.PlayProductUiModel
import com.tokopedia.play_common.util.extension.buildSpannedString
import com.tokopedia.unifycomponents.Label
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifyprinciples.R as unifyR

/**
 * Created by jegul on 03/03/20
 */
class ProductLineViewHolder(
    itemView: View,
    private val listener: Listener
) : BaseViewHolder(itemView) {

    private val context: Context
        get() = itemView.context

    private val tvProductTitle: TextView = itemView.findViewById(R.id.tv_product_title)
    private val ivProductImage: ImageView = itemView.findViewById(R.id.iv_product_image)
    private val tvProductDiscount: TextView = itemView.findViewById(R.id.tv_product_discount)
    private val tvOriginalPrice: TextView = itemView.findViewById(R.id.tv_original_price)
    private val tvCurrentPrice: TextView = itemView.findViewById(R.id.tv_current_price)
    private val btnProductBuy: UnifyButton = itemView.findViewById(R.id.btn_product_buy)
    private val btnProductAtc: UnifyButton = itemView.findViewById(R.id.btn_product_atc)
    private val lblOutOfStock: Label = itemView.findViewById(R.id.label_out_of_stock)
    private val shadowOutOfStock: View = itemView.findViewById(R.id.shadow_out_of_stock)
    private val ivNow: IconUnify = itemView.findViewById(R.id.iv_now)
    private val tvNow: TextView = itemView.findViewById(R.id.tv_now)
    private val llInfo: LinearLayout = itemView.findViewById(R.id.ll_info)
    private val iconPinned: IconUnify = itemView.findViewById(R.id.icon_pinned)
    private val tvInfo: TextView = itemView.findViewById(R.id.tv_info)

    private val imageRadius = itemView.resources.getDimensionPixelSize(R.dimen.play_product_image_radius).toFloat()
    private val separatorSpan = ForegroundColorSpan(
        MethodChecker.getColor(context, unifyR.color.Unify_NN500)
    )
    private val stockSpan = ForegroundColorSpan(
        MethodChecker.getColor(context, unifyR.color.Unify_RN500)
    )

    init {
        tvOriginalPrice.paintFlags = tvOriginalPrice.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
    }

    fun bind(item: PlayProductUiModel.Product) {
        ivProductImage.loadImageRounded(item.imageUrl, imageRadius)
        tvProductTitle.text = item.title

        llInfo.showWithCondition(
            shouldShow = item.isPinned ||
                    (item.stock is StockAvailable && item.stock.stock <= MIN_STOCK)
        )
        iconPinned.showWithCondition(item.isPinned)
        tvInfo.text = getInfo(item)

        tvNow.showWithCondition(item.isTokoNow)
        ivNow.showWithCondition(item.isTokoNow)

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
            is ComingSoon ->{
                btnProductAtc.hide()
                btnProductBuy.hide()
            }
        }

        btnProductBuy.setOnClickListener {
            listener.onBuyProduct(item)
        }
        btnProductAtc.setOnClickListener {
            listener.onAtcProduct(item)
        }
        itemView.setOnClickListener {
            if (!item.applink.isNullOrEmpty()) listener.onClicked(this, item)
        }
    }

    private fun getInfo(item: PlayProductUiModel.Product): CharSequence {
        return buildSpannedString {
            if (item.isPinned) {
                append(getString(R.string.play_product_pinned))
                append(' ')
            }

            if (item.stock !is StockAvailable ||
                item.stock.stock > MIN_STOCK) return@buildSpannedString

            if (item.isPinned) {
                val separator = getString(R.string.play_product_pinned_info_separator)
                append(separator, separatorSpan, Spanned.SPAN_INCLUSIVE_EXCLUSIVE)
                append(' ')
            }

            val stockText = getString(R.string.play_product_item_stock, item.stock.stock)
            append(stockText, stockSpan, Spanned.SPAN_INCLUSIVE_EXCLUSIVE)
        }
    }

    companion object {
        val LAYOUT = R.layout.item_play_product_line

        private const val MIN_STOCK: Int = 5
    }

    interface Listener {
        fun onClicked(viewHolder: ProductLineViewHolder, product: PlayProductUiModel.Product)
        fun onBuyProduct(product: PlayProductUiModel.Product)
        fun onAtcProduct(product: PlayProductUiModel.Product)
    }
}
