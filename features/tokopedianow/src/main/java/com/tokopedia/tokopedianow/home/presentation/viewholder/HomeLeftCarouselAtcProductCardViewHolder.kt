package com.tokopedia.tokopedianow.home.presentation.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.tokopedianow.R
import com.tokopedia.tokopedianow.databinding.ItemTokopedianowHomeLeftCarouselAtcProductCardBinding
import com.tokopedia.tokopedianow.home.presentation.uimodel.HomeLeftCarouselAtcProductCardUiModel
import com.tokopedia.utils.view.binding.viewBinding

class HomeLeftCarouselAtcProductCardViewHolder(
    itemView: View,
    private var listener: HomeLeftCarouselAtcProductCardListener? = null
): AbstractViewHolder<HomeLeftCarouselAtcProductCardUiModel>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_tokopedianow_home_left_carousel_atc_product_card
    }

    private var binding: ItemTokopedianowHomeLeftCarouselAtcProductCardBinding? by viewBinding()

    init {
        setIsRecyclable(false)
    }

    override fun bind(element: HomeLeftCarouselAtcProductCardUiModel) {
        binding?.productCard?.apply {
            setData(
                model = element.productCardModel
            )
            setOnClickListener {
                listener?.onProductCardClicked(
                    position = element.position,
                    product = element
                )
            }
            setOnClickQuantityEditorListener(
                onClickListener = { quantity ->
                    listener?.onProductCardQuantityChanged(
                        product = element,
                        quantity = quantity
                    )
                },
                onClickVariantListener = {}
            )
            addOnImpressionListener(element) {
                listener?.onProductCardImpressed(
                    position = element.position,
                    product = element
                )
            }
        }

        //            setAddToCartNonVariantClickListener(object: ATCNonVariantListener {
//                override fun onQuantityChanged(quantity: Int) {
//                    listener?.onProductCardQuantityChanged(
//                        product = element,
//                        quantity = quantity
//                    )
//                }
//            })
    }

    interface HomeLeftCarouselAtcProductCardListener {
        fun onProductCardAddVariantClicked(product: HomeLeftCarouselAtcProductCardUiModel)
        fun onProductCardQuantityChanged(product: HomeLeftCarouselAtcProductCardUiModel, quantity: Int)
        fun onProductCardClicked(position: Int, product: HomeLeftCarouselAtcProductCardUiModel)
        fun onProductCardImpressed(position: Int, product: HomeLeftCarouselAtcProductCardUiModel)
    }
}
