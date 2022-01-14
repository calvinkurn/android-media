package com.tokopedia.carouselproductcard

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.kotlin.extensions.view.ViewHintListener
import com.tokopedia.productcard.ATCNonVariantListener
import kotlinx.android.synthetic.main.carousel_product_card_item_grid_layout.view.*

internal class CarouselProductCardGridViewHolder(
    itemView: View,
    internalListener: CarouselProductCardInternalListener,
): BaseProductCardViewHolder<CarouselProductCardModel>(itemView, internalListener) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.carousel_product_card_item_grid_layout
    }

    override fun bind(carouselProductCardModel: CarouselProductCardModel) {
        val productCardModel = carouselProductCardModel.productCardModel

        itemView.carouselProductCardItem?.applyCarousel()

        itemView.carouselProductCardItem?.setProductModel(productCardModel)

        registerProductCardLifecycleObserver(itemView.carouselProductCardItem, productCardModel)

        setCarouselProductCardListeners(carouselProductCardModel)
    }

    private fun setCarouselProductCardListeners(carouselProductCardModel: CarouselProductCardModel) {
        val productCardModel = carouselProductCardModel.productCardModel

        val onItemClickListener = carouselProductCardModel.getOnItemClickListener()
        val onItemImpressedListener = carouselProductCardModel.getOnItemImpressedListener()
        val onItemAddToCartListener = carouselProductCardModel.getOnItemAddToCartListener()
        val onItemThreeDotsClickListener = carouselProductCardModel.getOnItemThreeDotsClickListener()
        val onATCNonVariantClickListener = carouselProductCardModel.getOnATCNonVariantClickListener()
        val onAddVariantClickListener = carouselProductCardModel.getAddVariantClickListener()

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

        itemView.carouselProductCardItem?.setAddToCartNonVariantClickListener(object: ATCNonVariantListener {
            override fun onQuantityChanged(quantity: Int) {
                onATCNonVariantClickListener?.onATCNonVariantClick(productCardModel, adapterPosition, quantity)
            }
        })

        itemView.carouselProductCardItem?.setAddVariantClickListener {
            onAddVariantClickListener?.onAddVariantClick(productCardModel, adapterPosition)
        }
    }

    override fun recycle() {
        itemView.carouselProductCardItem?.recycle()

        unregisterProductCardLifecycleObserver(itemView.carouselProductCardItem)
    }
}