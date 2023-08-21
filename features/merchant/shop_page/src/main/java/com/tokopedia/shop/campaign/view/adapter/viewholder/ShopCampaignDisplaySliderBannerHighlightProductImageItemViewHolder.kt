package com.tokopedia.shop.campaign.view.adapter.viewholder

import android.content.res.Resources
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.media.loader.loadImage
import com.tokopedia.shop.R
import com.tokopedia.shop.campaign.view.model.ShopWidgetDisplaySliderBannerHighlightUiModel
import com.tokopedia.shop.common.util.ShopUtil
import com.tokopedia.shop.databinding.ItemShopCampaignSliderBannerHighlightProductImageItemBinding
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifycomponents.toPx
import com.tokopedia.utils.view.binding.viewBinding

class ShopCampaignDisplaySliderBannerHighlightProductImageItemViewHolder(
    binding: ItemShopCampaignSliderBannerHighlightProductImageItemBinding,
    private val widgetUiModel: ShopWidgetDisplaySliderBannerHighlightUiModel?,
    private val listener: ShopCampaignDisplaySliderBannerHighlightViewHolder.Listener
) : RecyclerView.ViewHolder(binding.root) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_shop_campaign_slider_banner_highlight_product_image_item
        private const val MAXIMUM_PRODUCT_IMAGE_ON_SCREEN = 3
        private const val PARENT_HORIZONTAL_MARGIN = 32
    }

    private val viewBinding: ItemShopCampaignSliderBannerHighlightProductImageItemBinding? by viewBinding()
    private val productImage: ImageUnify? = viewBinding?.productImage

    fun bind(uiModel: ShopWidgetDisplaySliderBannerHighlightUiModel.ProductHighlightData) {
        setProductImage(uiModel)
        setImpressionListener(uiModel)
        setClickedListener(uiModel)
    }

    private fun setClickedListener(uiModel: ShopWidgetDisplaySliderBannerHighlightUiModel.ProductHighlightData) {
        itemView.setOnClickListener{
            widgetUiModel?.let {
                listener.onProductImageClicked(
                    it,
                    uiModel,
                    ShopUtil.getActualPositionFromIndex(bindingAdapterPosition)
                )
            }
        }
    }

    private fun setImpressionListener(uiModel: ShopWidgetDisplaySliderBannerHighlightUiModel.ProductHighlightData) {
        itemView.addOnImpressionListener(uiModel){
            widgetUiModel?.let {
                listener.onProductImageImpression(
                    it,
                    uiModel,
                    ShopUtil.getActualPositionFromIndex(bindingAdapterPosition)
                )
            }
        }
    }

    private fun setProductImage(uiModel: ShopWidgetDisplaySliderBannerHighlightUiModel.ProductHighlightData) {
        productImage?.apply {
            loadImage(uiModel.imageUrl)
            val deviceWidth = Resources.getSystem().displayMetrics.widthPixels
            val widgetWidth = deviceWidth - PARENT_HORIZONTAL_MARGIN.toPx()
            layoutParams?.width = (widgetWidth / MAXIMUM_PRODUCT_IMAGE_ON_SCREEN)
        }
    }

}
