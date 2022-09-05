package com.tokopedia.tokopedianow.home.presentation.viewholder

import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.ViewHintListener
import com.tokopedia.productcard.ATCNonVariantListener
import com.tokopedia.tokopedianow.R
import com.tokopedia.tokopedianow.databinding.ItemTokopedianowHomeLeftCarouselAtcProductCardBinding
import com.tokopedia.tokopedianow.home.presentation.uimodel.HomeLeftCarouselAtcProductCardUiModel
import com.tokopedia.utils.view.binding.viewBinding

class HomeLeftCarouselAtcProductCardViewHolder(
    itemView: View,
    private var listener: HomeLeftCarouselAtcProductCardListener? = null
): AbstractViewHolder<HomeLeftCarouselAtcProductCardUiModel>(itemView) {

    companion object {
        private const val SUBTRACTION_POSITION = 1

        @LayoutRes
        val LAYOUT = R.layout.item_tokopedianow_home_left_carousel_atc_product_card
    }

    private var binding: ItemTokopedianowHomeLeftCarouselAtcProductCardBinding? by viewBinding()

    override fun bind(element: HomeLeftCarouselAtcProductCardUiModel) {
        binding?.productCardGridView?.apply {
            applyCarousel()
            layoutParams?.height = ViewGroup.LayoutParams.MATCH_PARENT
            setProductModel(element.productCardModel)
            setOnClickListener {
                listener?.onProductCardClicked(
                    position = adapterPosition,
                    product = element
                )
            }
            setAddVariantClickListener {
                listener?.onProductCardAddVariantClicked(element)
            }
            setAddToCartNonVariantClickListener(object: ATCNonVariantListener {
                override fun onQuantityChanged(quantity: Int) {
                    listener?.onProductCardQuantityChanged(
                        product = element,
                        quantity = quantity
                    )
                }
            })
            setImageProductViewHintListener(element, object : ViewHintListener{
                override fun onViewHint() {
                    listener?.onProductCardImpressed(
                        position = adapterPosition - SUBTRACTION_POSITION,
                        product = element
                    )
                }
            })
        }
    }

    interface HomeLeftCarouselAtcProductCardListener {
        fun onProductCardAddVariantClicked(product: HomeLeftCarouselAtcProductCardUiModel)
        fun onProductCardQuantityChanged(product: HomeLeftCarouselAtcProductCardUiModel, quantity: Int)
        fun onProductCardClicked(position: Int, product: HomeLeftCarouselAtcProductCardUiModel)
        fun onProductCardImpressed(position: Int, product: HomeLeftCarouselAtcProductCardUiModel)
    }
}