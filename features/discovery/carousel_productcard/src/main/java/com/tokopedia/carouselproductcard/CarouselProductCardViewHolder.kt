package com.tokopedia.carouselproductcard

import android.support.annotation.LayoutRes
import android.support.v7.widget.RecyclerView
import android.view.View
import com.tokopedia.kotlin.extensions.view.ViewHintListener
import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.productcard.v2.BlankSpaceConfig
import com.tokopedia.productcard.v2.ProductCardModel
import kotlinx.android.synthetic.main.carousel_product_card_item_layout.view.*

internal class CarouselProductCardViewHolder(
        itemView: View,
        carouselProductCardListenerInfo: CarouselProductCardListenerInfo,
        private val blankSpaceConfig: BlankSpaceConfig
): RecyclerView.ViewHolder(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.carousel_product_card_item_layout
    }

    private val onItemClickListener = carouselProductCardListenerInfo.onItemClickListener
    private val onItemLongClickListener = carouselProductCardListenerInfo.onItemLongClickListener
    private val onItemImpressedListener = carouselProductCardListenerInfo.onItemImpressedListener
    private val onItemAddToCartListener = carouselProductCardListenerInfo.onItemAddToCartListener
    private val onWishlistClickListener = carouselProductCardListenerInfo.onWishlistItemClickListener

    fun bind(productCardModel: ProductCardModel) {

        itemView.carouselProductCardItem?.setProductModel(productCardModel, blankSpaceConfig)

        itemView.carouselProductCardItem?.setOnClickListener {
            onItemClickListener?.onItemClick(productCardModel)
        }

        itemView.carouselProductCardItem?.setOnLongClickListener {
            onItemLongClickListener?.onItemLongClick(productCardModel)
            true
        }

        // TODO:: Verify this ViewHintListener
        itemView.carouselProductCardItem?.setImageProductViewHintListener(ImpressHolder(), object : ViewHintListener {
            override fun onViewHint() {
                onItemImpressedListener?.onItemImpressed(productCardModel)
            }
        })

        itemView.carouselProductCardItem?.setAddToCartOnClickListener {
            onItemAddToCartListener?.onItemAddToCart(productCardModel)
        }

        itemView.carouselProductCardItem?.setButtonWishlistOnClickListener {
            onWishlistClickListener?.onWishlistItemClick(productCardModel)
        }
    }
}