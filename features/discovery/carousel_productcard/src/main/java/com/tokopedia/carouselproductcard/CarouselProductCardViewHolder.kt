package com.tokopedia.carouselproductcard

import android.view.View
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.ViewHintListener
import com.tokopedia.productcard.v2.ProductCardModel
import kotlinx.android.synthetic.main.carousel_product_card_item_layout.view.*

internal class CarouselProductCardViewHolder(
        itemView: View,
        carouselProductCardListenerInfo: CarouselProductCardListenerInfo
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

        itemView.carouselProductCardItem?.setProductModel(productCardModel)

        itemView.carouselProductCardItem?.setOnClickListener {
            onItemClickListener?.onItemClick(productCardModel, adapterPosition)
        }

        itemView.carouselProductCardItem?.setOnLongClickListener {
            onItemLongClickListener?.onItemLongClick(productCardModel, adapterPosition)
            true
        }

        onItemImpressedListener?.getImpressHolder(adapterPosition)?.let {
            itemView.carouselProductCardItem?.setImageProductViewHintListener(it, object : ViewHintListener {
                override fun onViewHint() {
                    onItemImpressedListener?.onItemImpressed(productCardModel, adapterPosition)
                }
            })
        }

        itemView.carouselProductCardItem?.setAddToCartOnClickListener {
            onItemAddToCartListener?.onItemAddToCart(productCardModel, adapterPosition)
        }

        itemView.carouselProductCardItem?.setButtonWishlistOnClickListener {
            onWishlistClickListener?.onWishlistItemClick(productCardModel, adapterPosition)
        }
    }

    fun updateWishlist(isWishlist: Boolean){
        itemView.carouselProductCardItem.setButtonWishlistImage(isWishlist)
    }
}