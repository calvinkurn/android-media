package com.tokopedia.carouselproductcard.reimagine.grid

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.carouselproductcard.R
import com.tokopedia.carouselproductcard.databinding.CarouselProductCardReimagineGridItemBinding
import com.tokopedia.productcard.ProductCardClickListener
import com.tokopedia.productcard.layout.ProductConstraintLayout
import com.tokopedia.utils.view.binding.viewBinding

internal class CarouselProductCardGridViewHolder(
    itemView: View,
): AbstractViewHolder<CarouselProductCardGridModel>(itemView) {

    private val binding: CarouselProductCardReimagineGridItemBinding? by viewBinding()

    override fun bind(element: CarouselProductCardGridModel) {
        binding?.carouselProductCardReimagineGridItem?.run {
            setProductModel(element.productCardModel)

            addOnImpressionListener(element)
            setVisibilityPercentListener(element.productCardModel.isAds, object : ProductConstraintLayout.OnVisibilityPercentChanged {
                override fun onShow() {
                    element.onViewAttachedToWindow()
                }

                override fun onShowOver(maxPercentage: Int) {
                    element.onViewDetachedFromWindow(maxPercentage)
                }
            })
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
