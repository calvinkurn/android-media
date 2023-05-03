package com.tokopedia.productcard.compact.productcardcarousel.presentation.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.kotlin.extensions.view.getScreenWidth
import com.tokopedia.productcard.compact.R
import com.tokopedia.productcard.compact.common.util.ViewUtil.getDpFromDimen
import com.tokopedia.productcard.compact.databinding.ItemProductCardCompactCarouselBinding
import com.tokopedia.productcard.compact.productcardcarousel.presentation.uimodel.ProductCardCompactCarouselItemUiModel
import com.tokopedia.utils.view.binding.viewBinding

class ProductCardCompactCarouselItemViewHolder(
    view: View,
    private var listener: ProductCardCarouselItemListener? = null
) : AbstractViewHolder<ProductCardCompactCarouselItemUiModel>(view) {

    companion object {
        private const val EXPECTED_WIDTH_PERCENTAGE = 0.3

        @LayoutRes
        val LAYOUT = R.layout.item_product_card_compact_carousel
    }

    private var binding: ItemProductCardCompactCarouselBinding? by viewBinding()

    init {
        setProductWidth()
    }

    override fun bind(element: ProductCardCompactCarouselItemUiModel) {
        binding?.productCard?.apply {
            setData(
                model = element.productCardModel
            )
            setOnClickListener {
                listener?.onProductCardClicked(
                    position = layoutPosition,
                    product = element
                )
            }
            setOnClickQuantityEditorListener { quantity ->
                listener?.onProductCardQuantityChanged(
                    position = layoutPosition,
                    product = element,
                    quantity = quantity
                )
            }
            setOnClickQuantityEditorVariantListener {
                listener?.onProductCardAddVariantClicked(
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
            binding?.productCard?.setData(
                model = element.productCardModel
            )
        }
    }

    private fun setProductWidth() {
        binding?.apply {
            val spaceWidth = getDpFromDimen(
                context = root.context,
                id = R.dimen.product_card_compact_product_card_total_space_width
            )
            productCard.layoutParams.width = (EXPECTED_WIDTH_PERCENTAGE * (getScreenWidth() - spaceWidth)).toInt()
        }
    }

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
    }
}
