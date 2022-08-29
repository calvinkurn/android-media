package com.tokopedia.carouselproductcard

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.carouselproductcard.databinding.CarouselProductCardItemListLayoutBinding
import com.tokopedia.kotlin.extensions.view.ViewHintListener
import com.tokopedia.productcard.ATCNonVariantListener
import com.tokopedia.utils.view.binding.viewBinding

internal class CarouselProductCardListViewHolder(
    itemView: View,
    internalListener: CarouselProductCardInternalListener,
): BaseProductCardViewHolder<CarouselProductCardModel>(itemView, internalListener) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.carousel_product_card_item_list_layout
    }

    private var binding: CarouselProductCardItemListLayoutBinding? by viewBinding()

    override fun bind(carouselProductCardModel: CarouselProductCardModel) {
        val binding = binding ?: return
        val productCardModel = carouselProductCardModel.productCardModel

        binding.carouselProductCardItem.applyCarousel()

        binding.carouselProductCardItem.setProductModel(productCardModel)

        registerProductCardLifecycleObserver(binding.carouselProductCardItem, productCardModel)

        setCarouselProductCardListeners(carouselProductCardModel)
    }

    private fun setCarouselProductCardListeners(carouselProductCardModel: CarouselProductCardModel) {
        val binding = binding ?: return
        val productCardModel = carouselProductCardModel.productCardModel

        val onItemClickListener = carouselProductCardModel.getOnItemClickListener()
        val onItemImpressedListener = carouselProductCardModel.getOnItemImpressedListener()
        val onItemAddToCartListener = carouselProductCardModel.getOnItemAddToCartListener()
        val onItemThreeDotsClickListener = carouselProductCardModel.getOnItemThreeDotsClickListener()
        val onATCNonVariantClickListener = carouselProductCardModel.getOnATCNonVariantClickListener()
        val onAddVariantClickListener = carouselProductCardModel.getAddVariantClickListener()

        binding.carouselProductCardItem.setOnClickListener {
            onItemClickListener?.onItemClick(productCardModel, adapterPosition)
        }

        onItemImpressedListener?.getImpressHolder(adapterPosition)?.let {
            binding.carouselProductCardItem.setImageProductViewHintListener(it, object : ViewHintListener {
                override fun onViewHint() {
                    onItemImpressedListener.onItemImpressed(productCardModel, adapterPosition)
                }
            })
        }

        binding.carouselProductCardItem.setAddToCartOnClickListener {
            onItemAddToCartListener?.onItemAddToCart(productCardModel, adapterPosition)
        }

        binding.carouselProductCardItem.setThreeDotsOnClickListener {
            onItemThreeDotsClickListener?.onItemThreeDotsClick(productCardModel, adapterPosition)
        }

        binding.carouselProductCardItem.setAddToCartNonVariantClickListener(object: ATCNonVariantListener {
            override fun onQuantityChanged(quantity: Int) {
                onATCNonVariantClickListener?.onATCNonVariantClick(productCardModel, adapterPosition, quantity)
            }
        })

        binding.carouselProductCardItem.setAddVariantClickListener {
            onAddVariantClickListener?.onAddVariantClick(productCardModel, adapterPosition)
        }
    }

    override fun recycle() {
        binding?.carouselProductCardItem?.recycle()

        unregisterProductCardLifecycleObserver(binding?.carouselProductCardItem)
    }
}