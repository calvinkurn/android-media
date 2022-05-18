package com.tokopedia.tokopedianow.home.presentation.viewholder

import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.home_component.mapper.ChannelModelMapper
import com.tokopedia.kotlin.extensions.view.ViewHintListener
import com.tokopedia.tokopedianow.R
import com.tokopedia.tokopedianow.databinding.ItemTokopedianowHomeLeftCarouselProductCardBinding
import com.tokopedia.tokopedianow.home.presentation.uimodel.HomeLeftCarouselProductCardUiModel
import com.tokopedia.utils.view.binding.viewBinding

class HomeLeftCarouselProductCardViewHolder(
    itemView: View,
    private var listener: ProductCardListener? = null
): AbstractViewHolder<HomeLeftCarouselProductCardUiModel>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_tokopedianow_home_left_carousel_product_card
    }

    private var binding: ItemTokopedianowHomeLeftCarouselProductCardBinding? by viewBinding()

    override fun bind(element: HomeLeftCarouselProductCardUiModel) {
        binding?.productCardGridView?.apply {
            applyCarousel()
            layoutParams?.height = ViewGroup.LayoutParams.MATCH_PARENT
            setProductModel(element.productCardModel)
            setImageProductViewHintListener(element, object : ViewHintListener{
                override fun onViewHint() {
                    listener?.onProductCardImpressListener(element)
                }
            })
        }

        binding?.productCardGridView?.setOnClickListener {
            listener?.onProductCardClickListener(element)
        }
    }

    interface ProductCardListener {
        fun onProductCardClickListener(product: HomeLeftCarouselProductCardUiModel)
        fun onProductCardImpressListener(product: HomeLeftCarouselProductCardUiModel)
    }
}