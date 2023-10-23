package com.tokopedia.tokopedianow.home.presentation.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.productcard.compact.databinding.ItemProductCardCompactCarouselBinding
import com.tokopedia.productcard.compact.productcard.presentation.listener.ProductCardCompactViewListener
import com.tokopedia.tokopedianow.home.presentation.uimodel.HomeLeftCarouselAtcProductCardUiModel
import com.tokopedia.utils.view.binding.viewBinding
import com.tokopedia.productcard.compact.R as productcardcompactR

class HomeLeftCarouselAtcProductCardViewHolder(
    itemView: View,
    private var listener: HomeLeftCarouselAtcProductCardListener? = null
): AbstractViewHolder<HomeLeftCarouselAtcProductCardUiModel>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = productcardcompactR.layout.item_product_card_compact_carousel
    }

    private var binding: ItemProductCardCompactCarouselBinding? by viewBinding()

    override fun bind(element: HomeLeftCarouselAtcProductCardUiModel) {
        binding?.productCard?.apply {
            val productCardListener = ProductCardCompactViewListener(
                onQuantityChangedListener = { quantity ->
                    listener?.onProductCardQuantityChanged(
                        product = element,
                        quantity = quantity
                    )
                },
                clickAddVariantListener = {
                    listener?.onProductCardAddVariantClicked(
                        product = element
                    )
                },
                blockAddToCartListener = {
                    listener?.onProductCardAddToCartBlocked()
                }
            )

            setOnClickListener {
                listener?.onProductCardClicked(
                    position = layoutPosition,
                    product = element
                )
            }

            bind(
                model = element.productCardModel,
                listener = productCardListener
            )

            addOnImpressionListener(element) {
                listener?.onProductCardImpressed(
                    position = layoutPosition,
                    product = element
                )
            }
        }
    }

    interface HomeLeftCarouselAtcProductCardListener {
        fun onProductCardAddVariantClicked(
            product: HomeLeftCarouselAtcProductCardUiModel
        )
        fun onProductCardQuantityChanged(
            product: HomeLeftCarouselAtcProductCardUiModel,
            quantity: Int
        )
        fun onProductCardClicked(
            position: Int,
            product: HomeLeftCarouselAtcProductCardUiModel
        )
        fun onProductCardImpressed(
            position: Int,
            product: HomeLeftCarouselAtcProductCardUiModel
        )
        fun onProductCardAddToCartBlocked()
    }
}
