package com.tokopedia.productcard.carousel

import android.support.annotation.LayoutRes
import android.support.v7.widget.RecyclerView
import android.view.View
import com.tokopedia.kotlin.extensions.view.ViewHintListener
import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.productcard.R
import com.tokopedia.productcard.v2.BlankSpaceConfig
import com.tokopedia.productcard.v2.ProductCardModel
import kotlinx.android.synthetic.main.product_card_carousel_item_layout.view.*

internal class ProductCardCarouselViewHolder(
        itemView: View,
        productCardCarouselListenerInfo: ProductCardCarouselListenerInfo,
        private val blankSpaceConfig: BlankSpaceConfig
): RecyclerView.ViewHolder(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.product_card_carousel_item_layout
    }

    private val onItemClickListener = productCardCarouselListenerInfo.onItemClickListener
    private val onItemLongClickListener = productCardCarouselListenerInfo.onItemLongClickListener
    private val onItemImpressedListener = productCardCarouselListenerInfo.onItemImpressedListener
    private val onItemAddToCartListener = productCardCarouselListenerInfo.onItemAddToCartListener
    private val onWishlistClickListener = productCardCarouselListenerInfo.onWishlistItemClickListener

    fun bind(productCardModel: ProductCardModel) {

        itemView.productCardCarouselItem?.setProductModel(productCardModel, blankSpaceConfig)

        itemView.productCardCarouselItem?.setOnClickListener {
            onItemClickListener?.onItemClick(productCardModel)
        }

        itemView.productCardCarouselItem?.setOnLongClickListener {
            onItemLongClickListener?.onItemLongClick(productCardModel)
            true
        }

        // TODO:: Verify this ViewHintListener
        itemView.productCardCarouselItem?.setImageProductViewHintListener(ImpressHolder(), object : ViewHintListener {
            override fun onViewHint() {
                onItemImpressedListener?.onItemImpressed(productCardModel)
            }
        })

        itemView.productCardCarouselItem?.setAddToCartOnClickListener {
            onItemAddToCartListener?.onItemAddToCart(productCardModel)
        }

        itemView.productCardCarouselItem?.setButtonWishlistOnClickListener {
            onWishlistClickListener?.onWishlistItemClick(productCardModel)
        }
    }
}