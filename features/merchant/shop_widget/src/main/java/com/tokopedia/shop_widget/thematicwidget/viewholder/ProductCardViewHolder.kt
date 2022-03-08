package com.tokopedia.shop_widget.thematicwidget.viewholder

import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.shop_widget.R
import com.tokopedia.shop_widget.thematicwidget.uimodel.ProductCardUiModel
import com.tokopedia.shop_widget.common.util.ProductCardMapper.mapToProductCardCampaignModel
import com.tokopedia.shop_widget.databinding.ItemProductCardBinding
import com.tokopedia.utils.view.binding.viewBinding

class ProductCardViewHolder(
    itemView: View,
    private var listener: ProductCardListener? = null
): AbstractViewHolder<ProductCardUiModel>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_product_card
    }

    private var binding: ItemProductCardBinding? by viewBinding()

    override fun bind(element: ProductCardUiModel) {
        binding?.productCardGridView?.apply {
            applyCarousel()
            layoutParams?.height = ViewGroup.LayoutParams.MATCH_PARENT
            setProductModel(mapToProductCardCampaignModel(
                isHasAddToCartButton = false,
                hasThreeDots = false,
                productCardUiModel = element
            ))
        }

        binding?.productCardGridView?.setOnClickListener {
            listener?.onProductCardClickListener(element)
        }
    }

    interface ProductCardListener {
        fun onProductCardClickListener(product: ProductCardUiModel)
    }
}