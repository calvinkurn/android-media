package com.tokopedia.play.broadcaster.ui.viewholder

import android.graphics.Paint
import android.view.View
import com.tokopedia.adapterdelegate.BaseViewHolder
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.invisible
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.play.broadcaster.R
import com.tokopedia.play_common.R as playCommonR
import com.tokopedia.play.broadcaster.type.DiscountedPrice
import com.tokopedia.play.broadcaster.type.OriginalPrice
import com.tokopedia.play.broadcaster.type.StockAvailable
import com.tokopedia.play.broadcaster.ui.model.ProductContentUiModel
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifyprinciples.Typography

/**
 * Created By : Jonathan Darwin on November 23, 2021
 */
class ProductTagViewHolder(
    itemView: View
): BaseViewHolder(itemView) {

    private val ivProductTag: ImageUnify = itemView.findViewById(playCommonR.id.iv_product_tag)
    private val ivProductTagCover: ImageUnify = itemView.findViewById(playCommonR.id.iv_product_tag_cover)
    private val tvDiscount: Typography = itemView.findViewById(playCommonR.id.tv_product_tag_discount)
    private val tvProductTagStock: Typography = itemView.findViewById(playCommonR.id.tv_product_tag_stock)
    private val tvProductTagPrice: Typography = itemView.findViewById(playCommonR.id.tv_product_tag_price)
    private val tvProductTagPriceOriginal: Typography = itemView.findViewById(playCommonR.id.tv_product_tag_normal_price)

    init {
        tvProductTagPriceOriginal.paintFlags = tvProductTagPriceOriginal.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
    }

    fun bind(item: ProductContentUiModel) {
        ivProductTag.setImageUrl(item.imageUrl)
        if (item.stock is StockAvailable) {
            tvProductTagStock.text = getString(R.string.play_bro_product_tag_stock_amount, item.stock.stock)
            ivProductTagCover.hide()
        }
        else {
            tvProductTagStock.text = getString(R.string.play_bro_product_tag_stock_empty)
            ivProductTagCover.show()
        }

         when(item.price) {
            is DiscountedPrice -> {
                tvProductTagPriceOriginal.show()
                tvDiscount.show()
                tvDiscount.text = getString(R.string.play_bro_product_discount_template, item.price.discountPercent)
                tvProductTagPrice.text = item.price.discountedPrice
                tvProductTagPriceOriginal.text = item.price.originalPrice
            }
            is OriginalPrice -> {
                tvProductTagPriceOriginal.invisible()
                tvDiscount.hide()
                tvProductTagPrice.text = item.price.price
            }
            else -> {
                tvProductTagPriceOriginal.invisible()
                tvDiscount.hide()
                tvProductTagPrice.text = getString(R.string.play_bro_product_tag_no_price)
            }
        }
    }

    companion object {
        val LAYOUT = playCommonR.layout.item_play_product_tag
    }
}