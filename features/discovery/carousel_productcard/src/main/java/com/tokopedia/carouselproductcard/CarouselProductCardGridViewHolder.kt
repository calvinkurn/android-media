package com.tokopedia.carouselproductcard

import android.view.View
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.ViewHintListener
import kotlinx.android.synthetic.main.carousel_product_card_item_grid_layout.view.*

internal class CarouselProductCardGridViewHolder(
        itemView: View
): RecyclerView.ViewHolder(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.carousel_product_card_item_grid_layout
    }

    fun bind(carouselProductCardModel: CarouselProductCardModel) {
        val productCardModel = carouselProductCardModel.productCardModel
        val onItemClickListener = carouselProductCardModel.getOnItemClickListener()
        val onItemImpressedListener = carouselProductCardModel.getOnItemImpressedListener()
        val onItemAddToCartListener = carouselProductCardModel.getOnItemAddToCartListener()
        val onItemThreeDotsClickListener = carouselProductCardModel.getOnItemThreeDotsClickListener()

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