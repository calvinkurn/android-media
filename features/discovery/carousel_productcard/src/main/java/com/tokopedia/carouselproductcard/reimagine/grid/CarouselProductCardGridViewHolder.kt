package com.tokopedia.carouselproductcard.reimagine.grid

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.carouselproductcard.R
import com.tokopedia.carouselproductcard.databinding.CarouselProductCardReimagineGridItemBinding
import com.tokopedia.productcard.ProductCardClickListener
import com.tokopedia.utils.view.binding.viewBinding

internal class CarouselProductCardGridViewHolder(
    itemView: View,
): AbstractViewHolder<CarouselProductCardGridModel>(itemView) {

    private val binding: CarouselProductCardReimagineGridItemBinding? by viewBinding()
    private var element: CarouselProductCardGridModel? = null

    override fun bind(element: CarouselProductCardGridModel) {
        this.element = element
        binding?.carouselProductCardReimagineGridItem?.run {
            setProductModel(element.productCardModel)

            addOnImpressionListener(element)

            setOnClickListener(object : ProductCardClickListener {
                override fun onClick(v: View) {
                    element.onClick()
                }

                override fun onAreaClicked(v: View) {
                    element.onAreaClicked()
                }

                override fun onProductImageClicked(v: View) {
                    element.onProductImageClicked()
                }

                override fun onSellerInfoClicked(v: View) {
                    element.onSellerInfoClicked()
                }
            })


            setAddToCartOnClickListener { element.onAddToCart() }
        }
    }

    override fun onViewAttachedToWindow() {
        element?.onViewAttachedToWindow?.invoke()
    }

    override fun onViewDetachedFromWindow(
        visiblePercentage: Int
    ) {
        element?.onViewDetachedFromWindow?.invoke(visiblePercentage)
    }

    private fun addOnImpressionListener(element: CarouselProductCardGridModel) {
        val impressHolder = element.impressHolder(bindingAdapterPosition) ?: return

        binding?.carouselProductCardReimagineGridItem?.addOnImpressionListener(impressHolder) {
            element.onImpressed()
        }
    }

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.carousel_product_card_reimagine_grid_item
    }
}
