package com.tokopedia.tokopedianow.home.presentation.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.tokopedianow.R
import com.tokopedia.tokopedianow.common.model.LabelGroup
import com.tokopedia.tokopedianow.common.model.TokoNowProductCardViewUiModel
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
        binding?.productCard?.setData(
            model = element.productCardModel
        )

//        binding?.productCardGridView?.apply {
//            setOnClickListener {
//                listener?.onProductCardClicked(
//                    position = element.position,
//                    product = element
//                )
//            }
//            setAddVariantClickListener {
//                listener?.onProductCardAddVariantClicked(element)
//            }
//            setAddToCartNonVariantClickListener(object: ATCNonVariantListener {
//                override fun onQuantityChanged(quantity: Int) {
//                    listener?.onProductCardQuantityChanged(
//                        product = element,
//                        quantity = quantity
//                    )
//                }
//            })
//            setImageProductViewHintListener(element, object : ViewHintListener{
//                override fun onViewHint() {
//                    listener?.onProductCardImpressed(
//                        position = element.position,
//                        product = element
//                    )
//                }
//            })
//        }
    }

    interface HomeLeftCarouselAtcProductCardListener {
        fun onProductCardAddVariantClicked(product: HomeLeftCarouselAtcProductCardUiModel)
        fun onProductCardQuantityChanged(product: HomeLeftCarouselAtcProductCardUiModel, quantity: Int)
        fun onProductCardClicked(position: Int, product: HomeLeftCarouselAtcProductCardUiModel)
        fun onProductCardImpressed(position: Int, product: HomeLeftCarouselAtcProductCardUiModel)
    }
}
