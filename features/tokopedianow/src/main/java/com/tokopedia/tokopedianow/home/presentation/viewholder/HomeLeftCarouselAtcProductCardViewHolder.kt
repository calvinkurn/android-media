package com.tokopedia.tokopedianow.home.presentation.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.productcard_compact.databinding.ItemTokopedianowProductCardCarouselBinding
import com.tokopedia.tokopedianow.R
import com.tokopedia.tokopedianow.home.presentation.uimodel.HomeLeftCarouselAtcProductCardUiModel
import com.tokopedia.utils.view.binding.viewBinding

class HomeLeftCarouselAtcProductCardViewHolder(
    itemView: View,
    private var listener: HomeLeftCarouselAtcProductCardListener? = null
): AbstractViewHolder<HomeLeftCarouselAtcProductCardUiModel>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_tokopedianow_product_card_carousel
    }

    private var binding: ItemTokopedianowProductCardCarouselBinding? by viewBinding()

    override fun bind(element: HomeLeftCarouselAtcProductCardUiModel) {
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
                    product = element,
                    quantity = quantity
                )
            }
            setOnClickQuantityEditorVariantListener {
                listener?.onProductCardAddVariantClicked(
                    product = element
                )
            }
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
    }
}
