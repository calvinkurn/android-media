package com.tokopedia.shop.home.view.adapter.viewholder.thematicwidget.viewholder

import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.ViewHintListener
import com.tokopedia.shop.R
import com.tokopedia.shop.databinding.ItemProductCardGridBinding
import com.tokopedia.shop.home.util.mapper.ShopPageHomeMapper
import com.tokopedia.shop.home.view.model.ShopHomeProductUiModel
import com.tokopedia.shop.home.view.model.thematicwidget.ThematicWidgetUiModel
import com.tokopedia.utils.view.binding.viewBinding

class ProductCardGridViewHolder(
    itemView: View,
    private var listener: ProductCardListener? = null,
    private val isOverrideWidgetTheme: Boolean,
    private val thematicWidgetUiModel: ThematicWidgetUiModel,
    private val isFestivity: Boolean
) : AbstractViewHolder<ShopHomeProductUiModel>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_product_card_grid
    }

    private var binding: ItemProductCardGridBinding? by viewBinding()

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
                    isOverrideTheme = isOverrideWidgetTheme,
                    patternColorType = listener?.getPatternColorType().orEmpty(),
                    backgroundColor = listener?.getBackgroundColor().orEmpty(),
                    isFestivity = isFestivity,
                    atcVariantButtonText = context?.getString(R.string.shop_atc).orEmpty()
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
        fun getPatternColorType(): String
        fun getBackgroundColor(): String
    }
}
