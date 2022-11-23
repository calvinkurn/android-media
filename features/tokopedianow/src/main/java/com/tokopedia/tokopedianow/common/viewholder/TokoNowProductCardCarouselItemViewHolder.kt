package com.tokopedia.tokopedianow.common.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.tokopedianow.R
import com.tokopedia.tokopedianow.common.model.TokoNowProductCardCarouselItemUiModel
import com.tokopedia.tokopedianow.databinding.ItemTokopedianowProductCardCarouselBinding
import com.tokopedia.utils.view.binding.viewBinding

class TokoNowProductCardCarouselItemViewHolder(
    view: View,
    private var listener: TokoNowCarouselProductCardItemListener? = null
) : AbstractViewHolder<TokoNowProductCardCarouselItemUiModel>(view) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_tokopedianow_product_card_carousel
    }

    private var binding: ItemTokopedianowProductCardCarouselBinding? by viewBinding()

    override fun bind(element: TokoNowProductCardCarouselItemUiModel) {
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

    override fun bind(element: TokoNowProductCardCarouselItemUiModel?, payloads: MutableList<Any>) {
        val payload = payloads.firstOrNull()

        if (payload != null && element != null) {
            binding?.productCard?.setData(
                model = element.productCardModel
            )
        } else {
            super.bind(element, payloads)
        }
    }

    interface TokoNowCarouselProductCardItemListener {
        fun onProductCardAddVariantClicked(
            position: Int,
            product: TokoNowProductCardCarouselItemUiModel
        )
        fun onProductCardQuantityChanged(
            position: Int,
            product: TokoNowProductCardCarouselItemUiModel,
            quantity: Int
        )
        fun onProductCardClicked(
            position: Int,
            product: TokoNowProductCardCarouselItemUiModel
        )
        fun onProductCardImpressed(
            position: Int,
            product: TokoNowProductCardCarouselItemUiModel
        )
    }
}
