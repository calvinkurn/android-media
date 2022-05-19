package com.tokopedia.play.ui.productfeatured.viewholder

import android.graphics.Paint
import android.view.View
import com.tokopedia.adapterdelegate.BaseViewHolder
import com.tokopedia.play.R
import com.tokopedia.play.ui.product.ProductBasicViewHolder
import com.tokopedia.play.view.type.DiscountedPrice
import com.tokopedia.play.view.type.OriginalPrice
import com.tokopedia.play.view.uimodel.PlayProductUiModel
import com.tokopedia.play_common.view.loadImage
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifyprinciples.Typography

/**
 * Created by jegul on 23/02/21
 */
class ProductFeaturedViewHolder(
        itemView: View,
        private val listener: ProductBasicViewHolder.Listener,
) : BaseViewHolder(itemView) {

    private val ivProductImage: ImageUnify = itemView.findViewById(R.id.iv_product_image)
    private val tvFinalPrice: Typography = itemView.findViewById(R.id.tv_final_price)
    private val tvSlashedPrice: Typography = itemView.findViewById(R.id.tv_slashed_price)
    private val tvProductDiscount: Typography = itemView.findViewById(R.id.tv_product_discount)

    init {
        tvSlashedPrice.paintFlags = tvSlashedPrice.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
    }

    fun bind(item: PlayProductUiModel.Product) {
        ivProductImage.loadImage(item.imageUrl)

        when (item.price) {
            is DiscountedPrice -> {
                tvProductDiscount.visibility = View.VISIBLE
                tvProductDiscount.text = itemView.context.getString(
                    R.string.play_discount_percent,
                    item.price.discountPercent
                )
                tvSlashedPrice.visibility = View.VISIBLE
                tvFinalPrice.text = item.price.discountedPrice
                tvSlashedPrice.text = item.price.originalPrice
            }
            is OriginalPrice -> {
                tvProductDiscount.visibility = View.GONE
                tvSlashedPrice.visibility = View.INVISIBLE
                tvFinalPrice.text = item.price.price
            }
        }

        itemView.setOnClickListener {
            if (item.applink.isNullOrEmpty()) return@setOnClickListener
            listener.onClickProductCard(item, adapterPosition)
        }
    }

    companion object {
        val LAYOUT = R.layout.item_play_product_featured
    }
}