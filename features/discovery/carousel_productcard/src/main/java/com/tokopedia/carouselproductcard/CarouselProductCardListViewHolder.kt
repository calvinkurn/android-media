package com.tokopedia.carouselproductcard

import android.view.View
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.ViewHintListener
import kotlinx.android.synthetic.main.carousel_product_card_item_list_layout.view.*

internal class CarouselProductCardListViewHolder(
        itemView: View,
        private val carouselProductCardListenerInfo: CarouselProductCardListenerInfo
): RecyclerView.ViewHolder(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.carousel_product_card_item_list_layout
    }

    fun bind(carouselProductCardModel: CarouselProductCardModel) {
        val productCardModel = carouselProductCardModel.productCardModel
        val onItemClickListener = carouselProductCardListenerInfo.onItemClickListener
        val onItemImpressedListener = carouselProductCardListenerInfo.onItemImpressedListener
        val onItemAddToCartListener = carouselProductCardListenerInfo.onItemAddToCartListener
        val onItemThreeDotsClickListener = carouselProductCardListenerInfo.onItemThreeDotsClickListener

        itemView.carouselProductCardItem?.applyCarousel()

        itemView.carouselProductCardItem?.setProductModel(productCardModel)

        itemView.carouselProductCardItem?.setOnClickListener {
            onItemClickListener?.onItemClick(productCardModel, adapterPosition)
        }

        onItemImpressedListener?.getImpressHolder(adapterPosition)?.let {
            itemView.carouselProductCardItem?.setImageProductViewHintListener(it, object : ViewHintListener {
                override fun onViewHint() {
                    onItemImpressedListener.onItemImpressed(productCardModel, adapterPosition)
                }
            })
        }

        itemView.carouselProductCardItem?.setAddToCartOnClickListener {
            onItemAddToCartListener?.onItemAddToCart(productCardModel, adapterPosition)
        }

        itemView.carouselProductCardItem?.setThreeDotsOnClickListener {
            onItemThreeDotsClickListener?.onItemThreeDotsClick(productCardModel, adapterPosition)
        }
    }

    fun recycle() {
        itemView.carouselProductCardItem?.recycle()
    }
}