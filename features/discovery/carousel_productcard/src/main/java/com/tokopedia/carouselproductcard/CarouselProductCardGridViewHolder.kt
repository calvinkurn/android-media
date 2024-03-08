package com.tokopedia.carouselproductcard

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.carouselproductcard.databinding.CarouselProductCardItemGridLayoutBinding
import com.tokopedia.kotlin.extensions.view.ViewHintListener
import com.tokopedia.productcard.ATCNonVariantListener
import com.tokopedia.productcard.ProductCardGridView
import com.tokopedia.utils.view.binding.viewBinding

internal class CarouselProductCardGridViewHolder(
    itemView: View,
    internalListener: CarouselProductCardInternalListener,
): BaseProductCardViewHolder<CarouselProductCardModel>(itemView, internalListener) {

    companion object {
        @LayoutRes
        fun layout(isReimagine: Boolean): Int =
            if (isReimagine) R.layout.carousel_product_card_item_grid_reimagine_layout
            else R.layout.carousel_product_card_item_grid_layout
    }

    private fun productCardGridView(): ProductCardGridView? =
        itemView.findViewById(R.id.carouselProductCardItem)

    override fun bind(carouselProductCardModel: CarouselProductCardModel) {
        val productCardGridView = productCardGridView()
        val productCardModel = carouselProductCardModel.productCardModel

        productCardGridView?.applyCarousel()

        productCardGridView?.setProductModel(productCardModel)

        registerProductCardLifecycleObserver(productCardGridView, productCardModel)

        setCarouselProductCardListeners(carouselProductCardModel)
    }

    private fun setCarouselProductCardListeners(carouselProductCardModel: CarouselProductCardModel) {
        val productCardGridView = productCardGridView()
        val productCardModel = carouselProductCardModel.productCardModel

        val onItemClickListener = carouselProductCardModel.getOnItemClickListener()
        val onItemImpressedListener = carouselProductCardModel.getOnItemImpressedListener()
        val onItemAddToCartListener = carouselProductCardModel.getOnItemAddToCartListener()
        val onItemThreeDotsClickListener = carouselProductCardModel.getOnItemThreeDotsClickListener()
        val onATCNonVariantClickListener = carouselProductCardModel.getOnATCNonVariantClickListener()
        val onAddVariantClickListener = carouselProductCardModel.getAddVariantClickListener()
        val onSeeOtherProductClickListener = carouselProductCardModel.getSeeOtherClickListener()

        productCardGridView?.setOnClickListener {
            onItemClickListener?.onItemClick(productCardModel, bindingAdapterPosition)
        }

        onItemImpressedListener?.getImpressHolder(bindingAdapterPosition)?.let {
            productCardGridView?.setImageProductViewHintListener(it, object : ViewHintListener {
                override fun onViewHint() {
                    onItemImpressedListener.onItemImpressed(productCardModel, bindingAdapterPosition)
                }
            })
        }

        productCardGridView?.setAddToCartOnClickListener {
            onItemAddToCartListener?.onItemAddToCart(productCardModel, bindingAdapterPosition)
        }

        productCardGridView?.setThreeDotsOnClickListener {
            onItemThreeDotsClickListener?.onItemThreeDotsClick(productCardModel, bindingAdapterPosition)
        }

        productCardGridView?.setAddToCartNonVariantClickListener(object: ATCNonVariantListener {
            override fun onQuantityChanged(quantity: Int) {
                onATCNonVariantClickListener?.onATCNonVariantClick(productCardModel, bindingAdapterPosition, quantity)
            }
        })

        productCardGridView?.setAddVariantClickListener {
            onAddVariantClickListener?.onAddVariantClick(productCardModel, bindingAdapterPosition)
        }

        productCardGridView?.setSeeOtherProductOnClickListener {
            onSeeOtherProductClickListener?.onSeeOtherProductClick(productCardModel, bindingAdapterPosition)
        }
    }

    override fun recycle() {
        val productCardView = productCardGridView()
        productCardView?.recycle()

        unregisterProductCardLifecycleObserver(productCardView)
    }
}
