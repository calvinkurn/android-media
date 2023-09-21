package com.tokopedia.shop_widget.thematicwidget.viewholder

import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.ViewHintListener
import com.tokopedia.shop_widget.R
import com.tokopedia.shop_widget.common.util.ProductCardMapper.mapToProductCardCampaignModel
import com.tokopedia.shop_widget.databinding.ItemProductCardListBinding
import com.tokopedia.shop_widget.thematicwidget.uimodel.ProductCardUiModel
import com.tokopedia.utils.view.binding.viewBinding

class ProductCardListViewHolder(
    itemView: View,
    private var listener: ProductCardListener? = null,
    private val isOverrideWidgetTheme: Boolean
) : AbstractViewHolder<ProductCardUiModel>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_product_card_list
    }

    private var binding: ItemProductCardListBinding? by viewBinding()

    override fun bind(element: ProductCardUiModel) {
        binding?.productCardGridView?.apply {
            applyCarousel()
            layoutParams?.height = ViewGroup.LayoutParams.MATCH_PARENT
            setProductModel(
                mapToProductCardCampaignModel(
                    isHasAddToCartButton = false,
                    hasThreeDots = false,
                    productCardUiModel = element,
                    forceLightModeColor = isOverrideWidgetTheme
                )
            )
            setImageProductViewHintListener(
                element,
                object : ViewHintListener {
                    override fun onViewHint() {
                        listener?.onProductCardImpressListener(element)
                    }
                }
            )
        }

        binding?.productCardGridView?.setOnClickListener {
            listener?.onProductCardClickListener(element)
        }
    }

    interface ProductCardListener {
        fun onProductCardClickListener(product: ProductCardUiModel)
        fun onProductCardImpressListener(product: ProductCardUiModel)
    }
}
