package com.tokopedia.shop.home.view.adapter.viewholder.thematicwidget.viewholder

import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.ViewHintListener
import com.tokopedia.shop.R
import com.tokopedia.shop.home.view.model.ShopHomeProductUiModel
import com.tokopedia.shop.home.view.model.thematicwidget.ThematicWidgetUiModel
import com.tokopedia.shop.databinding.ItemProductCardListBinding
import com.tokopedia.shop.home.util.mapper.ShopPageHomeMapper
import com.tokopedia.utils.view.binding.viewBinding

class ProductCardListViewHolder(
    itemView: View,
    private var listener: ProductCardListener? = null,
    private val isOverrideWidgetTheme: Boolean,
    private val thematicWidgetUiModel: ThematicWidgetUiModel
) : AbstractViewHolder<ShopHomeProductUiModel>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_product_card_list
    }

    private var binding: ItemProductCardListBinding? by viewBinding()

    override fun bind(element: ShopHomeProductUiModel) {
        binding?.productCardGridView?.apply {
            applyCarousel()
            layoutParams?.height = ViewGroup.LayoutParams.MATCH_PARENT
            setProductModel(
                ShopPageHomeMapper.mapToProductCardCampaignModel(
                    isHasAddToCartButton = false,
                    hasThreeDots = false,
                    shopHomeProductViewModel = element,
                    widgetName = thematicWidgetUiModel.name,
                    statusCampaign = thematicWidgetUiModel.statusCampaign,
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
        fun onProductCardClickListener(product: ShopHomeProductUiModel)
        fun onProductCardImpressListener(product: ShopHomeProductUiModel)
    }
}
