package com.tokopedia.productcard.compact.productcardcarousel.presentation.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.productcard.compact.R
import com.tokopedia.productcard.compact.databinding.ItemProductCardCompactCarouselBinding
import com.tokopedia.productcard.compact.productcard.presentation.listener.ProductCardCompactViewListener
import com.tokopedia.productcard.compact.productcardcarousel.presentation.uimodel.ProductCardCompactCarouselItemUiModel
import com.tokopedia.productcard.compact.productcardcarousel.util.ProductCardExtension.setProductCarouselWidth
import com.tokopedia.utils.view.binding.viewBinding

class ProductCardCompactCarouselItemViewHolder(
    view: View,
    private var listener: ProductCardCarouselItemListener? = null
) : AbstractViewHolder<ProductCardCompactCarouselItemUiModel>(view) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_product_card_compact_carousel
    }

    private var binding: ItemProductCardCompactCarouselBinding? by viewBinding()

    init {
        binding?.productCard?.setProductCarouselWidth()
    }

    override fun bind(element: ProductCardCompactCarouselItemUiModel) {
        binding?.productCard?.apply {
            bind(
                model = element.productCardModel,
                listener = createProductListener(element)
            )

            setOnClickListener {
                listener?.onProductCardClicked(
                    position = layoutPosition,
                    product = element
                )
            }

            addOnImpressionListener(element.impressHolder) {
                listener?.onProductCardImpressed(
                    position = layoutPosition,
                    product = element
                )
            }
        }
    }

    override fun bind(element: ProductCardCompactCarouselItemUiModel?, payloads: MutableList<Any>) {
        if (payloads.firstOrNull() == true && element != null) {
            binding?.productCard?.bind(
                model = element.productCardModel,
                listener = createProductListener(element)
            )
        }
    }

    private fun createProductListener(
        element: ProductCardCompactCarouselItemUiModel
    ) = ProductCardCompactViewListener(
        onQuantityChangedListener = { quantity ->
            listener?.onProductCardQuantityChanged(
                position = layoutPosition,
                product = element,
                quantity = quantity
            )
        },
        clickAddVariantListener = {
            listener?.onProductCardAddVariantClicked(
                position = layoutPosition,
                product = element
            )
        },
        blockAddToCartListener = {
            listener?.onProductCardAddToCartBlocked()
        }
    )

    interface ProductCardCarouselItemListener {
        fun onProductCardAddVariantClicked(
            position: Int,
            product: ProductCardCompactCarouselItemUiModel
        )
        fun onProductCardQuantityChanged(
            position: Int,
            product: ProductCardCompactCarouselItemUiModel,
            quantity: Int
        )
        fun onProductCardClicked(
            position: Int,
            product: ProductCardCompactCarouselItemUiModel
        )
        fun onProductCardImpressed(
            position: Int,
            product: ProductCardCompactCarouselItemUiModel
        )
        fun onProductCardAddToCartBlocked()
    }
}
